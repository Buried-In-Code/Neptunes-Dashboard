package macro.dashboard.neptunes.game

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import macro.dashboard.neptunes.Config
import macro.dashboard.neptunes.backend.Proteus
import org.slf4j.LoggerFactory

/**
 * Last Updated by Macro303 on 2019-May-10
 */
object GameRouter {
	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	internal fun getGame(route: Route) {
		route.get {
			val game = GameTable.select()
			call.respond(
				message = game,
				status = HttpStatusCode.OK
			)
		}
	}

	internal fun updateGame(route: Route) {
		route.put {
			Proteus.getGame(gameID = Config.CONFIG.gameID, code = Config.CONFIG.gameCode)
			call.respond(
				message = emptyMap<Any?, Any?>(),
				status = HttpStatusCode.NoContent
			)
		}
	}
}