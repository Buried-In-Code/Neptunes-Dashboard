package macro.neptunes.core

import java.time.LocalDateTime

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Game(
	val ID: Long,
	val name: String,
	val startTime: LocalDateTime,
	val totalStars: Int,
	val victoryStars: Int,
	val productions: Int,
	var lastUpdated: LocalDateTime,
	val admin: Int? = null,
	val fleetSpeed: Double? = null,
	val isGameOver: Boolean? = null,
	val isPaused: Boolean? = null,
	val isStarted: Boolean? = null,
	val isTurnBased: Boolean? = null,
	val productionCounter: Int? = null,
	val productionRate: Int? = null,
	val tick: Int? = null,
	val tickFragment: Int? = null,
	val tickRate: Int? = null,
	val tradeCost: Int? = null,
	val tradeScanned: Int? = null,
	val turnBasedTimeout: Int? = null,
	val war: Int? = null
) : Comparable<Game> {

	override fun compareTo(other: Game): Int {
		return byTime.then(byName).compare(this, other)
	}

	companion object {
		internal val byTime = compareBy(Game::startTime)
		internal val byName = compareBy(String.CASE_INSENSITIVE_ORDER, Game::name)
	}
}