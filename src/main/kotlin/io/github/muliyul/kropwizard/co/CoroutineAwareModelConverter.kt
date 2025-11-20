package io.github.muliyul.kropwizard.co

import com.fasterxml.jackson.databind.type.*
import io.swagger.v3.core.converter.*
import io.swagger.v3.oas.models.media.*
import kotlin.coroutines.*

@Suppress("unused")
class CoroutineAwareModelConverter : ModelConverter {
	private val ignoreTypes = listOf(Continuation::class.java, CoroutineContext::class.java)

	override fun resolve(
		type: AnnotatedType,
		context: ModelConverterContext,
		chain: MutableIterator<ModelConverter>
	): Schema<*>? {
		val introspected = type.type as? SimpleType
		return when {
			ignoreTypes.any { introspected?.isTypeOrSubTypeOf(it) == true } -> null
			chain.hasNext() -> chain.next().resolve(type, context, chain)
			else -> null
		}
	}
}