package macro.neptunes.core.game

import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.ContentType
import io.ktor.request.contentType
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.route
import org.slf4j.LoggerFactory
import macro.neptunes.core.Config.Companion.CONFIG

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object GameController {
	private val LOGGER = LoggerFactory.getLogger(GameController::class.java)

	fun Route.game() {
		route("/game") {
			get {
				if (call.request.contentType() == ContentType.Application.Json)
					call.respond(message = GameHandler.game)
				else
					call.respond(
						message = FreeMarkerContent(
							template = "game.ftl",
							model = mapOf("game" to GameHandler.game.toJson().plus("enabledTeams" to CONFIG.enableTeams))
						)
					)
			}
			get("/{field}") {
				val field = call.parameters["field"]
				val result = GameHandler.game.toJson()[field]
				call.respond(message = mapOf(field to result))
			}
		}
	}
}