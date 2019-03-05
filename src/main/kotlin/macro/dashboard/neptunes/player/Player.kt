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
	fun getTurns(): List<PlayerTurn> = PlayerTurnTable.searchByPlayer(playerID = ID)

	override fun compareTo(other: Player): Int {
		return byTeam.then(byAlias).compare(this, other)
	}

	fun toOutput(showGame: Boolean, showTeam: Boolean, showTurns: Boolean): Map<String, Any?> {
		val output = mapOf(
			"ID" to ID,
			"alias" to alias,
			"name" to name,
			"game" to gameID,
			"team" to teamID
		).toMutableMap()
		if (showGame)
			output["game"] = getGame().toOutput()
		if (showTeam)
			output["team"] = getTeam().toOutput(showGame = false, showPlayers = false)
		if (showTurns)
			output["turns"] = getTurns().map { it.toOutput(showPlayer = false) }
		return output.toSortedMap()
	}

	companion object {
		internal val byTeam = compareBy(Player::getTeam)
		internal val byAlias = compareBy(String.CASE_INSENSITIVE_ORDER, Player::alias)
	}
}
