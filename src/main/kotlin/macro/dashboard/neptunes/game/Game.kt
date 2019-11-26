package macro.dashboard.neptunes.game

import io.ktor.util.KtorExperimentalAPI
import macro.dashboard.neptunes.IEntry
import macro.dashboard.neptunes.ISendable
import macro.dashboard.neptunes.config.Config.Companion.CONFIG
import macro.dashboard.neptunes.player.Player
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.team.Team
import macro.dashboard.neptunes.team.TeamTable
import org.apache.logging.log4j.LogManager
import java.time.LocalDateTime

/**
 * Created by Macro303 on 2018-Nov-08.
 */
data class Game(
	val ID: Long,
	val fleetSpeed: Double,
	var isPaused: Boolean,
	var productions: Int,
	var tickFragment: Int,
	val tickRate: Int,
	val productionRate: Int,
	val victoryStars: Int,
	var isGameOver: Boolean,
	var isStarted: Boolean,
	var startTime: LocalDateTime,
	val totalStars: Int,
	var productionCounter: Int,
	val isTradeScanned: Boolean,
	var tick: Int,
	val tradeCost: Int,
	val name: String,
	val isTurnBased: Boolean,
	var war: Int,
	var turnTimeout: LocalDateTime,
	val fleetPrice: Int? = null,
	val gameType: String = "Triton"
) : ISendable, IEntry {
	@KtorExperimentalAPI
	override fun toJson(full: Boolean): Map<String, Any?> {
		val output = mutableMapOf<String, Any?>(
			"id" to ID,
			"fleetSpeed" to fleetSpeed,
			"isPaused" to isPaused,
			"productions" to productions,
			"tickFragment" to tickFragment,
			"tickRate" to tickRate,
			"productionRate" to productionRate,
			"victoryStars" to victoryStars,
			"isGameOver" to isGameOver,
			"isStarted" to isStarted,
			"startTime" to startTime,
			"totalStars" to totalStars,
			"productionCounter" to productionCounter,
			"isTradeScanned" to isTradeScanned,
			"tick" to tick,
			"tradeCost" to tradeCost,
			"name" to name,
			"isTurnBased" to isTurnBased,
			"war" to war,
			"turnTimeout" to turnTimeout,
			"fleetPrice" to fleetPrice,
			"gameType" to gameType
		)
		if (full) {
			output["players"] = getPlayers().map { it.toJson(full = false) }
			output["teams"] = getTeams().map { it.toJson(full = false) }
		}
		return output.toSortedMap()
	}

	override fun insert(): Game {
		GameTable.insert(item = this)
		return this
	}

	override fun update(): Game {
		GameTable.update(item = this)
		return this
	}

	override fun delete() {
		GameTable.delete(item = this)
	}

	fun getPlayers(): List<Player> = PlayerTable.search(gameId = ID)

	fun getTeams(): List<Team> = TeamTable.search(gameId = ID)

	companion object {
		private val LOGGER = LogManager.getLogger(Game::class.java)
	}
}