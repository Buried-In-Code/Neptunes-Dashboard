package macro.neptunes.core.team

import io.javalin.Context
import macro.neptunes.core.Util
import macro.neptunes.data.Exceptions
import java.util.stream.Collectors

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object TeamController {
	private const val SORT_NAME = "name"
	private const val SORT_STARS = "stars"
	private const val SORT_SHIPS = "ships"

	private fun getTeams(sort: String): List<Team>? {
		return when (sort) {
			SORT_NAME -> TeamHandler.sortByName()
			SORT_STARS -> TeamHandler.sortByStars()
			SORT_SHIPS -> TeamHandler.sortByShips()
			else -> null
		}
	}

	private fun filterTeams(context: Context, sort: String): List<Team>? {
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
	}

	object Leaderboard {
		fun webGet(context: Context) {
			val sort: String = context.queryParam("sort", default = "name")!!.toLowerCase()
			val teams = filterTeams(context = context, sort = sort)
				?: return Exceptions.invalidParam(context = context, param = sort)
			val output = TeamHandler.getTableData(teams = teams)
			var result = "<table style=\"width:100%; padding: 5px\"><tr>"
			if (output.isNotEmpty()) {
				output.first().forEach {
					result += "<th>${it.key}</th>"
				}
			} else {
				val temp = TeamHandler.getTableData()
				temp.first().forEach {
					result += "<th>${it.key}</th>"
				}
			}
			result += "</tr>"
			output.forEach { team ->
				result += "<tr>"
				team.forEach { key, value ->
					result += "<td${if (key == "Name") "" else " align=\"right\""}>$value</td>"
				}
				result += "</tr>"
			}
			result += "</table>"
			context.html(Util.addHTML(result, "Team Leaderboard"))
		}

		fun apiGet(context: Context) {
			val sort: String = context.queryParam("sort", default = "name")!!.toLowerCase()
			val teams = filterTeams(context = context, sort = sort)
				?: return Exceptions.invalidParam(context = context, param = sort)
			val output = TeamHandler.getTableData(teams = teams)
			context.json(output)
		}
	}
}