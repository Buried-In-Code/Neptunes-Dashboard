package macro.neptunes.data.controllers

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.contentType
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
import macro.neptunes.core.team.Team
import macro.neptunes.core.team.TeamHandler
import macro.neptunes.data.Message
import org.slf4j.LoggerFactory
import java.util.stream.Collectors

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object TeamController {
	private val LOGGER = LoggerFactory.getLogger(TeamController::class.java)

	private fun sortTeams(sort: String): List<Team> {
		return when (sort.toLowerCase()) {
			"name" -> TeamHandler.sortByName()
			"stars" -> TeamHandler.sortByStars()
			"ships" -> TeamHandler.sortByShips()
			"economy" -> TeamHandler.sortByEconomy()
			"industry" -> TeamHandler.sortByIndustry()
			"science" -> TeamHandler.sortByScience()
			else -> TeamHandler.teams
		}
	}

	private fun filterTeams(filterString: String, teams: List<Team>): List<Team> {
		val filter: Map<String, String> = filterString.trim()
			.split(",")
			.stream()
			.map { it.split(":") }
			.collect(Collectors.toMap({ it[0].toLowerCase() }, { if (it.size > 1) it[1] else "" }))
		val name: String = filter["name"] ?: ""
		val playerName: String = filter["player-name"] ?: ""
		val playerAlias: String = filter["player-alias"] ?: ""
		return TeamHandler.filter(
			name = name,
			playerName = playerName,
			playerAlias = playerAlias,
			teams = teams
		)
	}

	fun Route.teams() {
		route("/teams") {
			get {
				val sort = call.request.queryParameters["sort"] ?: "name"
				val filter = call.request.queryParameters["filter"] ?: ""
				if (call.request.contentType() == ContentType.Application.Json) {
					val teams = selectTeams(sort = sort, filter = filter)
					call.respond(message = teams)
				} else
					TODO(reason = "Not Yet Implemented")
			}
			post {
				TODO(reason = "Not Yet Implemented")
			}
			get("/leaderboard") {
				val sort = call.request.queryParameters["sort"] ?: "name"
				val filter = call.request.queryParameters["filter"] ?: ""
				if (call.request.contentType() == ContentType.Application.Json) {
					val leaderboard =
						selectLeaderboard(sort = sort, filter = filter)
					call.respond(message = leaderboard)
				} else
					TODO(reason = "Not Yet Implemented")
			}
			get("/{name}") {
				val name = call.parameters["name"] ?: ""
				if (call.request.contentType() == ContentType.Application.Json) {
					val team = selectTeam(name = name)
					call.respond(message = team)
				} else
					TODO(reason = "Not Yet Implemented")
			}
		}
	}

	private fun selectTeam(name: String): Map<String, Any> {
		return TeamHandler.filter(name = name).map { it.longJSON() }.firstOrNull() ?: emptyMap()
	}

	private fun selectTeams(sort: String, filter: String): List<Map<String, Any>> {
		val teams = sortTeams(sort = sort)
		return filterTeams(filterString = filter, teams = teams).map { it.shortJSON() }
	}

	private fun selectLeaderboard(sort: String, filter: String): List<Map<String, Any>> {
		var teams = sortTeams(sort = sort)
		teams = filterTeams(filterString = filter, teams = teams)
		return TeamHandler.getTableData(teams = teams)
	}
}