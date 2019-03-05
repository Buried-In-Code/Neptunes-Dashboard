package macro.dashboard.neptunes.team

import macro.dashboard.neptunes.GeneralException
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.Player
import macro.dashboard.neptunes.player.PlayerTable

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Team(
	val ID: Int,
	val gameID: Long,
	var name: String
) : Comparable<Team> {

	fun getGame(): Game = GameTable.select(ID = gameID) ?: throw GeneralException()
	fun getPlayers(): List<Player> = PlayerTable.searchByTeam(teamID = ID)

	/*fun getTotalEconomy() = getPlayers().sumBy { it.economy }
	fun getTotalIndustry() = getPlayers().sumBy { it.industry }
	fun getTotalScience() = getPlayers().sumBy { it.science }
	fun getTotalStars() = getPlayers().sumBy { it.stars }
	fun getTotalFleet() = getPlayers().sumBy { it.fleet }
	fun getTotalShips() = getPlayers().sumBy { it.ships }*/

	override fun compareTo(other: Team): Int {
		return byName.compare(this, other)
	}

	fun toOutput(showGame: Boolean, showPlayers: Boolean): Map<String, Any> {
		val output = mapOf(
			"name" to name,
			"game" to gameID,
			"players" to getPlayers().map { it.ID }
			/*"totalEconomy" to getTotalEconomy(),
			"totalIndustry" to getTotalIndustry(),
			"totalScience" to getTotalScience(),
			"totalStars" to getTotalStars(),
			"totalFleet" to getTotalFleet(),
			"totalShips" to getTotalShips()*/
		).toMutableMap()
		if (showGame)
			output["game"] = getGame().toOutput()
		if (showPlayers)
			output["players"] = getPlayers().map { it.toOutput(showGame = false, showTeam = false, showTurns = true) }
		return output.toSortedMap()
	}

	companion object {
		internal val byName = compareBy(String.CASE_INSENSITIVE_ORDER, Team::name)
	}
}
