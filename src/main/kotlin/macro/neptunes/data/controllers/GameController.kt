package macro.neptunes.data.controllers

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.contentType
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import macro.neptunes.core.game.GameHandler
import macro.neptunes.data.Message
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object GameController {
	private val LOGGER = LoggerFactory.getLogger(GameController::class.java)

	fun Route.game() {
		get("/game") {
			if (call.request.contentType() == ContentType.Application.Json)
				call.respond(GameHandler.game)
			else
				call.respond(status = HttpStatusCode.NotImplemented, message = Message("Not Yet Implemented"))
		}
	}
}