package io.github.muliyul.kropwizard.testapp

import io.dropwizard.core.Application
import io.dropwizard.core.Configuration
import io.dropwizard.core.setup.Bootstrap
import io.dropwizard.core.setup.Environment
import io.github.muliyul.kropwizard.KropwizardBundle

class TestApp : Application<TestConfig>() {
	override fun initialize(bootstrap: Bootstrap<TestConfig>) {
		bootstrap.addBundle(KropwizardBundle())
	}

	override fun run(p0: TestConfig, p1: Environment) {

	}
}

data class TestConfig(
	val name: String = "test"
) : Configuration()