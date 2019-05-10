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
	private val playerCol = reference(
		name = "playerID",
		foreign = PlayerTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	private val cycleCol = integer(name = "cycle")
	private val economyCol = integer(name = "economy").default(0)
	private val industryCol = integer(name = "industry").default(0)
	private val scienceCol = integer(name = "science").default(0)
	private val starsCol = integer(name = "stars")
	private val fleetCol = integer(name = "fleet")
	private val shipsCol = integer(name = "ships")
	private val isActiveCol = bool(name = "isActive")
	private val scanningCol = integer(name = "scanning").default(0)
	private val hyperspaceCol = integer(name = "hyperspace").default(0)
	private val experimentationCol = integer(name = "experimentation").default(0)
	private val weaponsCol = integer(name = "weapons").default(0)
	private val bankingCol = integer(name = "banking").default(0)
	private val manufacturingCol = integer(name = "manufacturing").default(0)

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
					it[scanningCol] = update.tech["Scanning"]?.level ?: 0
					it[hyperspaceCol] = update.tech["Hyperspace"]?.level ?: 0
					it[experimentationCol] = update.tech["Experimentation"]?.level ?: 0
					it[weaponsCol] = update.tech["Weapons"]?.level ?: 0
					it[bankingCol] = update.tech["Banking"]?.level ?: 0
					it[manufacturingCol] = update.tech["Manufacturing"]?.level ?: 0
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
		scanning = this[scanningCol],
		hyperspace = this[hyperspaceCol],
		experimentation = this[experimentationCol],
		weapons = this[weaponsCol],
		banking = this[bankingCol],
		manufacturing = this[manufacturingCol]
	)
}