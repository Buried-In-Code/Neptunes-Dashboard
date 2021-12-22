package macro.dashboard.v2.schemas

import macro.dashboard.Utils
import macro.dashboard.v2.ISchema
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Created by Macro303 on 2018-Nov-08.
 */
class Game(id: EntityID<Long>) : LongEntity(id), ISchema {
	// Static Columns
	var apiCode by GameTable.apiCodeCol
	var tickRate by GameTable.tickRateCol
	var title by GameTable.titleCol
	var type by GameTable.typeCol
	var victoryStars by GameTable.victoryStarsCol
	var totalStars by GameTable.totalStarsCol
	var tradeCost by GameTable.tradeCostCol
	var isTradeScanOnly by GameTable.isTradeScanOnlyCol
	var isTurnBased by GameTable.isTurnBasedCol

	// Dynamic Columns
	var isPaused by GameTable.isPausedCol
	var isGameOver by GameTable.isGameOverCol
	var isStarted by GameTable.isStartedCol
	var startTime by GameTable.startTimeCol
	var turn by GameTable.turnCol
	var nextTurn by GameTable.nextTurnCol

	// Referenced Columns
	val players by Player referrersOn PlayerTable.gameCol

	override fun toJson(vararg filterKeys: String): SortedMap<String, Any?> {
		val output = mapOf<String, Any?>(
			"id" to id.value,
			"title" to title,
			"type" to type,
			"victoryStars" to victoryStars,
			"totalStars" to totalStars,
			"tradeCost" to tradeCost,
			"isTradeScanOnly" to isTradeScanOnly,
			"isTurnBased" to isTurnBased,
			"isPaused" to isPaused,
			"isGameOver" to isGameOver,
			"isStarted" to isStarted,
			"startTime" to startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
			"turn" to turn,
			"nextTurn" to nextTurn.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
		).filterKeys { !filterKeys.contains(it) }.toSortedMap()
		if (filterKeys.contains("players").not())
			output["players"] = players.map { it.toJson(*filterKeys, "game") }
		return output
	}

	companion object : LongEntityClass<Game>(GameTable) {
		private val LOGGER = LogManager.getLogger()
	}
}

object GameTable : LongIdTable(name = "Games") {
	// Static Columns
	val apiCodeCol = text("api_code")
	val tickRateCol = integer("tick_rate")
	val titleCol = text("title")
	val typeCol = customEnumeration("type", "TEXT", Utils::toType, Utils::fromType)
	val victoryStarsCol = integer("victory_stars")
	val totalStarsCol = integer("total_stars")
	val tradeCostCol = integer("trade_cost")
	val isTradeScanOnlyCol = bool("is_trade_scan_only")
	val isTurnBasedCol = bool("is_turn_based")

	// Dynamic Columns
	val isPausedCol = bool("is_paused")
	val isGameOverCol = bool("is_game_over")
	val isStartedCol = bool("is_started")
	val startTimeCol = datetime("start_time").default(LocalDateTime.now())
	val turnCol = long("turn")
	val nextTurnCol = datetime("next_turn").default(LocalDateTime.now())

	private val LOGGER = LogManager.getLogger()

	init {
		if (!exists())
			transaction(db = Utils.DATABASE) {
				SchemaUtils.create(this@GameTable)
			}
	}
}