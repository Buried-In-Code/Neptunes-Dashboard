package macro.neptunes.core.team

import io.javalin.Context
import macro.neptunes.data.Exceptions

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
		val name: String = context.queryParam("name", default = "")!!
		val playerName: String = context.queryParam("player-name", default = "")!!
		val playerAlias: String = context.queryParam("player-alias", default = "")!!
		return TeamHandler.filter(name = name, playerName = playerName, playerAlias = playerAlias, teams = teams)
	}

	fun webGet(context: Context) {
		val name = context.pathParam("name")
		val team = TeamHandler.filter(name = name)?.map { it.longHTML() }?.firstOrNull() ?: ""
		context.html(team)
	}

	fun webGetAll(context: Context) {
		val sort: String = context.queryParam("sort", default = "name")!!.toLowerCase()
		val teams = filterTeams(context = context, sort = sort)?.map { it.shortHTML() }
			?: return Exceptions.invalidParam(context = context, param = sort)
		context.html(teams.joinToString("<br />"))
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
			var result = "<html><table style=\"width:100%; padding: 5px\"><tr>"
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
			result += "</table></html>"
			context.html(result)
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