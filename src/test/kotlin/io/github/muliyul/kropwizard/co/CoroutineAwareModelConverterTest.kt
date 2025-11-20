package io.github.muliyul.kropwizard.co

import io.mockk.mockk
import io.mockk.verify
import io.swagger.v3.core.converter.AnnotatedType
import io.swagger.v3.core.converter.ModelConverter
import io.swagger.v3.core.converter.ModelConverterContextImpl
import org.junit.jupiter.api.Assertions.*
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test

class CoroutineAwareModelConverterTest {
	private val converter = CoroutineAwareModelConverter()

	@Test
	fun `should ignore Continuation and CoroutineContext types`() {
		val continuationSchema = converter.resolve(
			type = AnnotatedType().type(Continuation::class.java),
			context = ModelConverterContextImpl(emptyList()),
			chain = mutableListOf<ModelConverter>().iterator()
		)
		assertNull(continuationSchema, "Continuation type should be ignored")

		val coroutineContextSchema = converter.resolve(
			type = AnnotatedType().type(CoroutineContext::class.java),
			context = ModelConverterContextImpl(emptyList()),
			chain = mutableListOf<ModelConverter>().iterator()
		)
		assertNull(coroutineContextSchema, "CoroutineContext type should be ignored")
	}

	@Test
	fun `should process normal types`() {
		val mc = mockk<ModelConverter>(relaxed = true)

		val schema = converter.resolve(
			type = AnnotatedType().type(String::class.java),
			context = ModelConverterContextImpl(emptyList()),
			chain = mutableListOf(mc).iterator()
		)

		assertNotNull(schema, "Normal type should be processed")

		verify(exactly = 1) {
			mc.resolve(any(), any(), any())
		}
	}
}