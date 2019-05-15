package macro.dashboard.neptunes.game

import macro.dashboard.neptunes.Config.Companion.CONFIG
import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.team.TeamTable
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Game(
	val ID: Long,
	val gameType: String,
	val fleetSpeed: Double,
	var isPaused: Boolean,
	var productions: Int,
	val fleetPrice: Int?,
	var tickFragment: Int,
	val tickRate: Int,
	val productionRate: Int,
	val victoryStars: Int,
	var isGameOver: Boolean,
	var isStarted: Boolean,
	var startTime: LocalDateTime,
	val totalStars: Int,
	var productionCounter: Int,
	val isTradeScanned: Boolean,
	var tick: Int,
	val tradeCost: Int,
	val name: String,
	val isTurnBased: Boolean,
	var war: Int,
	var cycleTimeout: LocalDateTime
) {

	fun toMap(): Map<String, Any?> {
		return mapOf(
			"ID" to ID,
			"gameType" to gameType,
			"isPaused" to isPaused,
			"productions" to productions,
			"fleetPrice" to fleetSpeed,
			"tickFragment" to tickFragment,
			"tickRate" to tickRate,
			"productionRate" to productionRate,
			"victoryStars" to victoryStars,
			"isGameOver" to isGameOver,
			"isStarted" to isStarted,
			"startTime" to startTime.format(Util.JAVA_FORMATTER),
			"totalStars" to totalStars,
			"productionCounter" to productionCounter,
			"isTradeScanned" to isTradeScanned,
			"tick" to tick,
			"tradeCost" to tradeCost,
			"name" to name,
			"isTurnBased" to isTurnBased,
			"war" to war,
			"cycleTimeout" to cycleTimeout.format(Util.JAVA_FORMATTER),
			"cycles" to tick / CONFIG.gameCycle,
			"playerCount" to PlayerTable.count(),
			"teamCount" to TeamTable.count()
		).toSortedMap()
	}

	companion object {
		private val LOGGER = LoggerFactory.getLogger(this::class.java)
	}
}