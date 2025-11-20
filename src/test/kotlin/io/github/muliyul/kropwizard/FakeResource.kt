package io.github.muliyul.kropwizard

import io.dropwizard.auth.Auth
import jakarta.ws.rs.DELETE
import jakarta.ws.rs.GET
import jakarta.ws.rs.POST
import jakarta.ws.rs.Path
import java.security.Principal

@Path("/fake")
interface FakeResource {
	@GET
	suspend fun suspending(): String

	@POST
	fun blocking(input: String): String

	@DELETE
	suspend fun delete(@Auth user: Principal)
}