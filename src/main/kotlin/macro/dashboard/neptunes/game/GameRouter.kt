package macro.dashboard.neptunes.game

import io.ktor.application.call
import io.ktor.features.NotFoundException
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.util.KtorExperimentalAPI
import macro.dashboard.neptunes.Triton
import macro.dashboard.neptunes.config.Config.Companion.CONFIG
import org.apache.logging.log4j.LogManager

/**
 * Last Updated by Macro303 on 2019-May-10
 */
object GameRouter {
	private val LOGGER = LogManager.getLogger(GameRouter::class.java)

	@KtorExperimentalAPI
	private fun getGame(): Game {
		CONFIG.game.gameId ?: throw NotFoundException()
		return GameTable.select(gameId = CONFIG.game.gameId!!) ?: throw NotFoundException()
	}

	@KtorExperimentalAPI
	fun routing(route: Route) {
		route.get {
			call.respond(
				message = getGame().toJson(),
				status = HttpStatusCode.OK
			)
		}
		route.put {
			CONFIG.game.code ?: throw NotFoundException()
			Triton.getGame(gameId = getGame().ID, code = CONFIG.game.code!!)
			call.respond(
				message = getGame().toJson(),
				status = HttpStatusCode.OK
			)
		}
	}
}