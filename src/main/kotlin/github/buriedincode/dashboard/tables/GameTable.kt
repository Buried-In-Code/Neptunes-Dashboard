package github.buriedincode.dashboard.tables

import github.buriedincode.dashboard.Utils
import github.buriedincode.dashboard.models.GameType
import org.apache.logging.log4j.kotlin.Logging
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.LocalDateTime

object GameTable : LongIdTable(name = "games"), Logging {
    val apiKeyCol: Column<String> = text(name = "api_key")
    val tickRateCol: Column<Int> = integer(name = "tick_rate")
    val nameCol: Column<String> = text(name = "name")
    val typeCol: Column<GameType> = enumerationByName(
        name = "type",
        length = 16,
        klass = GameType::class,
    ).default(defaultValue = GameType.TRITON)
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
        Utils.query {
            SchemaUtils.create(this)
        }
    }
}
