package io.github.muliyul.kropwizard.co

import jakarta.inject.Provider
import jakarta.ws.rs.container.AsyncResponse
import jakarta.ws.rs.container.ConnectionCallback
import kotlinx.coroutines.*
import kotlinx.coroutines.slf4j.MDCContext
import org.glassfish.jersey.server.model.Invocable
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn

/**
 * [InvocationHandler] that runs a jersey resource method implemented by a suspend
 * function in a coroutine, and bridges the return/exception value into an [AsyncResponse]
 * for the request, allowing to suspend functions to transparently implement jersey async requests.
 *
 * Resource classes/methods annotated with [AsyncDispatcher] can specify which standard coroutine dispatcher
 * to launch the coroutine with, otherwise defaults to [Dispatchers.Unconfined].
 * Additionally, if a resource class implements [CoroutineScope], that scope is used to launch the coroutine.
 */
@Suppress("InjectDispatcher") // specify dispatchers by annotation instead of injection
class CoroutineInvocationHandler(
	private val asyncContextProvider: Provider<AsyncResponse>,
	private val originalObjectProvider: () -> Any,
	private val originalInvocable: Invocable,
	private val shouldIgnoreReturn: Boolean
) : InvocationHandler {
	private suspend fun invokeCoroutine(
		originalObject: Any,
		args: Array<out Any>,
		asyncResponse: AsyncResponse
	) {
		try {
			// Can't use .callSuspend() if the object gets subclassed dynamically by AOP,
			// so use suspendCoroutineUninterceptedOrReturn to get the current continuation
			val result: Any? = suspendCoroutineUninterceptedOrReturn { cont ->
				originalInvocable.handlingMethod.invoke(originalObject, *args, cont)
			}
			if (!shouldIgnoreReturn) {
				asyncResponse.resume(result)
			}
		} catch (e: Throwable) {
			var cause: Throwable? = e
			while (cause !is CancellationException && cause != null) {
				cause = cause.cause
			}
			if (cause is CancellationException) {
				asyncResponse.cancel()
			} else {
				asyncResponse.resume(e)
			}
		}
	}

	override fun invoke(proxy: Any, method: Method, args: Array<out Any>?): Any? {
		val asyncResponse = asyncContextProvider.get()
		check(asyncResponse.isSuspended) { "Can't suspend!" }
		val originalObject = originalObjectProvider()
		val methodAnnotation = originalInvocable.definitionMethod.getAnnotation(AsyncDispatcher::class.java)
		val classAnnotation =
			originalInvocable.definitionMethod.declaringClass.getAnnotation(AsyncDispatcher::class.java)
		val additionalContext = when ((methodAnnotation ?: classAnnotation)?.dispatcher) {
			"Default" -> Dispatchers.Default
			"IO" -> Dispatchers.IO
			"Main" -> Dispatchers.Main
			else -> Dispatchers.Unconfined
		}
		val scope = CoroutineScope(additionalContext + MDCContext())
		scope.launch {
			// cancel this coroutine when a client disconnection is detected by jersey
			asyncResponse.register(ConnectionCallback { cancel() })
			invokeCoroutine(originalObject, args ?: emptyArray(), asyncResponse)
		}
		return null
	}
}