package macro.dashboard.neptunes.game

import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.team.TeamTable
import java.time.LocalDateTime

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
	val tradeCost: Int,
	var startTime: LocalDateTime,
	var production: Int,
	var isGameOver: Boolean,
	var isPaused: Boolean,
	var isStarted: Boolean,
	var productionCounter: Int,
	var tick: Int,
	var tickFragment: Int,
	var tradeScanned: Int,
	var war: Int,
	var turnBasedTimeout: Long
) : Comparable<Game> {

	override fun compareTo(other: Game): Int {
		return byStartTime.reversed().compare(this, other)
	}

	fun toOutput(): Map<String, Any?> {
		val output = mapOf(
			"ID" to ID,
			"name" to name,
			"totalStars" to totalStars,
			"victoryStars" to victoryStars,
			"productionRate" to productionRate,
			"startTime" to startTime.format(Util.JAVA_FORMATTER),
			"isGameOver" to isGameOver,
			"isPaused" to isPaused,
			"isStarted" to isStarted,
			"tick" to tick,
			"players" to PlayerTable.search(gameID = ID).size,
			"teams" to TeamTable.search(gameID = ID).size
		)
		return output.toSortedMap()
	}

	companion object {
		val byStartTime = compareBy(Game::startTime)
	}
}