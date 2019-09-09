package macro.dashboard.neptunes.game

import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import macro.dashboard.neptunes.Proteus
import macro.dashboard.neptunes.config.Config
import org.slf4j.LoggerFactory

/**
 * Last Updated by Macro303 on 2019-May-10
 */
object GameRouter {
	private val LOGGER = LoggerFactory.getLogger(GameRouter::class.java)

	internal fun getGame(route: Route) {
		route.get {
			val game = GameTable.select()
			call.respond(
				message = game.toMap(),
				status = HttpStatusCode.OK
			)
		}
	}

	internal fun updateGame(route: Route) {
		route.put {
			Proteus.getGame(gameID = Config.CONFIG.game.id, code = Config.CONFIG.game.code)
			call.respond(
				message = emptyMap<Any?, Any?>(),
				status = HttpStatusCode.NoContent
			)
		}
	}
}