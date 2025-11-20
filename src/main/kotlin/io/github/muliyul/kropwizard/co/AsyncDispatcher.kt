package io.github.muliyul.kropwizard.co

/**
 * Annotation that allows explicitly setting the dispatcher for coroutine async jersey resource methods.
 * [dispatcher] can be any of: "Default", "IO", "Unconfined"
 * ("Main" supported if a main dispatcher artifact is imported, but generally doesn't apply to a server)
 */
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.CLASS)
annotation class AsyncDispatcher(val dispatcher: String) {
	companion object {
		const val DEFAULT = "Default"
		const val IO = "IO"
		const val UNCONFINED = "Unconfined"
		const val MAIN = "Main"
	}
}