package io.github.muliyul.kropwizard.testapp

import io.dropwizard.auth.Auth
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import java.security.Principal

@Path("/fake")
class FakeResource {
	@GET
	suspend fun suspending(): String = "suspended"

	@POST
	fun echo(input: String): String = input

	@DELETE
	suspend fun delete(@Auth user: Principal) = Unit
}