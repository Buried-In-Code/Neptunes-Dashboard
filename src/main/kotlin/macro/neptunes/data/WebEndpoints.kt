package macro.neptunes.data

import io.javalin.Context
import macro.neptunes.core.Config
import macro.neptunes.core.Util
import macro.neptunes.data.Sorting.*

/**
 * Created by Macro303 on 2018-Nov-14.
 */
internal object WebEndpoints {

	internal object Game {
		internal fun get(context: Context) {
			val details = Util.game.toString()
			context.result(details)
		}
	}

	internal object Player {
		internal fun get(context: Context) {
			val name: String = context.queryParam("name", default = "")!!
			val alias: String = context.queryParam("alias", default = "")!!
			val sorting: String = context.queryParam("sort", default = "name")!!.toUpperCase()
			val sort: Sorting
			try {
				sort = Sorting.valueOf(sorting)
			} catch (iae: IllegalArgumentException) {
				Exceptions.invalidParam(context = context, param = sorting.toLowerCase())
				return
			}
			val players = when (sort) {
				STARS -> Util.sortPlayersByStars(players = Util.players)
				NAME -> Util.sortPlayersByName(players = Util.players)
				ALIAS -> Util.sortPlayersByAlias(players = Util.players)
				TEAM -> Util.sortPlayersByTeam(players = Util.players)
				SHIPS -> Util.sortPlayersByShips(players = Util.players)
			}
			val details = when {
				name == "" && alias != "" -> players.filter { it.alias.contains(alias, ignoreCase = true) }.map { it.toString() }
				alias == "" && name != "" -> players.filter { it.name.contains(name, ignoreCase = true) }.map { it.toString() }
				else -> players.filter { it.name.contains(name, ignoreCase = true) }.filter { it.alias.contains(alias, ignoreCase = true) }.map { it.toString() }
			}
			context.result(details.joinToString("\n"))
		}

		internal fun getAll(context: Context) {
			val sorting: String = context.queryParam("sort", default = "name")!!.toUpperCase()
			val sort: Sorting
			try {
				sort = Sorting.valueOf(sorting)
			} catch (iae: IllegalArgumentException) {
				Exceptions.invalidParam(context = context, param = sorting.toLowerCase())
				return
			}
			val players = when (sort) {
				STARS -> Util.sortPlayersByStars(players = Util.players)
				NAME -> Util.sortPlayersByName(players = Util.players)
				ALIAS -> Util.sortPlayersByAlias(players = Util.players)
				TEAM -> Util.sortPlayersByTeam(players = Util.players)
				SHIPS -> Util.sortPlayersByShips(players = Util.players)
			}
			val details: List<String> = if (Config.enableTeams)
				players.map { "${it.playerName()} = ${it.team} = ${it.calcComplete()}%" }
			else
				players.map { "${it.playerName()} = ${it.calcComplete()}%" }
			context.result(details.joinToString("\n"))
		}
	}

	internal object Leaderboard {
		internal fun get(context: Context) {
			val headers: List<String> = if (Config.enableTeams)
				listOf("Player", "Team", "Stars", "Ships", "Economy", "$/Turn", "Industry", "Ships/Turn", "Science", "Banking", "Experimentation", "Hyperspace", "Manufacturing", "Scanning", "Terraforming", "Weapons", "Active")
			else
				listOf("Player", "Stars", "Ships", "Economy", "$/Turn", "Industry", "Ships/Turn", "Science", "Banking", "Experimentation", "Hyperspace", "Manufacturing", "Scanning", "Terraforming", "Weapons", "Active")
			val data = ArrayList<List<Any>>()
			val sorting: String = context.queryParam("sort", default = "name")!!.toUpperCase()
			val sort: Sorting
			try {
				sort = Sorting.valueOf(sorting)
			} catch (iae: IllegalArgumentException) {
				Exceptions.invalidParam(context = context, param = sorting.toLowerCase())
				return
			}
			val players = when (sort) {
				STARS -> Util.sortPlayersByStars(players = Util.players)
				NAME -> Util.sortPlayersByName(players = Util.players)
				ALIAS -> Util.sortPlayersByAlias(players = Util.players)
				TEAM -> Util.sortPlayersByTeam(players = Util.players)
				SHIPS -> Util.sortPlayersByShips(players = Util.players)
			}
			if (Config.enableTeams)
				players.forEach {
					data.add(listOf(it.playerName(), it.team, "${it.stars} (${it.calcComplete()}%)", it.strength, it.economy, it.calcMoney(), it.industry, it.calcShips(), it.science, it.banking, it.experimentation, it.hyperspace, it.manufacturing, it.scanning, it.terraforming, it.weapons, it.isActive))
				}
			else
				players.forEach {
					data.add(listOf(it.playerName(), "${it.stars} (${it.calcComplete()}%)", it.strength, it.economy, it.calcMoney(), it.industry, it.calcShips(), it.science, it.banking, it.experimentation, it.hyperspace, it.manufacturing, it.scanning, it.terraforming, it.weapons, it.isActive))
				}
			var result: String = "<html><table style=\"width:100%; padding: 5px\"><tr>"
			headers.forEach {
				result += "<th>$it</th>"
			}
			result += "</tr>"
			data.forEach { player ->
				result += "<tr>"
				player.forEachIndexed { index, value ->
					result += "<td${if (index == 0) "" else " align=\"right\""}>$value</td>"
				}
				result += "</tr>"
			}
			result += "</table></html>"
			context.html(result)
		}
	}

	internal object Help {
		internal fun get(context: Context) {
			Exceptions.notYetAvailable(context = context)
		}
	}
}