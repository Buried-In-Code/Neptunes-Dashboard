package macro.dashboard.neptunes.game

import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.team.TeamTable

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Game(
	val ID: Long,
	val name: String,
	val totalStars: Int,
	val victoryStars: Int,
	val admin: Int,
	val fleetSpeed: Double,
	val isTurnBased: Boolean,
	val productionRate: Int,
	val tickRate: Int,
	val tradeCost: Int
) : Comparable<Game> {

	override fun compareTo(other: Game): Int {
		return byID.compare(this, other) * -1
	}

	fun toOutput(showParent: Boolean = false, showChildren: Boolean = true): Map<String, Any?> {
		var output = mapOf(
			"ID" to ID,
			"name" to name,
			"totalStars" to totalStars,
			"victoryStars" to victoryStars,
			"productionRate" to productionRate,
			"players" to PlayerTable.count(game = this),
			"teams" to TeamTable.count(game = this)
		)
		if(showChildren)
			output = output.plus("turns" to TurnTable.search(ID = ID).map { it.toOutput() })
		return output.toSortedMap()
	}

	companion object {
		val byID = compareBy(Game::ID)
	}
}