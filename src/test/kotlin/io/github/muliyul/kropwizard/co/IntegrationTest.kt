package io.github.muliyul.kropwizard.co

import io.dropwizard.core.Application
import io.dropwizard.core.Configuration
import io.dropwizard.core.setup.Bootstrap
import io.dropwizard.core.setup.Environment
import io.github.muliyul.kropwizard.KropwizardBundle
import ru.vyarus.dropwizard.guice.test.ClientSupport
import ru.vyarus.dropwizard.guice.test.jupiter.TestDropwizardApp
import kotlin.test.Test

@TestDropwizardApp(TestApp::class)
class IntegrationTest {
	@Test
	fun appStartup() {
		// If the application starts up without exceptions, the test passes.
	}
}

class TestApp : Application<Configuration>() {
	override fun initialize(bootstrap: Bootstrap<Configuration>) {
		bootstrap.addBundle(KropwizardBundle())
	}

	override fun run(p0: Configuration, p1: Environment) {

	}
}