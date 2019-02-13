package macro.neptunes.game

import macro.neptunes.team.Team
import macro.neptunes.Util
import macro.neptunes.team.TeamTable
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
	var productions: Int,
	var lastUpdated: LocalDateTime,
	val admin: Int,
	val fleetSpeed: Double,
	var isGameOver: Boolean,
	var isPaused: Boolean,
	var isStarted: Boolean,
	val isTurnBased: Boolean,
	val productionCounter: Int,
	val productionRate: Int,
	var tick: Int,
	val tickFragment: Int,
	val tickRate: Int,
	val tradeCost: Int,
	val tradeScanned: Int,
	val turnBasedTimeout: Int,
	val war: Int
) : Comparable<Game> {
	fun getTeams(): List<Team> = TeamTable.search()

	override fun compareTo(other: Game): Int {
		return byTime.then(byName).compare(this, other)
	}

	fun toOutput(showParent: Boolean = false, showChildren: Boolean = true): Map<String, Any> {
		var output = mapOf(
			"ID" to ID,
			"name" to name,
			"startTime" to startTime.format(Util.JAVA_FORMATTER),
			"totalStars" to totalStars,
			"victoryStars" to victoryStars,
			"productions" to productions,
			"lastUpdated" to lastUpdated.format(Util.JAVA_FORMATTER),
			"isGameOver" to isGameOver,
			"isPaused" to isPaused,
			"isStarted" to isStarted,
			"productionRate" to productionRate,
			"tick" to tick
		)
		if (showChildren) {
			output = output.plus("teams" to getTeams().map { it.toOutput(showParent = false) })
		}
		return output.toSortedMap()
	}

	companion object {
		internal val byTime = compareBy(Game::startTime)
		internal val byName = compareBy(String.CASE_INSENSITIVE_ORDER, Game::name)
	}
}