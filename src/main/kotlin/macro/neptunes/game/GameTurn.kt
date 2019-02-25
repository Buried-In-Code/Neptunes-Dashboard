package macro.neptunes.game

import java.time.LocalDateTime

/**
 * Created by Macro303 on 2019-Feb-25.
 */
data class GameTurn(
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
	var war: Int
) : Comparable<GameTurn> {

	override fun compareTo(other: GameTurn): Int {
		return byGame.then(byTurn).compare(this, other)
	}

	companion object {
		internal val byGame = compareBy(GameTurn::game)
		internal val byTurn = compareBy(GameTurn::tick)
	}
}