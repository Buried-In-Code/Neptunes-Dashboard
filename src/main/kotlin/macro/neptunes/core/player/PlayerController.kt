package macro.neptunes.core.player

import io.javalin.Context
import macro.neptunes.Application
import macro.neptunes.core.Config
import macro.neptunes.core.Util
import macro.neptunes.core.Util.fromJSON
import macro.neptunes.data.Exceptions
import org.apache.logging.log4j.LogManager
import java.util.stream.Collectors

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object PlayerController {
	private val LOGGER = LogManager.getLogger(PlayerController::class.java)
	private const val SORT_NAME = "name"
	private const val SORT_ALIAS = "alias"
	private const val SORT_TEAMS = "team"
	private const val SORT_STARS = "stars"
	private const val SORT_SHIPS = "ships"
	private const val SORT_ECONOMY = "economy"
	private const val SORT_INDUSTRY = "industry"
	private const val SORT_SCIENCE = "science"

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
		val filterString = context.queryParam("filter") ?: ""
		val filter: Map<String, String> = filterString.trim()
			.split(",")
			.stream()
			.map { it.split(":") }
			.collect(Collectors.toMap({ it[0].toLowerCase() }, { if (it.size > 1) it[1] else "" }))
		val name: String = filter["name"] ?: ""
		val alias: String = filter["alias"] ?: ""
		val team: String = filter["team"] ?: ""
		return PlayerHandler.filter(name = name, alias = alias, team = team, players = players)
	}

	fun webGet(context: Context) {
		val alias = context.pathParam("alias")
		val player = PlayerHandler.filter(alias = alias).map { it.longHTML() }.firstOrNull() ?: "<h3>No Player Found</h3>"
		val htmlString = Util.addHTML(player, "Get Player")
		context.html(htmlString)
	}

	fun webGetAll(context: Context) {
		val sort: String = context.queryParam("sort", default = "name")!!.toLowerCase()
		var players = filterPlayers(context = context, sort = sort)?.map { it.shortHTML() }
			?: return Exceptions.invalidParam(context = context, param = sort)
		if(players.isEmpty())
			players = listOf("<h3>No Players Found</h3>")
		val htmlString = Util.addHTML(players.joinToString(""), "Get Players")
		context.html(htmlString)
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
			var result = "<table style=\"width:100%; padding: 5px\"><tr>"
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
			result += "</table>"
			context.html(Util.addHTML(result, "Player Leaderboard"))
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