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
	fun toOutput(): Map<String, Any?> {
		val output = mapOf(
			"ID" to ID,
			"gameType" to gameType,
			"name" to name,
			"totalStars" to totalStars,
			"victoryStars" to victoryStars,
			"productionRate" to productionRate,
			"startTime" to startTime.format(Util.JAVA_FORMATTER),
			"isGameOver" to isGameOver,
			"isPaused" to isPaused,
			"isStarted" to isStarted,
			"cycles" to tick / CONFIG.gameCycle,
			"cycleTimeout" to cycleTimeout.format(Util.JAVA_FORMATTER),
			"playerCount" to PlayerTable.search().size,
			"teamCount" to TeamTable.search().filterNot { it.players.isEmpty() }.size
		).toMutableMap()
		return output.toSortedMap()
	}

	companion object {
		private val LOGGER = LoggerFactory.getLogger(this::class.java)
	}
}