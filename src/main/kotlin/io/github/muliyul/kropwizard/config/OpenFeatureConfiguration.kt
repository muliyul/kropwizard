package io.github.muliyul.kropwizard.config

import dev.openfeature.kotlin.sdk.FeatureProvider

interface OpenFeatureConfiguration {
	val featureProvider: FeatureProvider
}