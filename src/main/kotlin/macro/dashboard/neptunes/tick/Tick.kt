package macro.dashboard.neptunes.tick

import io.ktor.features.NotFoundException
import io.ktor.util.KtorExperimentalAPI
import macro.dashboard.neptunes.IEntry
import macro.dashboard.neptunes.ISendable
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.Player
import macro.dashboard.neptunes.player.PlayerTable
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2019-Mar-04.
 */
data class Tick(
	val gameId: Long,
	val playerId: String,
	val tick: Int,
	var economy: Int,
	var industry: Int,
	var science: Int,
	var stars: Int,
	var fleets: Int,
	var ships: Int,
	var isActive: Boolean,
	var scanning: Int,
	var propulsion: Int,
	var terraforming: Int,
	var research: Int,
	var weapons: Int,
	var banking: Int,
	var manufacturing: Int
) : ISendable, IEntry {
	@KtorExperimentalAPI
	override fun toJson(full: Boolean): Map<String, Any?> {
		val output = mutableMapOf<String, Any?>(
			"tick" to tick,
			"stars" to stars,
			"fleets" to fleets,
			"ships" to ships,
			"isActive" to isActive,
			"economy" to economy,
			"economyPerTick" to calcEconomy(),
			"industry" to industry,
			"industryPerTick" to calcIndustry(),
			"science" to science,
			"sciencePerTick" to calcScience(),
			"research" to mapOf<String, Any?>(
				"scanning" to scanning,
				"hyperspaceRange" to propulsion,
				"terraforming" to terraforming,
				"experimentation" to research,
				"weapons" to weapons,
				"banking" to banking,
				"manufacturing" to manufacturing
			).toSortedMap()
		)
		if (full) {
			output["game"] = getGame().toJson(full = false)
			output["player"] = getPlayer().toJson(full = false)
		}
		return output.toSortedMap()
	}

	override fun insert(): Tick {
		TickTable.insert(item = this)
		return this
	}

	override fun update(): Tick {
		TickTable.update(item = this)
		return this
	}

	override fun delete() {
		TickTable.delete(item = this)
	}

	@KtorExperimentalAPI
	fun getGame(): Game = GameTable.select(gameId = gameId) ?: throw NotFoundException()

	@KtorExperimentalAPI
	fun getPlayer(): Player = PlayerTable.select(gameId = gameId, alias = playerId) ?: throw NotFoundException()

	fun calcEconomy(): Double = economy * 10.0 + banking * 75.0

	fun calcIndustry(): Double = industry * (manufacturing + 5.0) / 2.0

	fun calcScience(): Double = science * 1.0

	companion object {
		private val LOGGER = LogManager.getLogger(Tick::class.java)
	}
}