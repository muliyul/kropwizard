package io.github.muliyul.kropwizard

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.dropwizard.core.Configuration
import io.dropwizard.core.setup.Environment
import io.github.muliyul.kropwizard.co.CoroutineAwareModelConverter
import io.github.muliyul.kropwizard.co.CoroutineModelProcessor
import io.swagger.v3.core.converter.ModelConverters
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource
import kotlin.test.Test
import kotlin.test.assertContains

class KropwizardBundleTest {
	private val bundle = KropwizardBundle<Configuration>()
	private val environment = Environment("test")

	@Test
	fun `should register Jackson Kotlin module`() {
		bundle.run(Configuration(), environment)
		val mapper = environment.objectMapper
		assertContains(mapper.registeredModuleIds, KotlinModule::class.qualifiedName)
	}

	@Test
	fun `should register OpenApi resources`() {
		bundle.run(Configuration(), environment)

		val classes = environment.jersey().resourceConfig.classes

		assertContains(classes, OpenApiResource::class.java)
		assertContains(classes, AcceptHeaderOpenApiResource::class.java)
	}

	@Test
	fun `should register CoroutineModelProcessor and CoroutineAwareModelConverter`() {
		bundle.run(Configuration(), environment)

		assertContains(environment.jersey().resourceConfig.classes, CoroutineModelProcessor::class.java)

		ModelConverters.getInstance().converters.find { it is CoroutineAwareModelConverter }
			?: throw AssertionError("CoroutineAwareModelConverter not registered")
	}
}
