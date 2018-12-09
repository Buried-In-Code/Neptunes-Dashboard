package macro.neptunes.core.team

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
import macro.neptunes.core.game.GameHandler
import macro.neptunes.data.Message
import org.apache.logging.log4j.LogManager
import java.util.stream.Collectors

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object TeamController {
	private val LOGGER = LogManager.getLogger(TeamController::class.java)

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

	@Suppress("UNCHECKED_CAST")
	fun Route.teams() {
		route("/teams") {
			get {
				val sort = call.request.queryParameters["sort"] ?: "name"
				val filter = call.request.queryParameters["filter"] ?: ""
				val teams = selectTeams(sort = sort, filter = filter)
				when {
					call.request.contentType() == ContentType.Application.Json -> call.respond(message = teams.map { it.toJson() })
					teams.isNotEmpty() -> call.respond(
						message = FreeMarkerContent(
							template = "team-list.ftl",
							model = mapOf("teams" to teams.map { it.toJson() })
						)
					)
					else -> call.respond(
						message = FreeMarkerContent(
							template = "message.ftl",
							model = mapOf(
								"message" to Message(
									title = "No Teams Found",
									content = "No teams were found, either Teams have been disabled in the config or you need to check your setup"
								)
							)
						), status = HttpStatusCode.NotFound
					)
				}
			}
			post {
				if (call.request.contentType() == ContentType.Application.Json)
					call.respond(
						message = Util.getNotImplementedMessage(endpoint = "/teams"),
						status = HttpStatusCode.NotImplemented
					)
				else
					call.respond(
						message = FreeMarkerContent(
							template = "message.ftl",
							model = mapOf("message" to Util.getNotImplementedMessage(endpoint = "/teams"))
						), status = HttpStatusCode.NotImplemented
					)
			}
			get("/leaderboard") {
				val sort = call.request.queryParameters["sort"] ?: "name"
				val filter = call.request.queryParameters["filter"] ?: ""
				val leaderboard = selectTeams(sort = sort, filter = filter)
				when {
					call.request.contentType() == ContentType.Application.Json -> call.respond(message = leaderboard.map { it.toJson() })
					leaderboard.isNotEmpty() -> call.respond(
						message = FreeMarkerContent(
							template = "team-leaderboard.ftl",
							model = mapOf("leaderboard" to leaderboard.map { it.toJson() })
						)
					)
					else -> call.respond(
						message = FreeMarkerContent(
							template = "message.ftl",
							model = mapOf(
								"message" to Message(
									title = "No Teams Found",
									content = "No teams were found, either Teams have been disabled in the config or you need to check your setup"
								)
							)
						), status = HttpStatusCode.NotFound
					)
				}
			}
			route("/{name}") {
				get {
					val name = call.parameters["name"] ?: ""
					val team = selectTeam(name = name)
					when {
						call.request.contentType() == ContentType.Application.Json -> call.respond(
							message = team?.toJson() ?: emptyMap<String, Any?>()
						)
						team != null -> call.respond(
							message = FreeMarkerContent(
								template = "team.ftl",
								model = mapOf("team" to team.toJson())
							)
						)
						else -> call.respond(
							message = FreeMarkerContent(
								template = "message.ftl",
								model = mapOf(
									"message" to Message(
										title = "Team Not Found",
										content = "No teams were found with the name: $name"
									)
								)
							), status = HttpStatusCode.NotFound
						)
					}
				}
				get("/{field}") {
					val name = call.parameters["name"] ?: ""
					val field = call.parameters["field"]
					val team = selectTeam(name = name)?.toJson() ?: emptyMap()
					call.respond(message = mapOf(field to team[field]))
				}
			}
		}
	}

	private fun selectTeam(name: String): Team? {
		return TeamHandler.filter(name = name).firstOrNull()
	}

	private fun selectTeams(sort: String, filter: String): List<Team> {
		val teams = sortTeams(sort = sort)
		return filterTeams(filterString = filter, teams = teams)
	}
}