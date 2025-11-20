package io.github.muliyul.kropwizard

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.github.muliyul.kropwizard.co.CoroutineAwareModelConverter
import io.github.muliyul.kropwizard.co.CoroutineModelProcessor
import io.github.muliyul.kropwizard.config.OpenFeatureConfiguration
import dev.openfeature.kotlin.sdk.OpenFeatureAPI
import io.dropwizard.core.Configuration
import io.dropwizard.core.ConfiguredBundle
import io.dropwizard.core.setup.Bootstrap
import io.dropwizard.core.setup.Environment
import io.dropwizard.jersey.setup.JerseyEnvironment
import io.swagger.v3.core.converter.ModelConverters
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource
import kotlinx.coroutines.runBlocking
import ru.vyarus.dropwizard.guice.GuiceBundle


class KropwizardBundle<T : Configuration>(
	private val guiceBundleConfiguration: GuiceBundle.Builder.() -> Unit = { },
	private val openFeatureConfiguration: OpenFeatureAPI.() -> Unit = { }
) : ConfiguredBundle<T> {
	override fun initialize(bootstrap: Bootstrap<*>) {
		bootstrap.addBundle(
			GuiceBundle.builder()
				.enableAutoConfig(bootstrap.application.javaClass.`package`.name)
				.apply(guiceBundleConfiguration)
				.build()
		)
	}

	override fun run(configuration: T, environment: Environment) {
		environment.objectMapper.registerKotlinModule().apply {
			setDefaultPropertyInclusion(JsonInclude.Include.NON_NULL)
		}

		configuration.configureOpenFeature()

		environment.jersey().apply {
			configureSwagger()
			configureCoroutineSupport()
		}
	}

	private fun Configuration.configureOpenFeature() {
		if (this !is OpenFeatureConfiguration) return
		runBlocking { OpenFeatureAPI.setProviderAndWait(featureProvider) }
		OpenFeatureAPI.apply(openFeatureConfiguration)
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