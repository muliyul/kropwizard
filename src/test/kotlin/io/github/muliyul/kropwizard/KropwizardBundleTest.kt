package io.github.muliyul.kropwizard

import com.fasterxml.jackson.module.kotlin.KotlinModule
import io.dropwizard.core.Application
import io.dropwizard.core.Configuration
import io.dropwizard.core.setup.Bootstrap
import io.dropwizard.core.setup.Environment
import io.github.muliyul.kropwizard.co.CoroutineAwareModelConverter
import io.github.muliyul.kropwizard.co.CoroutineModelProcessor
import io.github.muliyul.kropwizard.config.OpenFeatureConfiguration
import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifyAll
import io.swagger.v3.core.converter.ModelConverters
import io.swagger.v3.jaxrs2.integration.resources.AcceptHeaderOpenApiResource
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource
import jakarta.ws.rs.GET
import jakarta.ws.rs.Path
import ru.vyarus.dropwizard.guice.test.ClientSupport
import ru.vyarus.dropwizard.guice.test.jupiter.TestDropwizardApp
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.expect

class KropwizardBundleTest {
	private val bundle = KropwizardBundle<Configuration>()
	private val environment = Environment("test")

	@Test
	fun `should install Guice bundle`() {
		val app = mockk<Application<Configuration>>()
		val bootstrap = spyk(Bootstrap(app))
		bundle.initialize(bootstrap)

		verify(exactly = 1) {
			bootstrap.addBundle(withArg { it is ru.vyarus.dropwizard.guice.GuiceBundle })
		}
	}

	@Test
	fun `should register jackson Kotlin module`() {
		bundle.run(Configuration(), environment)
		val mapper = environment.objectMapper
		assertContains(mapper.registeredModuleIds, KotlinModule::class.qualifiedName)
	}

	@Test
	fun `should configure OpenFeature`() {
		var configured = false
		val testBundle = KropwizardBundle<Configuration>(
			openFeatureConfiguration = {
				configured = true
			}
		)
		val configuration = mockk<Configuration>(moreInterfaces = arrayOf(OpenFeatureConfiguration::class), relaxed = true)
		testBundle.run(configuration, environment)
		expect(true) { configured }
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
