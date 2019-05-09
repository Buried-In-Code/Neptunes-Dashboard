package macro.dashboard.neptunes.game

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import macro.dashboard.neptunes.BadRequestException
import macro.dashboard.neptunes.Config.Companion.CONFIG
import macro.dashboard.neptunes.backend.Proteus
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object GameController {
	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	fun Route.gameRoutes() {
		route(path = "/game") {
			get {
				call.respond(
					message = GameTable.select().toOutput(),
					status = HttpStatusCode.OK
				)
			}
			put {
				val game = GameTable.select()
				when (game.gameType) {
					"Proteus" -> Proteus.getGame(gameID = CONFIG.gameID, code = CONFIG.gameCode)
					else -> throw BadRequestException(message = "Invalid Game Type => ${game.gameType} `${game.ID}`")
				}
				call.respond(
					message = GameTable.select().toOutput(),
					status = HttpStatusCode.OK
				)
			}
		}
	}
}