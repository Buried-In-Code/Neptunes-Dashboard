package macro.neptunes.core.player

import io.ktor.application.call
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.contentType
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import macro.neptunes.core.Util
import macro.neptunes.data.Message
import org.slf4j.LoggerFactory
import java.util.stream.Collectors

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object PlayerController {
	private val LOGGER = LoggerFactory.getLogger(PlayerController::class.java)

	private fun sortPlayers(sortString: String): List<Player> {
		val field = sortString.split(":")[0]
		val asc = sortString.split(":").getOrNull(1)?.toLowerCase() == "asc"
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
		if(asc)
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

	fun Route.players() {
		route(path = "/players") {
			get {
				val sort = call.request.queryParameters["sort"] ?: "name"
				val filter = call.request.queryParameters["filter"] ?: ""
				val players = selectPlayers(sort = sort, filter = filter)
				when {
					call.request.contentType() == ContentType.Application.Json -> call.respond(message = players.map { it.toJson() })
					players.isNotEmpty() -> call.respond(
						message = FreeMarkerContent(
							template = "player-list.ftl",
							model = mapOf("players" to players.map { it.toJson() })
						)
					)
					else -> call.respond(
						message = FreeMarkerContent(
							template = "message.ftl",
							model = mapOf(
								"message" to Message(
									title = "No Players Found",
									content = "No players were found, please check your setup"
								)
							)
						), status = HttpStatusCode.NotFound
					)
				}
			}
			post {
				if (call.request.contentType() == ContentType.Application.Json)
					call.respond(
						message = Util.getNotImplementedMessage(endpoint = "/players"),
						status = HttpStatusCode.NotImplemented
					)
				else
					call.respond(
						message = FreeMarkerContent(
							template = "message.ftl",
							model = mapOf("message" to Util.getNotImplementedMessage(endpoint = "/players"))
						), status = HttpStatusCode.NotImplemented
					)
			}
			get(path = "/leaderboard") {
				val sort = call.request.queryParameters["sort"] ?: "name"
				val filter = call.request.queryParameters["filter"] ?: ""
				val leaderboard = selectPlayers(sort = sort, filter = filter)
				when {
					call.request.contentType() == ContentType.Application.Json -> call.respond(message = leaderboard.map { it.toJson() })
					leaderboard.isNotEmpty() -> call.respond(
						message = FreeMarkerContent(
							template = "player-leaderboard.ftl",
							model = mapOf("leaderboard" to leaderboard.map { it.toJson() })
						)
					)
					else -> call.respond(
						message = FreeMarkerContent(
							template = "message.ftl",
							model = mapOf(
								"message" to Message(
									title = "No Players Found",
									content = "No players were found, please check your setup"
								)
							)
						), status = HttpStatusCode.NotFound
					)
				}
			}
			get(path = "/fields") {
				val player = selectPlayer(alias = "")
				call.respond(message = player?.toJson()?.keys ?: emptySet<String>())
			}
			route(path = "/{alias}") {
				get {
					val alias = call.parameters["alias"] ?: ""
					val player = selectPlayer(alias = alias)
					when {
						call.request.contentType() == ContentType.Application.Json -> call.respond(
							message = player?.toJson() ?: emptyMap<String, Any?>()
						)
						player != null -> call.respond(
							message = FreeMarkerContent(
								template = "player.ftl",
								model = mapOf("player" to player.toJson())
							)
						)
						else -> call.respond(
							message = FreeMarkerContent(
								template = "message.ftl",
								model = mapOf(
									"message" to Message(
										title = "Player Not Found",
										content = "No players were found with the alias: $alias"
									)
								)
							), status = HttpStatusCode.NotFound
						)
					}
				}
				route(path = "/technology") {
					get {
						val alias = call.parameters["alias"] ?: ""
						val field = "technology"
						val player = selectPlayer(alias = alias)?.toJson() ?: emptyMap()
						call.respond(message = mapOf(field to player[field]))
					}
					get("/fields") {
						val player = selectPlayer(alias = "")
						call.respond(
							message = (player?.toJson()?.get("technology") as Map<String, Any?>?)?.keys
								?: emptySet<String>()
						)
					}
					get(path = "/{field}") {
						val alias = call.parameters["alias"] ?: ""
						val field = call.parameters["field"]
						val player =
							selectPlayer(alias = alias)?.toJson()?.get("technology") as Map<String, Any?>? ?: emptyMap()
						call.respond(message = mapOf(field to player[field]))
					}
				}
				get(path = "/{field}") {
					val alias = call.parameters["alias"] ?: ""
					val field = call.parameters["field"]
					val player = selectPlayer(alias = alias)?.toJson() ?: emptyMap()
					call.respond(message = mapOf(field to player[field]))
				}
			}
		}
	}

	private fun selectPlayer(alias: String): Player? {
		return PlayerHandler.filter(alias = alias).firstOrNull()
	}

	private fun selectPlayers(sort: String, filter: String): List<Player> {
		val players = sortPlayers(sortString = sort)
		return filterPlayers(filterString = filter, players = players)
	}
}