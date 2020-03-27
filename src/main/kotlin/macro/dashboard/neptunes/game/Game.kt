package macro.dashboard.neptunes.game

import macro.dashboard.neptunes.ISendable
import macro.dashboard.neptunes.player.Player
import macro.dashboard.neptunes.player.PlayerTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.format.DateTimeFormatter

/**
 * Created by Macro303 on 2018-Nov-08.
 */
class Game(id: EntityID<Long>) : LongEntity(id), ISendable, Comparable<Game> {
	companion object : LongEntityClass<Game>(GameTable)

	var code by GameTable.codeCol
	var fleetSpeed by GameTable.fleetSpeedCol
	var isPaused by GameTable.isPausedCol
	var productions by GameTable.productionsCol
	var tickFragment by GameTable.tickFragmentCol
	var tickRate by GameTable.tickRateCol
	var productionRate by GameTable.productionRateCol
	var victoryStars by GameTable.victoryStarsCol
	var isGameOver by GameTable.isGameOverCol
	var isStarted by GameTable.isStartedCol
	var startTime by GameTable.startTimeCol
	var totalStars by GameTable.totalStarsCol
	var productionCounter by GameTable.productionCounterCol
	var isTradeScanned by GameTable.isTradeScannedCol
	var tick by GameTable.tickCol
	var tradeCost by GameTable.tradeCostCol
	var name by GameTable.nameCol
	var isTurnBased by GameTable.isTurnBasedCol
	var war by GameTable.warCol
	var turnTimeout by GameTable.turnTimeoutCol
	var fleetPrice by GameTable.fleetPriceCol
	var gameType by GameTable.gameTypeCol

	val players by Player referrersOn PlayerTable.gameCol

	override fun toJson(full: Boolean): Map<String, Any?> {
		val output = mutableMapOf<String, Any?>(
			"id" to id.value,
			"fleetSpeed" to fleetSpeed,
			"isPaused" to isPaused,
			"productions" to productions,
			"tickFragment" to tickFragment,
			"tickRate" to tickRate,
			"productionRate" to productionRate,
			"victoryStars" to victoryStars,
			"isGameOver" to isGameOver,
			"isStarted" to isStarted,
			"startTime" to startTime.format(DateTimeFormatter.ISO_DATE_TIME),
			"totalStars" to totalStars,
			"productionCounter" to productionCounter,
			"isTradeScanned" to isTradeScanned,
			"tick" to tick,
			"tradeCost" to tradeCost,
			"name" to name,
			"isTurnBased" to isTurnBased,
			"war" to war,
			"turnTimeout" to turnTimeout.format(DateTimeFormatter.ISO_DATE_TIME),
			"fleetPrice" to fleetPrice,
			"gameType" to gameType
		)
		if (full) {
			output["players"] = players.map { it.toJson(full = false) }
		}
		return output.toSortedMap()
	}

	override fun compareTo(other: Game): Int =
		compareByDescending<Game> { it.startTime }
			.thenBy { it.name }
			.thenBy { it.id }
			.compare(this, other)
}