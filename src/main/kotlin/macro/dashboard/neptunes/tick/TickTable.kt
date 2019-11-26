package macro.dashboard.neptunes.tick

import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.PlayerTable
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*

/**
 * Created by Macro303 on 2019-Mar-04.
 */
internal object TickTable : Table(name = "Tick") {
	val gameCol = reference(
		name = "gameId",
		foreign = GameTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	val playerCol = reference(
		name = "playerId",
		refColumn = PlayerTable.aliasCol,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	val tickCol = integer(name = "tick")

	val economyCol = integer(name = "economy")
	val industryCol = integer(name = "industry")
	val scienceCol = integer(name = "science")
	val starsCol = integer(name = "stars")
	val fleetsCol = integer(name = "fleets")
	val shipsCol = integer(name = "ships")
	val isActiveCol = bool(name = "isActive")
	val scanningCol = integer(name = "scanning")
	val propulsionCol = integer(name = "propulsion")
	val terraformingCol = integer(name = "terraforming")
	val researchCol = integer(name = "research")
	val weaponsCol = integer(name = "weapons")
	val bankingCol = integer(name = "banking")
	val manufacturingCol = integer(name = "manufacturing")

	private val LOGGER = LogManager.getLogger(TickTable::class.java)

	init {
		Util.query(description = "Create Tick table") {
			uniqueIndex(gameCol, playerCol, tickCol)
			SchemaUtils.create(this)
		}
	}

	fun select(gameId: Long, playerId: String, tick: Int): Tick? =
		Util.query(description = "Select Tick by GameId: $gameId, PlayerId: $playerId, Tick: $tick") {
			select {
				gameCol eq gameId and (playerCol eq playerId) and (tickCol eq tick)
			}.limit(1).firstOrNull()?.parse()
		}

	fun search(gameId: Long, playerId: String): List<Tick> =
		Util.query(description = "Search Cycles in Game: $gameId, from Player: $playerId") {
			select {
				gameCol eq gameId and (playerCol eq playerId)
			}.map { it.parse() }
		}

	fun insert(item: Tick): Boolean = Util.query(description = "Insert Tick") {
		try {
			insert {
				it[gameCol] = EntityID(item.gameId, GameTable)
				it[playerCol] = item.playerId
				it[tickCol] = item.tick
				it[economyCol] = item.economy
				it[industryCol] = item.industry
				it[scienceCol] = item.science
				it[starsCol] = item.stars
				it[fleetsCol] = item.fleets
				it[shipsCol] = item.ships
				it[isActiveCol] = item.isActive
				it[scanningCol] = item.scanning
				it[propulsionCol] = item.propulsion
				it[terraformingCol] = item.terraforming
				it[researchCol] = item.research
				it[weaponsCol] = item.weapons
				it[bankingCol] = item.banking
				it[manufacturingCol] = item.manufacturing
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun update(item: Tick): Boolean = Util.query(description = "Update Tick") {
		try {
			update({ gameCol eq item.gameId and (playerCol eq item.playerId) and (tickCol eq item.tick) }) {
				it[economyCol] = item.economy
				it[industryCol] = item.industry
				it[scienceCol] = item.science
				it[starsCol] = item.stars
				it[fleetsCol] = item.fleets
				it[shipsCol] = item.ships
				it[isActiveCol] = item.isActive
				it[scanningCol] = item.scanning
				it[propulsionCol] = item.propulsion
				it[terraformingCol] = item.terraforming
				it[researchCol] = item.research
				it[weaponsCol] = item.weapons
				it[bankingCol] = item.banking
				it[manufacturingCol] = item.manufacturing
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun delete(item: Tick): Boolean = Util.query(description = "Delete Tick") {
		try {
			deleteWhere { gameCol eq item.gameId and (playerCol eq item.playerId) and (tickCol eq item.tick) }
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	private fun ResultRow.parse(): Tick = Tick(
		gameId = this[gameCol].value,
		playerId = this[playerCol],
		tick = this[tickCol],
		economy = this[economyCol],
		industry = this[industryCol],
		science = this[scienceCol],
		stars = this[starsCol],
		fleets = this[fleetsCol],
		ships = this[shipsCol],
		isActive = this[isActiveCol],
		scanning = this[scanningCol],
		propulsion = this[propulsionCol],
		terraforming = this[terraformingCol],
		research = this[researchCol],
		weapons = this[weaponsCol],
		banking = this[bankingCol],
		manufacturing = this[manufacturingCol]
	)
}