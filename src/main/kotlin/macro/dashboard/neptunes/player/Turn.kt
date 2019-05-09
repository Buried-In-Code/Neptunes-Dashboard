package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.Config.Companion.CONFIG
import macro.dashboard.neptunes.GeneralException
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2019-Mar-04.
 */
data class Turn(
	val ID: Int,
	val playerID: Int,
	val cycle: Int,
	var economy: Int,
	var industry: Int,
	var science: Int,
	var stars: Int,
	var fleet: Int,
	var ships: Int,
	var isActive: Boolean
) {

	val player: Player by lazy {
		PlayerTable.select(ID = playerID) ?: throw GeneralException()
	}
	val technology: List<Technology> by lazy {
		TechnologyTable.searchByTurn(turnID = ID)
	}

	val scanning: Technology by lazy {
		technology.first { it.name == "scanning" }
	}
	val hyperspace: Technology by lazy {
		technology.first { it.name == "propulsion" }
	}
	val experimentation: Technology by lazy {
		technology.first { it.name == "research" }
	}
	val weapons: Technology by lazy {
		technology.first { it.name == "weapons" }
	}
	val banking: Technology by lazy {
		technology.first { it.name == "banking" }
	}
	val manufacturing: Technology by lazy {
		technology.first { it.name == "manufacturing" }
	}

	val economyPerTurn by lazy {
		economy * banking.level * CONFIG.gameCycle
	}
	val industryPerTurn by lazy {
		industry * manufacturing.level * CONFIG.gameCycle
	}
	val sciencePerTurn by lazy {
		science * experimentation.level * CONFIG.gameCycle
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
			"economyPerTurn" to economyPerTurn,
			"industryPerTurn" to industryPerTurn,
			"sciencePerTurn" to sciencePerTurn,
			"tech" to mapOf(
				"scanning" to scanning.level,
				"hyperspace" to hyperspace.level,
				"experimentation" to experimentation.level,
				"weapons" to weapons.level,
				"banking" to banking.level,
				"manufacturing" to manufacturing.level
			).toSortedMap()
		).toMutableMap()
		return output.toSortedMap()
	}

	companion object {
		private val LOGGER = LoggerFactory.getLogger(this::class.java)
	}
}