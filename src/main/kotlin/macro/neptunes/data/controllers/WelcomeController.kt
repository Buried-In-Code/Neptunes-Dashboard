package macro.neptunes.data.controllers

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.contentType
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import macro.neptunes.data.Message
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object WelcomeController {
	private val LOGGER = LoggerFactory.getLogger(WelcomeController::class.java)

	fun Route.welcome() {
		get("/") {
			val message = "Welcome to BIT 269's Neptune's Pride API"
			if (call.request.contentType() == ContentType.Application.Json)
				call.respond(Message(message = message))
			else
				call.respond(status = HttpStatusCode.NotImplemented, message = Message("Not Yet Implemented"))
		}
	}
}