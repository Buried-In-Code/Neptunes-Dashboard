package macro.neptunes.data

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import macro.neptunes.core.Util
import macro.neptunes.core.player.Player
import macro.neptunes.core.player.PlayerHandler
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object PlayerController {
	private val LOGGER = LogManager.getLogger(PlayerController::class.java)

	fun getPlayers(): List<Map<String, Any?>> {
		val players = PlayerHandler.players.sortedBy { it.alias }
		return players.map { it.toJson() }
	}

	suspend fun ApplicationCall.parsePlayer(): Player? {
		val alias = parameters["Alias"]
		if (alias == null) {
			respond(
				message = Util.notFoundMessage(type = "Player", field = "Alias", value = alias),
				status = HttpStatusCode.NotFound
			)
			return null
		}
		val player = PlayerHandler.players.sortedBy { it.alias }.firstOrNull {
			it.alias.equals(alias, ignoreCase = true)
		}
		if (player == null) {
			respond(
				message = Util.notFoundMessage(type = "Player", field = "Alias", value = alias),
				status = HttpStatusCode.NotFound
			)
			return null
		}
		return player
	}

	fun Route.playerRoutes() {
		route(path = "/players") {
			contentType(contentType = ContentType.Application.Json) {
				get {
					call.respond(
						message = getPlayers(),
						status = HttpStatusCode.OK
					)
				}
				post {
					call.respond(
						message = Util.notImplementedMessage(request = call.request),
						status = HttpStatusCode.NotImplemented
					)
				}
				route(path = "/{Alias}") {
					get {
						val player = call.parsePlayer() ?: return@get
						call.respond(
							message = player,
							status = HttpStatusCode.OK
						)
					}
					put {
						val player = call.parsePlayer() ?: return@put
						call.respond(
							message = Util.notImplementedMessage(request = call.request),
							status = HttpStatusCode.NotImplemented
						)
					}
					delete {
						val player = call.parsePlayer() ?: return@delete
						call.respond(
							message = Util.notImplementedMessage(request = call.request),
							status = HttpStatusCode.NotImplemented
						)
					}
				}
			}
		}
	}
}