package github.buriedincode.dashboard.models

import github.buriedincode.dashboard.Utils.titlecase
import github.buriedincode.dashboard.Utils.toString
import github.buriedincode.dashboard.tables.GameTable
import github.buriedincode.dashboard.tables.PlayerTable
import org.apache.logging.log4j.kotlin.Logging
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.time.LocalDateTime

class Game(id: EntityID<Long>) : LongEntity(id), IJson, Comparable<Game> {
    companion object : LongEntityClass<Game>(GameTable), Logging {
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
        val output = mutableMapOf<String, Any?>(
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
            "startTime" to startTime.toString("yyyy-MM-dd HH:mm:ss"),
            "turn" to turn,
            "nextTurn" to nextTurn.toString("yyyy-MM-dd HH:mm:ss"),
        )
        if (showAll) {
            output["players"] = players.map { it.toJson() }
        }
        return output.toSortedMap()
    }

    override fun compareTo(other: Game): Int = comparator.compare(this, other)
}
