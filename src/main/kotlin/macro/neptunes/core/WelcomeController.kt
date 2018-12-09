package macro.neptunes.core

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.request.contentType
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import macro.neptunes.data.Message
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object WelcomeController {
	private val LOGGER = LogManager.getLogger(WelcomeController::class.java)

	fun Route.welcome() {
		get("/") {
			if (call.request.contentType() == ContentType.Application.Json)
				call.respond(message = Message(title = "Welcome", content = "Welcome to BIT 269's Neptune's Pride API"))
		}
	}
}