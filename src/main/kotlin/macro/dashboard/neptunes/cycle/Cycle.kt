package macro.dashboard.neptunes.cycle

import io.javalin.http.InternalServerErrorResponse
import macro.dashboard.neptunes.Config.Companion.CONFIG
import macro.dashboard.neptunes.player.Player
import macro.dashboard.neptunes.player.PlayerTable
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2019-Mar-04.
 */
data class Cycle(
	val ID: Int,
	val playerID: Int,
	val cycle: Int,
	var economy: Int,
	var industry: Int,
	var science: Int,
	var stars: Int,
	var fleet: Int,
	var ships: Int,
	var isActive: Boolean,
	var scanning: Int,
	var hyperspace: Int,
	var experimentation: Int,
	var weapons: Int,
	var banking: Int,
	var manufacturing: Int
) {
	val player: Player by lazy {
		PlayerTable.select(ID = playerID) ?: throw InternalServerErrorResponse("Unable to Find Player => $playerID")
	}

	val economyPerCycle by lazy {
		economy * banking * CONFIG.gameCycle
	}
	val industryPerCycle by lazy {
		industry * manufacturing * CONFIG.gameCycle
	}
	val sciencePerCycle by lazy {
		science * experimentation * CONFIG.gameCycle
	}

	fun toOutput(): Map<String, Any?> {
		val output = mapOf<String, Any?>(
			"cycle" to cycle,
			"player" to playerID,
			"economy" to economy,
			"industry" to industry,
			"science" to science,
			"stars" to stars,
			"fleet" to fleet,
			"ships" to ships,
			"isActive" to isActive,
			"economyPerCycle" to economyPerCycle,
			"industryPerCycle" to industryPerCycle,
			"sciencePerCycle" to sciencePerCycle,
			"tech" to mapOf(
				"scanning" to scanning,
				"hyperspace" to hyperspace,
				"experimentation" to experimentation,
				"weapons" to weapons,
				"banking" to banking,
				"manufacturing" to manufacturing
			).toSortedMap()
		).toMutableMap()
		return output.toSortedMap()
	}

	companion object {
		private val LOGGER = LoggerFactory.getLogger(this::class.java)
	}
}