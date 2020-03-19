package macro.dashboard.neptunes.tick

import macro.dashboard.neptunes.ISendable
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.player.Player
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

/**
 * Created by Macro303 on 2019-Mar-04.
 */
class Tick(id: EntityID<Long>) : LongEntity(id), ISendable {
	companion object : LongEntityClass<Tick>(TickTable)

	var game by Game referencedOn TickTable.gameCol
	var player by Player referencedOn TickTable.playerCol
	var tick by TickTable.tickCol
	var economy by TickTable.economyCol
	var industry by TickTable.industryCol
	var science by TickTable.scienceCol
	var stars by TickTable.starsCol
	var fleets by TickTable.fleetsCol
	var ships by TickTable.shipsCol
	var isActive by TickTable.isActiveCol
	var scanning by TickTable.scanningCol
	var propulsion by TickTable.propulsionCol
	var terraforming by TickTable.terraformingCol
	var research by TickTable.researchCol
	var weapons by TickTable.weaponsCol
	var banking by TickTable.bankingCol
	var manufacturing by TickTable.manufacturingCol

	private fun calcEconomy(): Double = economy * 10.0 + banking * 75.0

	private fun calcIndustry(): Double = industry * (manufacturing + 5.0) / 2.0

	private fun calcScience(): Double = science * 1.0

	override fun toJson(full: Boolean): Map<String, Any?> {
		val output = mutableMapOf<String, Any?>(
			"id" to id.value,
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
			output["game"] = game.toJson(full = false)
			output["player"] = player.toJson(full = false)
		}
		return output.toSortedMap()
	}
}