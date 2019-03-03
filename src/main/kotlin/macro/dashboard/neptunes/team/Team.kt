package macro.dashboard.neptunes.team

import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.Player
import macro.dashboard.neptunes.player.PlayerTable

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Team(
	val gameID: Long,
	var name: String
) : Comparable<Team> {
	fun getPlayers(): List<Player> = PlayerTable.search(gameID = gameID, team = this)

	fun getTotalEconomy() = getPlayers().sumBy { it.economy }
	fun getTotalIndustry() = getPlayers().sumBy { it.industry }
	fun getTotalScience() = getPlayers().sumBy { it.science }
	fun getTotalStars() = getPlayers().sumBy { it.stars }
	fun getTotalFleet() = getPlayers().sumBy { it.fleet }
	fun getTotalShips() = getPlayers().sumBy { it.ships }

	fun getGame(): Game = GameTable.select(ID = gameID)!!

	override fun compareTo(other: Team): Int {
		return byName.compare(this, other)
	}

	fun toOutput(showParent: Boolean = false, showChildren: Boolean = true): Map<String, Any> {
		var output = mapOf(
			"name" to name,
			"totalEconomy" to getTotalEconomy(),
			"totalIndustry" to getTotalIndustry(),
			"totalScience" to getTotalScience(),
			"totalStars" to getTotalStars(),
			"totalFleet" to getTotalFleet(),
			"totalShips" to getTotalShips()
		)
		output = when (showParent) {
			true -> output.plus("game" to getGame().toOutput(showChildren = false))
			false -> output.plus("game" to gameID)
		}
		if (showChildren)
			output = output.plus(
				"players" to getPlayers().map { it.toOutput(showParent = false) }
			)
		return output.toSortedMap()
	}

	companion object {
		internal val byName = compareBy(String.CASE_INSENSITIVE_ORDER, Team::name)
	}
}
