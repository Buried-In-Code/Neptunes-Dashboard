package macro.dashboard.neptunes.cycle

import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.backend.ProteusPlayer
import macro.dashboard.neptunes.player.PlayerTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2019-Mar-04.
 */
object CycleTable : IntIdTable(name = "Turn") {
	val playerCol = reference(
		name = "playerID",
		foreign = PlayerTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	val cycleCol = integer(name = "cycle")
	val economyCol = integer(name = "economy").default(0)
	val industryCol = integer(name = "industry").default(0)
	val scienceCol = integer(name = "science").default(0)
	val starsCol = integer(name = "stars")
	val fleetCol = integer(name = "fleet")
	val shipsCol = integer(name = "ships")
	val isActiveCol = bool(name = "isActive")
	val weaponsCol = integer(name = "weapons").default(0)
	val rangeCol = integer(name = "hyperspace").default(0)
	val scanningCol = integer(name = "scanning").default(0)
	val bankingCol = integer(name = "banking").default(0)
	val manufacturingCol = integer(name = "manufacturing").default(0)
	val experimentationCol = integer(name = "experimentation").default(0)

	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	init {
		Util.query(description = "Create Cycle table") {
			uniqueIndex(cycleCol, playerCol)
			SchemaUtils.create(this)
		}
	}

	fun select(ID: Int): Cycle? = Util.query(description = "Select Cycle by ID: $ID") {
		select {
			id eq ID
		}.orderBy(playerCol to SortOrder.ASC, cycleCol to SortOrder.DESC).limit(n = 1).firstOrNull()?.parse()
	}

	fun select(playerID: Int, cycle: Int): Cycle? =
		Util.query(description = "Select Cycle by Player: $playerID and Cycle: $cycle") {
			select {
				playerCol eq playerID and (cycleCol eq cycle)
			}.orderBy(playerCol to SortOrder.ASC, cycleCol to SortOrder.DESC).limit(n = 1).firstOrNull()?.parse()
		}

	fun selectLatest(playerID: Int): Cycle? =
		Util.query(description = "Search for Latest Cycle by Player: $playerID") {
			select {
				playerCol eq playerID
			}.orderBy(playerCol to SortOrder.ASC, cycleCol to SortOrder.DESC).limit(n = 1).firstOrNull()?.parse()
		}

	fun searchByPlayer(playerID: Int): List<Cycle> =
		Util.query(description = "Search for Cycles by Player: $playerID") {
			select {
				playerCol eq playerID
			}.orderBy(playerCol to SortOrder.ASC, cycleCol to SortOrder.DESC).map {
				it.parse()
			}
		}

	fun insert(playerID: Int, cycle: Int, update: ProteusPlayer): Boolean =
		Util.query(description = "Insert Proteus Cycle") {
			try {
				insert {
					it[playerCol] = EntityID(id = playerID, table = PlayerTable)
					it[cycleCol] = cycle
					it[economyCol] = update.economy
					it[industryCol] = update.industry
					it[scienceCol] = update.science
					it[starsCol] = update.stars
					it[fleetCol] = update.fleet
					it[shipsCol] = update.ships
					it[isActiveCol] = update.conceded == 0
					it[weaponsCol] = update.tech["weapons"]?.level ?: 0
					it[rangeCol] = update.tech["propulsion"]?.level ?: 0
					it[scanningCol] = update.tech["scanning"]?.level ?: 0
					it[bankingCol] = update.tech["banking"]?.level ?: 0
					it[manufacturingCol] = update.tech["manufacturing"]?.level ?: 0
					it[experimentationCol] = update.tech["research"]?.level ?: 0
				}
				true
			} catch (esqle: ExposedSQLException) {
				false
			}
		}

	private fun ResultRow.parse(): Cycle = Cycle(
		ID = this[id].value,
		playerID = this[playerCol].value,
		cycle = this[cycleCol],
		economy = this[economyCol],
		industry = this[industryCol],
		science = this[scienceCol],
		stars = this[starsCol],
		fleet = this[fleetCol],
		ships = this[shipsCol],
		isActive = this[isActiveCol],
		weapons = this[weaponsCol],
		range = this[rangeCol],
		scanning = this[scanningCol],
		banking = this[bankingCol],
		manufacturing = this[manufacturingCol],
		experimentation = this[experimentationCol]
	)
}