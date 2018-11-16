package macro.neptunes.core.player

import io.javalin.Context
import macro.neptunes.Application
import macro.neptunes.core.Config
import macro.neptunes.core.Util.fromJSON
import macro.neptunes.data.Exceptions

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object PlayerController {
	private const val SORT_NAME = "name"
	private const val SORT_ALIAS = "alias"
	private const val SORT_TEAMS = "teams"
	private const val SORT_STARS = "stars"
	private const val SORT_SHIPS = "ships"

	private fun getPlayers(sort: String): List<Player>? {
		return when (sort) {
			SORT_NAME -> PlayerHandler.sortByName()
			SORT_ALIAS -> PlayerHandler.sortByAlias()
			SORT_TEAMS -> PlayerHandler.sortByTeams()
			SORT_STARS -> PlayerHandler.sortByStars()
			SORT_SHIPS -> PlayerHandler.sortByShips()
			else -> null
		}
	}

	private fun filterPlayers(context: Context, sort: String): List<Player>? {
		val players = getPlayers(sort = sort) ?: return null
		val name: String = context.queryParam("name") ?: ""
		val alias: String = context.queryParam("alias") ?: ""
		val team: String = context.queryParam("team") ?: ""
		return PlayerHandler.filter(name = name, alias = alias, team = team, players = players)
	}

	fun webGet(context: Context) {
		val alias = context.pathParam("alias")
		val player = PlayerHandler.filter(alias = alias).map { it.longHTML() }.firstOrNull() ?: ""
		context.html(player)
	}

	fun webGetAll(context: Context) {
		val sort: String = context.queryParam("sort", default = "name")!!.toLowerCase()
		val players = filterPlayers(context = context, sort = sort)?.map { it.shortHTML() }
			?: return Exceptions.invalidParam(context = context, param = sort)
		context.html(players.joinToString("<br />"))
	}

	fun apiGet(context: Context) {
		if (context.status() >= 400)
			return
		val alias = context.pathParam("alias")
		val player = PlayerHandler.filter(alias = alias).map { it.longJSON() }.firstOrNull() ?: emptyMap()
		context.json(player)
	}

	fun apiGetAll(context: Context) {
		if (context.status() >= 400)
			return
		val sort: String = context.queryParam("sort", default = "name")!!.toLowerCase()
		val players = filterPlayers(context = context, sort = sort)?.map { it.shortJSON() }
			?: return Exceptions.invalidParam(context = context, param = sort)
		context.json(players)
	}

	fun apiPost(context: Context) {
		if (context.status() >= 400)
			return
		val temp: List<Pair<String, String>> = context.body().fromJSON().map { Pair(it.value.toString(), it.key) }
		Config.players = Config.players.plus(temp)
		Config.saveConfig()
		Application.refreshData()
		context.status(204)
	}

	object Leaderboard {
		fun webGet(context: Context) {
			val sort: String = context.queryParam("sort", default = "name")!!.toLowerCase()
			val players = filterPlayers(context = context, sort = sort)
				?: return Exceptions.invalidParam(context = context, param = sort)
			val output = PlayerHandler.getTableData(players = players)
			var result = "<html><table style=\"width:100%; padding: 5px\"><tr>"
			if (output.isNotEmpty()) {
				output.first().forEach {
					result += "<th>${it.key}</th>"
				}
			} else {
				val temp = PlayerHandler.getTableData()
				temp.first().forEach {
					result += "<th>${it.key}</th>"
				}
			}
			result += "</tr>"
			output.forEach { player ->
				result += "<tr>"
				player.forEach { key, value ->
					result += "<td${if (key == "Player" || key == "Team") "" else " align=\"right\""}>$value</td>"
				}
				result += "</tr>"
			}
			result += "</table></html>"
			context.html(result)
		}

		fun apiGet(context: Context) {
			val sort: String = context.queryParam("sort", default = "name")!!.toLowerCase()
			val players = filterPlayers(context = context, sort = sort)
				?: return Exceptions.invalidParam(context = context, param = sort)
			val output = PlayerHandler.getTableData(players = players)
			context.json(output)
		}
	}
}