package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.GeneralException

/**
 * Created by Macro303 on 2019-Mar-04.
 */
data class PlayerTurn(
	val ID: Int,
	val turnID: Int,
	val playerID: Int,
	var economy: Int,
	var industry: Int,
	var science: Int,
	var stars: Int,
	var fleet: Int,
	var ships: Int,
	var isActive: Boolean
) : Comparable<PlayerTurn> {
	//	fun getTurn() = GameTurnTable.select(ID = turnID) ?: throw GeneralException()
	fun getPlayer() = PlayerTable.select(ID = playerID) ?: throw GeneralException()

	/*private fun getTechnologies(): List<Technology> = TechnologyTable.search(player = this)
	fun getScanning(): Technology = getTechnologies().first { it.name == "Scanning" }
	fun getHyperspace(): Technology = getTechnologies().first { it.name == "Hyperspace" }
	fun getTerraforming(): Technology = getTechnologies().first { it.name == "Terraforming" }
	fun getExperimentation(): Technology = getTechnologies().first { it.name == "Experimentation" }
	fun getWeapons(): Technology = getTechnologies().first { it.name == "Weapons" }
	fun getBanking(): Technology = getTechnologies().first { it.name == "Banking" }
	fun getManufacturing(): Technology = getTechnologies().first { it.name == "Manufacturing" }*/

	fun getEconomyTurn(): Double = /*economy * 10 + getBanking().value * 75*/ 42.0
	fun getIndustryTurn(): Double = /*industry * (getManufacturing().value + 5) / 24*/ 42.0

	override fun compareTo(other: PlayerTurn): Int {
		return byPlayer/*.then(byTurn)*/.compare(this, other)
	}

	fun toOutput(showParent: Boolean = false, showChildren: Boolean = true): Map<String, Any?> {
		var output = mapOf(
			"economy" to economy,
			"industry" to industry,
			"science" to science,
			"stars" to stars,
			"fleet" to fleet,
			"ships" to ships,
			"isActive" to isActive,
			"economyTurn" to getEconomyTurn(),
			"industryTurn" to getIndustryTurn()/*,
			"technologies" to getTechnologies()*/
		)
		output = when (showParent) {
			true -> {
				output.plus(
					mapOf(
						"player" to getPlayer().toOutput(showChildren = false)
//						"turn" to getTurn().toOutput(showChildren = false)
					)
				)
			}
			false -> output.plus(mapOf("player" to getPlayer().ID/*, "turn" to getTurn().ID*/))
		}
		return output.toSortedMap()
	}

	companion object {
		internal val byPlayer = compareBy(PlayerTurn::getPlayer)
//		internal val byTurn = compareBy(PlayerTurn::getTurn)
	}
}