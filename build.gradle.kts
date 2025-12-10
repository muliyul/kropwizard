plugins {
	alias(libs.plugins.kotlin.jvm)
	`java-library`
	`maven-publish`
}

group = "io.github.muliyul"
version = "0.2"

repositories {
	mavenCentral()
}

dependencies {
	implementation(libs.kotlinx.coroutines.core)
	implementation(libs.kotlinx.coroutines.slf4j)
	implementation(libs.bytebuddy)

	implementation(platform(libs.dropwizard.bom))
	implementation(libs.dropwizard.core)
	implementation(libs.dropwizard.auth)
	implementation(libs.jackson.kotlin)

	implementation(libs.swagger.core)
	api(libs.swagger.annotations)

	testImplementation(libs.dropwizard.testing)
	testImplementation(libs.mockk)
	testImplementation(kotlin("test"))
}

tasks.test {
	useJUnitPlatform()
}

kotlin {
	jvmToolchain(JavaVersion.VERSION_21.ordinal)
}

publishing {
	repositories {
		repositories {
			maven {
				name = "GitHubPackages"
				url = uri("https://maven.pkg.github.com/muliyul/kropwizard")
				credentials {
					username = System.getenv("GITHUB_ACTOR")
					password = System.getenv("GITHUB_TOKEN")
				}
			}
		}
	}
	publications {
		register<MavenPublication>("gpr") {
			from(components["java"])
		}
	}
}