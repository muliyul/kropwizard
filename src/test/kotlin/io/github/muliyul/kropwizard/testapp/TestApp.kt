package io.github.muliyul.kropwizard.testapp

import io.dropwizard.core.Application
import io.dropwizard.core.Configuration
import io.dropwizard.core.setup.Bootstrap
import io.dropwizard.core.setup.Environment
import io.github.muliyul.kropwizard.KropwizardBundle

class TestApp : Application<Configuration>() {
	override fun initialize(bootstrap: Bootstrap<Configuration>) {
		bootstrap.addBundle(KropwizardBundle())
	}

	override fun run(p0: Configuration, p1: Environment) {

	}
}