package macro.neptunes.data

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import macro.neptunes.core.Util
import macro.neptunes.core.player.Player
import macro.neptunes.core.player.PlayerHandler
import org.apache.logging.log4j.LogManager
import java.util.stream.Collectors

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object PlayerController {
	private val LOGGER = LogManager.getLogger(PlayerController::class.java)

	private fun sortPlayers(sortString: String): List<Player> {
		val field = sortString.split(":")[0]
		val ascending = sortString.split(":").getOrNull(1)?.toLowerCase() == "ascending"
		val players = when (field.toLowerCase()) {
			"name" -> PlayerHandler.sortByName()
			"alias" -> PlayerHandler.sortByAlias()
			"team" -> PlayerHandler.sortByTeams()
			"stars" -> PlayerHandler.sortByStars()
			"ships" -> PlayerHandler.sortByShips()
			"economy" -> PlayerHandler.sortByEconomy()
			"industry" -> PlayerHandler.sortByIndustry()
			"science" -> PlayerHandler.sortByScience()
			else -> PlayerHandler.players
		}
		if (ascending)
			return players.reversed()
		return players
	}

	private fun filterPlayers(filterString: String, players: List<Player>): List<Player> {
		val filter: Map<String, String> = filterString.trim()
			.split(",")
			.stream()
			.map { it.split(":") }
			.collect(Collectors.toMap({ it[0].toLowerCase() }, { if (it.size > 1) it[1] else "" }))
		val name: String = filter["name"] ?: ""
		val alias: String = filter["alias"] ?: ""
		val team: String = filter["team"] ?: ""
		return PlayerHandler.filter(
			name = name,
			alias = alias,
			team = team,
			players = players
		)
	}

	fun Route.playerRoutes() {
		route(path = "/players") {
			contentType(contentType = ContentType.Application.Json) {
				get {
					call.respond(
						message = PlayerHandler.players,
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
						val alias = call.parameters["Alias"] ?: "Unknown"
						val player =
							PlayerHandler.players.sorted().firstOrNull { it.alias.equals(alias, ignoreCase = true) }
						if (player == null)
							call.respond(
								message = Message(
									title = "No Player Found",
									content = "No Player was found with the Alias: $alias"
								),
								status = HttpStatusCode.NotFound
							)
						else
							call.respond(
								message = player,
								status = HttpStatusCode.OK
							)
					}
					put {
						call.respond(
							message = Util.notImplementedMessage(request = call.request),
							status = HttpStatusCode.NotImplemented
						)
					}
					delete {
						call.respond(
							message = Util.notImplementedMessage(request = call.request),
							status = HttpStatusCode.NotImplemented
						)
					}
				}
				get(path = "/leaderboard") {
					call.respond(
						message = PlayerHandler.players,
						status = HttpStatusCode.OK
					)
				}
			}
		}
	}
}