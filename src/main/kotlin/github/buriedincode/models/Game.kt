package github.buriedincode.models

import github.buriedincode.Utils.titlecase
import github.buriedincode.Utils.transaction
import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

class Game(id: EntityID<Long>) : LongEntity(id), IJson, Comparable<Game> {
  companion object : LongEntityClass<Game>(GameTable) {
    @JvmStatic private val LOGGER = KotlinLogging.logger {}
    val comparator = compareBy(Game::startTime)
  }

  var apiKey: String by GameTable.apiKeyCol
  var tickRate: Int by GameTable.tickRateCol
  var name: String by GameTable.nameCol
  var type: GameType by GameTable.typeCol
  var starsForVictory: Int by GameTable.starsForVictoryCol
  var totalStars: Int by GameTable.totalStarsCol
  var tradeCost: Int by GameTable.tradeCostCol
  var isTradeScanOnly: Boolean by GameTable.isTradeScanOnlyCol
  var isTurnBased: Boolean by GameTable.isTurnBasedCol

  var isPaused: Boolean by GameTable.isPausedCol
  var isGameOver: Boolean by GameTable.isGameOverCol
  var isStarted: Boolean by GameTable.isStartedCol
  var startTime: LocalDateTime by GameTable.startTimeCol
  var turn: Long by GameTable.turnCol
  var nextTurn: LocalDateTime by GameTable.nextTurnCol

  val players by Player referrersOn PlayerTable.gameCol

  override fun toJson(showAll: Boolean): Map<String, Any?> {
    val output =
      mutableMapOf<String, Any?>(
        "id" to id.value,
        "tickRate" to tickRate,
        "name" to name,
        "type" to type.titlecase(),
        "starsForVictory" to starsForVictory,
        "totalStars" to totalStars,
        "tradeCost" to tradeCost,
        "isTradeScanOnly" to isTradeScanOnly,
        "isTurnBased" to isTurnBased,
        "isPaused" to isPaused,
        "isGameOver" to isGameOver,
        "isStarted" to isStarted,
        "startTime" to startTime.format(LocalDateTime.Formats.ISO),
        "turn" to turn,
        "nextTurn" to nextTurn.format(LocalDateTime.Formats.ISO),
      )
    if (showAll) {
      output["players"] = players.map { it.toJson() }
    }
    return output.toSortedMap()
  }

  override fun compareTo(other: Game): Int = comparator.compare(this, other)
}

object GameTable : LongIdTable(name = "games") {
  @JvmStatic private val LOGGER = KotlinLogging.logger {}

  val apiKeyCol: Column<String> = text(name = "api_key")
  val tickRateCol: Column<Int> = integer(name = "tick_rate")
  val nameCol: Column<String> = text(name = "name")
  val typeCol: Column<GameType> =
    enumerationByName(name = "type", length = 16, klass = GameType::class).default(defaultValue = GameType.TRITON)
  val starsForVictoryCol: Column<Int> = integer(name = "stars_for_victory")
  val totalStarsCol: Column<Int> = integer(name = "total_stars")
  val tradeCostCol: Column<Int> = integer(name = "trade_cost")
  val isTradeScanOnlyCol: Column<Boolean> = bool(name = "is_trade_scan_only")
  val isTurnBasedCol: Column<Boolean> = bool(name = "is_turn_based")
  val isPausedCol: Column<Boolean> = bool(name = "is_paused")
  val isGameOverCol: Column<Boolean> = bool(name = "is_game_over")
  val isStartedCol: Column<Boolean> = bool(name = "is_started")
  val startTimeCol: Column<LocalDateTime> = datetime(name = "start_time")
  val turnCol: Column<Long> = long(name = "turn")
  val nextTurnCol: Column<LocalDateTime> = datetime(name = "next_turn")

  init {
    transaction { SchemaUtils.create(this) }
  }
}
