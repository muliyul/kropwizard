package io.github.muliyul.kropwizard.co

import io.dropwizard.testing.ConfigOverride
import io.dropwizard.testing.ResourceHelpers
import io.dropwizard.testing.junit5.DropwizardAppExtension
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport
import io.github.muliyul.kropwizard.testapp.TestApp
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test


@ExtendWith(DropwizardExtensionsSupport::class)
class IntegrationTest {

	val EXT = DropwizardAppExtension(
		TestApp::class.java,
		ResourceHelpers.resourceFilePath("test-config.yaml"),
		ConfigOverride.randomPorts()
	)

	@Test
	fun `should start without exceptions`() {
	}
}