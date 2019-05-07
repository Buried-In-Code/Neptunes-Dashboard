package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.GeneralException
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2019-Mar-04.
 */
data class Turn(
	val ID: Int,
	val playerID: Int,
	val tick: Int,
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
	val terraforming: Technology? by lazy {
		technology.firstOrNull { it.name == "terraforming" }
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

	val economyPerTurn: Double by lazy {
		economy * 10 + banking.value * 75
	}
	val industryPerTurn: Double by lazy {
		industry * (manufacturing.value + 5) / 2
	}

	fun toOutput(): Map<String, Any?> {
		val output = mapOf<String, Any?>(
			"tick" to tick,
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
			"tech" to mapOf(
				"scanning" to scanning.level,
				"hyperspace" to hyperspace.level,
				"terraforming" to (terraforming?.level ?: 0),
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