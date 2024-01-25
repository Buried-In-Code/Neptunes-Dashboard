package github.buriedincode.dashboard.services.triton

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class ScanData(
    val fleets: Map<String, Fleet>,
    val fleetSpeed: Double,
    @JsonNames("paused")
    val isPaused: Boolean,
    val productions: Int,
    val tickFragment: Int,
    val now: Long,
    val tickRate: Int,
    val productionRate: Int,
    val stars: Map<String, Star>,
    val starsForVictory: Int,
    private val gameOver: Int,
    @JsonNames("started")
    val isStarted: Boolean,
    @JsonNames("start_time")
    private val start: Long,
    val totalStars: Int,
    val productionCounter: Int,
    private val tradeScanned: Int,
    val tick: Int,
    val tradeCost: Int,
    val name: String,
    val playerUid: Int,
    val admin: Int,
    private val turnBased: Int,
    val war: Int,
    val players: Map<String, Player>,
    val turnBasedTimeOut: Long,
) {
    val isGameOver: Boolean
        get() = this.gameOver != 0
    val isTradeScanOnly: Boolean
        get() = this.tradeScanned == 0
    val isTurnBased: Boolean
        get() = this.turnBased != 0
    val startTime: LocalDateTime
        get() = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.start), ZoneId.of("Pacific/Auckland"))
    val turn: Long
        get() = (this.tick / this.tickRate).toLong()
    val nextTurn: LocalDateTime
        get() = LocalDateTime.ofInstant(Instant.ofEpochMilli(this.turnBasedTimeOut), ZoneId.of("Pacific/Auckland"))
}
