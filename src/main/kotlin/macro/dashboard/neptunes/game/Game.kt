package macro.dashboard.neptunes.game

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
	companion object {
		private val LOGGER = LoggerFactory.getLogger(this::class.java)
	}
}