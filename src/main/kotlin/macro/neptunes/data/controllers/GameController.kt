package macro.neptunes.data.controllers

import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.contentType
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import macro.neptunes.core.Util
import macro.neptunes.core.game.GameHandler
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
				call.respond(
					message = FreeMarkerContent(
						template = "message.ftl",
						model = mapOf("message" to Util.getNotImplementedMessage(endpoint = "/game"))
					), status = HttpStatusCode.NotImplemented
				)
		}
	}
}