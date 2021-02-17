package macro.dashboard.v2.schemas

import macro.dashboard.Utils
import macro.dashboard.v2.ISchema
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 * Created by Macro303 on 2019-Mar-04.
 */
class Turn(id: EntityID<Long>) : LongEntity(id), ISchema {
	var turn by TurnTable.turnCol
	var stars by TurnTable.starsCol
	var carriers by TurnTable.carriersCol
	var ships by TurnTable.shipsCol
	var isActive by TurnTable.isActiveCol
	// Infrastructure
	var economy by TurnTable.economyCol
	var industry by TurnTable.industryCol
	var science by TurnTable.scienceCol
	// Technology
	var scanning by TurnTable.scanningCol
	var hyperspaceRange by TurnTable.hyperspaceRangeCol
	var terraforming by TurnTable.terraformingCol
	var experimentation by TurnTable.experimentationCol
	var weapons by TurnTable.weaponsCol
	var banking by TurnTable.bankingCol
	var manufacturing by TurnTable.manufacturingCol

	var player by Player referencedOn TurnTable.playerCol

	private fun calcEconomy(): Double = economy * 10.0 + banking * 75.0

	private fun calcIndustry(): Double = industry * (manufacturing + 5.0) / 2.0

	private fun calcScience(): Double = science * 1.0

	override fun toJson(vararg filterKeys: String): SortedMap<String, Any?> = mapOf<String, Any?>(
		"turn" to turn,
		"stars" to stars,
		"carriers" to carriers,
		"ships" to ships,
		"isActive" to isActive,
		"infrastructure" to mapOf<String, Any?>(
			"economy" to economy,
			"economyPerHr" to calcEconomy(),
			"industry" to industry,
			"industryPerHr" to calcIndustry(),
			"science" to science,
			"sciencePerHr" to calcScience()
		).filterKeys { !filterKeys.contains(it) }.toSortedMap(),
		"technology" to mapOf(
			"scanning" to scanning,
			"hyperspaceRange" to hyperspaceRange,
			"terraforming" to terraforming,
			"experimentation" to experimentation,
			"weapons" to weapons,
			"banking" to banking,
			"manufacturing" to manufacturing
		).filterKeys { !filterKeys.contains(it) }.toSortedMap()
	).filterKeys { !filterKeys.contains(it) }.toSortedMap()

	companion object : LongEntityClass<Turn>(TurnTable) {
		private val LOGGER = LogManager.getLogger()
	}
}

object TurnTable : LongIdTable(name = "Turns") {
	val playerCol = reference(
		name = "player_id",
		foreign = PlayerTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	val turnCol = long("turn")
	val starsCol = integer("stars")
	val carriersCol = integer("carriers")
	val shipsCol = integer("ships")
	val isActiveCol = bool("isActive")
	val economyCol = integer("economy")
	val industryCol = integer("industry")
	val scienceCol = integer("science")
	val scanningCol = integer("scanning_tech")
	val hyperspaceRangeCol = integer("hyperspace_range_tech")
	val terraformingCol = integer("terraforming_tech")
	val experimentationCol = integer("experimentation_tech")
	val weaponsCol = integer("weapons_tech")
	val bankingCol = integer("banking_tech")
	val manufacturingCol = integer("manufacturing_tech")

	private val LOGGER = LogManager.getLogger()

	init {
		if (!exists())
			transaction(db = Utils.DATABASE) {
				uniqueIndex(playerCol, turnCol)
				SchemaUtils.create(this@TurnTable)
			}
	}
}