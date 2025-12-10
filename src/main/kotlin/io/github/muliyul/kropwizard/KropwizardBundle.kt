package io.github.muliyul.kropwizard

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.dropwizard.core.Configuration
import io.dropwizard.core.ConfiguredBundle
import io.dropwizard.core.setup.Bootstrap
import io.dropwizard.core.setup.Environment
import io.dropwizard.jersey.setup.JerseyEnvironment
import io.github.muliyul.kropwizard.co.CoroutineAwareModelConverter
import io.github.muliyul.kropwizard.co.CoroutineModelProcessor
import io.swagger.v3.core.converter.ModelConverters
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource


class KropwizardBundle<T : Configuration> : ConfiguredBundle<T> {
	override fun initialize(bootstrap: Bootstrap<*>) {
		bootstrap.objectMapper.registerKotlinModule().apply {
			setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
		}
	}

	override fun run(configuration: T, environment: Environment) {
		environment.jersey().apply {
			configureSwagger()
			configureCoroutineSupport()
		}
	}

	private fun JerseyEnvironment.configureSwagger() {
		register(OpenApiResource::class.java)
		register(AcceptHeaderOpenApiResource::class.java)

		ModelConverters.getInstance().addConverter(CoroutineAwareModelConverter())
	}

	private fun JerseyEnvironment.configureCoroutineSupport() {
		register(CoroutineModelProcessor::class.java)
	}
}