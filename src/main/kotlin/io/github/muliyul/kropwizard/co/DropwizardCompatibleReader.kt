package io.github.muliyul.kropwizard.co

import com.fasterxml.jackson.annotation.*
import com.fasterxml.jackson.databind.type.*
import io.dropwizard.auth.*
import io.swagger.v3.jaxrs2.*
import io.swagger.v3.oas.integration.SwaggerConfiguration
import io.swagger.v3.oas.models.*
import jakarta.ws.rs.Consumes
import java.lang.reflect.*
import kotlin.coroutines.*

@Suppress("unused")
class DropwizardCompatibleReader : Reader() {

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