plugins {
	kotlin("jvm") version "2.2.10"
}

group = "io.github.muliyul.kropwizard"
version = "1.0-SNAPSHOT"

repositories {
	mavenCentral()
}

dependencies {
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.kotlinx.coroutines.slf4j)
	implementation(libs.bytebuddy)

	implementation(platform(libs.guicey.bom))
	implementation(libs.guicey.core)
	implementation(libs.dropwizard.auth)
	implementation(libs.jackson.kotlin)

	api(libs.openFeature)
	api(libs.swagger.core)

	testImplementation(libs.dropwizard.testing)
	testImplementation(libs.mockk)
	testImplementation(kotlin("test"))
}

tasks.test {
	useJUnitPlatform()
}
kotlin {
	jvmToolchain(21)
}