package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.GeneralException
import macro.dashboard.neptunes.technology.Technology
import macro.dashboard.neptunes.technology.TechnologyTable

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
) : Comparable<Turn> {

	fun getPlayer() = PlayerTable.select(ID = playerID) ?: throw GeneralException()
	fun getTechnology() = TechnologyTable.searchByTurn(turnID = ID)

	fun getScanning(): Technology = getTechnology().first { it.name == "scanning" }
	fun getHyperspace(): Technology = getTechnology().first { it.name == "propulsion" }
	fun getTerraforming(): Technology = getTechnology().first { it.name == "terraforming" }
	fun getExperimentation(): Technology = getTechnology().first { it.name == "research" }
	fun getWeapons(): Technology = getTechnology().first { it.name == "weapons" }
	fun getBanking(): Technology = getTechnology().first { it.name == "banking" }
	fun getManufacturing(): Technology = getTechnology().first { it.name == "manufacturing" }

	fun getEconomyTurn(): Double = economy * 10 + getBanking().value * 75
	fun getIndustryTurn(): Double = industry * (getManufacturing().value + 5) / 24

	override fun compareTo(other: Turn): Int {
		return byPlayer.then(byTick).compare(this, other)
	}

	fun toOutput(): Map<String, Any?> {
		val output = mapOf(
			"tick" to tick,
			"player" to playerID,
			"economy" to economy,
			"industry" to industry,
			"science" to science,
			"stars" to stars,
			"fleet" to fleet,
			"ships" to ships,
			"isActive" to isActive,
			"economyTurn" to getEconomyTurn(),
			"industryTurn" to getIndustryTurn(),
			"tech" to mapOf(
				"scanning" to getScanning().level,
				"hyperspace" to getHyperspace().level,
				"terraforming" to getTerraforming().level,
				"experimentation" to getExperimentation().level,
				"weapons" to getWeapons().level,
				"banking" to getBanking().level,
				"manufacturing" to getManufacturing().level
			)
		).toMutableMap()
		return output.toSortedMap()
	}

	companion object {
		internal val byPlayer = compareBy(Turn::getPlayer)
		internal val byTick = compareBy(Turn::tick)
	}
}