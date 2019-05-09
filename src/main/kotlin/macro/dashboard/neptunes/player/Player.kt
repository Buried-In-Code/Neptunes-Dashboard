package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.GeneralException
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.team.Team
import macro.dashboard.neptunes.team.TeamTable
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Player(
	val ID: Int,
	val gameID: Long,
	var teamID: Int,
	val alias: String,
	var name: String? = null
) {
	val game: Game by lazy {
		GameTable.select()
	}
	val team: Team by lazy {
		TeamTable.select(ID = teamID) ?: throw GeneralException()
	}
	val cycles: List<Cycle> by lazy {
		CycleTable.searchByPlayer(playerID = ID)
	}
	val latestCycle: Cycle by lazy {
		CycleTable.selectLatest(playerID = ID) ?: throw GeneralException()
	}

	fun toOutput(showGame: Boolean, showTeam: Boolean, showCycles: Boolean = true): Map<String, Any?> {
		val output = mapOf(
			"ID" to ID,
			"alias" to alias,
			"name" to name,
			"game" to gameID,
			"team" to team.name,
			"cycles" to latestCycle.toOutput()
		).toMutableMap()
		if (showGame)
			output["game"] = game.toOutput()
		if (showTeam)
			output["team"] = team.toOutput(showGame = false, showPlayers = false)
		if (showCycles)
			output["cycles"] = cycles.map { it.toOutput() }
		return output.toSortedMap()
	}

	companion object {
		private val LOGGER = LoggerFactory.getLogger(this::class.java)
	}
}