package macro.neptunes.core.game

import macro.neptunes.core.Util
import macro.neptunes.core.player.PlayerHandler
import macro.neptunes.core.team.TeamHandler
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Game(
	val fleetSpeed: Double,
	val isPaused: Boolean,
	val productions: Int,
	val tickRate: Int,
	val productionRate: Int,
	val victoryStars: Int,
	val isGameOver: Boolean,
	val isStarted: Boolean,
	val startTime: LocalDateTime,
	val totalStars: Int,
	val productionCounter: Int,
	val tick: Int,
	val tradeCost: Int,
	val name: String,
	val isTurnBased: Boolean
) {
	fun toJson(): Map<String, Any?> {
		val data = mapOf(
			"fleetSpeed" to fleetSpeed,
			"isPaused" to isPaused,
			"productions" to productions,
			"tickRate" to tickRate,
			"productionRate" to productionRate,
			"victoryStars" to victoryStars,
			"isGameOver" to isGameOver,
			"isStarted" to isStarted,
			"startTime" to startTime.format(Util.JAVA_FORMATTER),
			"totalStars" to totalStars,
			"productionCounter" to productionCounter,
			"tick" to tick,
			"tradeCost" to tradeCost,
			"name" to name,
			"isTurnBased" to isTurnBased,
			"playerCount" to PlayerHandler.players.size,
			"teamCount" to TeamHandler.teams.size
		).toSortedMap()
		return data
	}
}