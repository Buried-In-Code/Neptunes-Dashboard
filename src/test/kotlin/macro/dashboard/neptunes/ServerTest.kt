package macro.dashboard.neptunes

import io.ktor.application.Application
import io.ktor.http.HttpMethod
import io.ktor.server.testing.handleRequest
import io.ktor.server.testing.withTestApplication
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Last Updated by Macro303 on 2019-May-06
 */
class ServerTest {
	@Test
	fun testRequests() = withTestApplication(Application::module) {
		with(handleRequest(HttpMethod.Get, "/api")) {
			assertEquals(null, response.status())
		}
	}
}