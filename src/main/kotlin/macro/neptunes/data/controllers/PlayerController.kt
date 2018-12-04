package macro.neptunes.data.controllers

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
import macro.neptunes.core.player.Player
import macro.neptunes.core.player.PlayerHandler
import macro.neptunes.data.Message
import org.slf4j.LoggerFactory
import java.util.stream.Collectors

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object PlayerController {
	private val LOGGER = LoggerFactory.getLogger(PlayerController::class.java)

	private fun sortPlayers(sort: String): List<Player> {
		return when (sort.toLowerCase()) {
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
		route("/players") {
			get {
				val sort = call.request.queryParameters["sort"] ?: "name"
				val filter = call.request.queryParameters["filter"] ?: ""
				val players = selectPlayers(sort = sort, filter = filter)
				if (call.request.contentType() == ContentType.Application.Json) {
					call.respond(message = players)
				} else
					call.respond(FreeMarkerContent("player-list.ftl", mapOf("players" to players)))
			}
			post {
				TODO(reason = "Not Yet Implemented")
			}
			get("/leaderboard") {
				val sort = call.request.queryParameters["sort"] ?: "name"
				val filter = call.request.queryParameters["filter"] ?: ""
				val leaderboard = selectLeaderboard(sort = sort, filter = filter)
				if (call.request.contentType() == ContentType.Application.Json) {
					call.respond(message = leaderboard)
				} else
					call.respond(FreeMarkerContent("player-leaderboard.ftl", mapOf("leaderboard" to leaderboard)))
			}
			get("/{alias}") {
				val alias = call.parameters["alias"] ?: ""
				val player = selectPlayer(alias = alias)
				if (call.request.contentType() == ContentType.Application.Json) {
					call.respond(message = player)
				} else
					call.respond(FreeMarkerContent("player.ftl", mapOf("player" to player)))

			}
		}
	}

	private fun selectPlayer(alias: String): Map<String, Any> {
		return PlayerHandler.filter(alias = alias).map { it.longJSON() }.firstOrNull() ?: emptyMap()
	}

	private fun selectPlayers(sort: String, filter: String): List<Map<String, Any>> {
		val players = sortPlayers(sort = sort)
		return filterPlayers(filterString = filter, players = players).map { it.shortJSON() }
	}

	private fun selectLeaderboard(sort: String, filter: String): List<Map<String, Any>> {
		var players = sortPlayers(sort = sort)
		players = filterPlayers(filterString = filter, players = players)
		return PlayerHandler.getTableData(players = players)
	}
}