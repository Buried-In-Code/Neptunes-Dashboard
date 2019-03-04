package macro.dashboard.neptunes.game

import macro.dashboard.neptunes.Util
import java.time.LocalDateTime

/**
 * Created by Macro303 on 2019-Feb-25.
 */
data class Turn(
	val game: Game,
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
	val turnBasedTimeout: Long
) : Comparable<Turn> {

	override fun compareTo(other: Turn): Int {
		return byGame.thenDescending(byTick).compare(this, other)
	}

	fun toOutput(showParent: Boolean = false, showChildren: Boolean = true): Map<String, Any?>{
		var output = mapOf(
			"startTime" to startTime.format(Util.JAVA_FORMATTER),
			"production" to production,
			"isGameOver" to isGameOver,
			"isPaused" to isPaused,
			"isStarted" to isStarted,
			"tick" to tick
		)
		output = when(showParent) {
			true -> output.plus("game" to game.toOutput(showChildren = false))
			false -> output.plus("game" to game.ID)
		}
		return output.toSortedMap()
	}

	companion object {
		internal val byGame = compareBy(Turn::game)
		internal val byTick = compareBy(Turn::tick)
	}
}