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
				if (call.request.contentType() == ContentType.Application.Json) {
					val players =
						selectPlayers(sort = sort, filter = filter)
					call.respond(message = players)
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
					val leaderboard =
						selectLeaderboard(sort = sort, filter = filter)
					call.respond(message = leaderboard)
				} else
					call.respond(status = HttpStatusCode.NotImplemented, message = Message("Not Yet Implemented"))
			}
			get("/{alias}") {
				val alias = call.parameters["alias"] ?: ""
				if (call.request.contentType() == ContentType.Application.Json) {
					val player = selectPlayer(alias = alias)
					call.respond(message = player)
				} else
					call.respond(status = HttpStatusCode.NotImplemented, message = Message("Not Yet Implemented"))

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
		players =
				filterPlayers(filterString = filter, players = players)
		return PlayerHandler.getTableData(players = players)
	}

	/*fun webGet(context: Context) {
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
			var result = "<table class=\"table table-dark table-striped table-bordered\"><tr>"
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
			context.html(Util.addHTML("<div style=\"padding: 10px\">$result</div>", "Player Leaderboard"))
		}*/
}