package io.github.muliyul.kropwizard.co

import io.github.muliyul.kropwizard.FakeResource
import jakarta.ws.rs.core.MediaType
import kotlin.test.Test
import kotlin.test.assertNull
import kotlin.test.expect

class DropwizardCompatibleReaderTest {
	private val reader = DropwizardCompatibleReader()

	@Test
	fun `should ignore Continuation type`() {
		val result = reader.read(FakeResource::class.java)

		val operation = result.paths["/fake"]?.get
		assertNull(operation?.requestBody, "Continuation type should be ignored as request body")
	}

	@Test
	fun `should process normal return type`() {
		val result = reader.read(FakeResource::class.java)
		val operation = result.paths["/fake"]?.post
		expect("string") { operation?.requestBody?.content?.get(MediaType.WILDCARD)?.schema?.type }
	}

	@Test
	fun `should ignore @Auth parameter`() {
		val result = reader.read(FakeResource::class.java)
		val operation = result.paths["/fake"]?.delete
		assertNull(operation?.requestBody, "@Auth parameter should be ignored as request body")
	}
}

