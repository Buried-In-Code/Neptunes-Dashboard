package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.GeneralException
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.team.Team
import macro.dashboard.neptunes.team.TeamTable

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Player(
	val ID: Int,
	val gameID: Long,
	var teamID: Int,
	val alias: String,
	var name: String? = null
) : Comparable<Player> {

	fun getGame(): Game = GameTable.select(ID = gameID) ?: throw GeneralException()
	fun getTeam(): Team = TeamTable.select(ID = teamID) ?: throw GeneralException()

	override fun compareTo(other: Player): Int {
		return byTeam.then(byAlias).compare(this, other)
	}

	fun toOutput(showParent: Boolean = false, showChildren: Boolean = true): Map<String, Any?> {
		var output = mapOf(
			"ID" to ID,
			"alias" to alias,
			"name" to name
		)
		output = when (showParent) {
			true -> output.plus("team" to getTeam().toOutput(showChildren = false))
			false -> output.plus("team" to teamID)
		}
		if (showChildren)
			output = output.plus("turns" to PlayerTurnTable.searchByPlayer(playerID = ID).map { it.toOutput() })
		return output.toSortedMap()
	}

	companion object {
		internal val byTeam = compareBy(Player::getTeam)
		internal val byAlias = compareBy(String.CASE_INSENSITIVE_ORDER, Player::alias)
	}
}
