package macro.neptunes.data

import io.javalin.Context
import macro.neptunes.core.Config
import macro.neptunes.core.Util
import macro.neptunes.core.Util.fromJSON
import macro.neptunes.core.player.PlayerHandler
import macro.neptunes.core.team.TeamHandler
import macro.neptunes.data.Sorting.*

/**
 * Created by Macro303 on 2018-Nov-14.
 */
internal object APIEndpoints {

	internal object Game {
		internal fun get(context: Context) {
			context.json(Util.game)
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
				name == "" && alias != "" -> players.filter { it.alias.contains(alias, ignoreCase = true) }
				alias == "" && name != "" -> players.filter { it.name.contains(name, ignoreCase = true) }
				else -> players.filter { it.name.contains(name, ignoreCase = true) }.filter { it.alias.contains(alias, ignoreCase = true) }
			}
			context.json(details)
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
			context.json(players)
		}

		@Suppress("UNCHECKED_CAST")
		internal fun add(context: Context) {
			val temp: List<Pair<String, String>> = context.body().fromJSON().map { Pair(it.value.toString(), it.key) }
			Config.players = Config.players.plus(temp)
			Config.saveConfig()
			Util.players = PlayerHandler.getData()
			if (Config.enableTeams)
				Util.teams = TeamHandler.getData(players = Util.players)
			else
				Util.teams = null
			context.status(204)
		}
	}

	internal object Leaderboard {
		internal fun get(context: Context) {
			Exceptions.notYetAvailable(context = context)
			val sorting = context.queryParam("sort", default = "name")!!.toUpperCase()
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
		}
	}

	internal object Help {
		internal fun get(context: Context) {
			Exceptions.notYetAvailable(context = context)
		}
	}
}