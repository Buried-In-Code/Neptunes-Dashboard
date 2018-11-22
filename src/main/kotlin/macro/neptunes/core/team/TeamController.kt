package macro.neptunes.core.team

import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.request.contentType
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.route
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
		return TeamHandler.filter(name = name, playerName = playerName, playerAlias = playerAlias, teams = teams)
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
					call.respond(status = HttpStatusCode.NotImplemented, message = Message("Not Yet Implemented"))
			}
			post {
				call.respond(status = HttpStatusCode.NotImplemented, message = Message("Not Yet Implemented"))
			}
			get("/leaderboard") {
				val sort = call.request.queryParameters["sort"] ?: "name"
				val filter = call.request.queryParameters["filter"] ?: ""
				if (call.request.contentType() == ContentType.Application.Json) {
					val leaderboard = selectLeaderboard(sort = sort, filter = filter)
					call.respond(message = leaderboard)
				} else
					call.respond(status = HttpStatusCode.NotImplemented, message = Message("Not Yet Implemented"))
			}
			get("/{name}") {
				val name = call.parameters["name"] ?: ""
				if (call.request.contentType() == ContentType.Application.Json) {
					val team = selectTeam(name = name)
					call.respond(message = team)
				} else
					call.respond(status = HttpStatusCode.NotImplemented, message = Message("Not Yet Implemented"))

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

	/*private fun filterTeams(context: Context, sort: String): List<Team>? {
		val teams = getTeams(sort = sort) ?: return null
		val filterString = context.queryParam("filter") ?: ""
		val filter: Map<String, String> = filterString.trim()
			.split(",")
			.stream()
			.map { it.split(":") }
			.collect(Collectors.toMap({ it[0].toLowerCase() }, { if (it.size > 1) it[1] else "" }))
		val name: String = filter["name"] ?: ""
		val playerName: String = filter["player-name"] ?: ""
		val playerAlias: String = filter["player-alias"] ?: ""
		return TeamHandler.filter(name = name, playerName = playerName, playerAlias = playerAlias, teams = teams)
	}

	fun webGet(context: Context) {
		val name = context.pathParam("name")
		val team = TeamHandler.filter(name = name)?.map { it.longHTML() }?.firstOrNull() ?: "<h3>No Team Found</h3>"
		val htmlString = Util.addHTML(team, "Get Team")
		context.html(htmlString)
	}

	fun webGetAll(context: Context) {
		val sort: String = context.queryParam("sort", default = "name")!!.toLowerCase()
		var teams = filterTeams(context = context, sort = sort)?.map { it.shortHTML() }
			?: return Exceptions.invalidParam(context = context, param = sort)
		if(teams.isEmpty())
			teams = listOf("<h3>No Teams Found</h3>")
		val htmlString = Util.addHTML(teams.joinToString(""), "Get Teams")
		context.html(htmlString)
	}

	fun apiGet(context: Context) {
		if (context.status() >= 400)
			return
		val name = context.pathParam("name")
		val team = TeamHandler.filter(name = name)?.map { it.longJSON() }?.firstOrNull() ?: emptyMap()
		context.json(team)
	}

	fun apiGetAll(context: Context) {
		if (context.status() >= 400)
			return
		val sort: String = context.queryParam("sort", default = "name")!!.toLowerCase()
		val teams = filterTeams(context = context, sort = sort)?.map { it.shortJSON() }
			?: return Exceptions.invalidParam(context = context, param = sort)
		context.json(teams)
	}

	fun apiPost(context: Context) {
		if (context.status() >= 400)
			return
		Exceptions.notYetAvailable(context = context)
	}*/

	object Leaderboard {
		/*fun webGet(context: Context) {
			val sort: String = context.queryParam("sort", default = "name")!!.toLowerCase()
			val teams = filterTeams(context = context, sort = sort)
				?: return Exceptions.invalidParam(context = context, param = sort)
			val output = TeamHandler.getTableData(teams = teams)
			var result = "<table class=\"table table-dark table-striped table-bordered\"><tr>"
			if (output.isNotEmpty()) {
				output.first().forEach {
					result += "<th >${it.key}</th>"
				}
			} else {
				val temp = TeamHandler.getTableData()
				temp.first().forEach {
					result += "<th >${it.key}</th>"
				}
			}
			result += "</tr>"
			output.forEach { team ->
				result += "<tr>"
				team.forEach { key, value ->
					var temp = value
					when (value) {
						is Int -> temp = Util.INT_FORMAT.format(value)
						is Double -> temp = Util.PERCENT_FORMAT.format(value)
					}
					result += "<td>$temp</td>"
				}
				result += "</tr>"
			}
			result += "</table>"
			context.html(Util.addHTML(bodyHTML = "<div style=\"padding: 10px\">$result</div>", title = "Team Leaderboard"))
		}

		fun apiGet(context: Context) {
			val sort: String = context.queryParam("sort", default = "name")!!.toLowerCase()
			val teams = filterTeams(context = context, sort = sort)
				?: return Exceptions.invalidParam(context = context, param = sort)
			val output = TeamHandler.getTableData(teams = teams)
			context.json(output)
		}*/
	}
}