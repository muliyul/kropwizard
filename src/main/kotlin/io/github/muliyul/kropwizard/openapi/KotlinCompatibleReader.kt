package io.github.muliyul.kropwizard.openapi

import com.fasterxml.jackson.annotation.JsonView
import com.fasterxml.jackson.databind.type.SimpleType
import io.dropwizard.auth.Auth
import io.swagger.v3.jaxrs2.Reader
import io.swagger.v3.jaxrs2.ResolvedParameter
import io.swagger.v3.oas.models.Operation
import jakarta.ws.rs.Consumes
import java.lang.reflect.Type
import kotlin.coroutines.Continuation

@Suppress("unused")
class KotlinCompatibleReader : Reader() {

	override fun getParameters(
		type: Type?,
		annotations: MutableList<Annotation>?,
		operation: Operation,
		classConsumes: Consumes?,
		methodConsumes: Consumes?,
		jsonViewAnnotation: JsonView?
	): ResolvedParameter {
		val isAuthParam = annotations?.any { it.annotationClass == Auth::class } == true
		val isContinuationParam = (type as? SimpleType)?.isTypeOrSubTypeOf(Continuation::class.java) == true

		return super.getParameters(
			type,
			annotations,
			operation,
			classConsumes,
			methodConsumes,
			jsonViewAnnotation
		).apply {
			// instructs Swagger to ignore @Auth and Continuation types as body param
			if (isAuthParam || isContinuationParam) requestBody = null
		}
	}

}