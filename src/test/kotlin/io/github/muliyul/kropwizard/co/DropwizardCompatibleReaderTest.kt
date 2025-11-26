package io.github.muliyul.kropwizard.co

import io.github.muliyul.kropwizard.openapi.KotlinCompatibleReader
import io.github.muliyul.kropwizard.testapp.FakeResource
import io.swagger.v3.oas.models.OpenAPI
import jakarta.ws.rs.core.MediaType
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.expect

class DropwizardCompatibleReaderTest {
	private val reader = KotlinCompatibleReader()
	private val spec: OpenAPI by lazy(LazyThreadSafetyMode.NONE) { reader.read(FakeResource::class.java) }

	@Test
	fun `should ignore Continuation type`() {
		val operation = spec.paths["/fake"]?.get
		assertNull(operation?.requestBody, "Continuation type should be ignored as request body")
	}

	@Test
	fun `should process normal return type`() {
		val operation = spec.paths["/fake"]?.post
		expect("string") { operation?.requestBody?.content?.get(MediaType.WILDCARD)?.schema?.type }
	}

	@Test
	fun `should ignore @Auth parameter`() {
		val operation = spec.paths["/fake"]?.delete
		assertNull(operation?.requestBody, "@Auth parameter should be ignored as request body")
	}
}

