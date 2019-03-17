package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.backend.PlayerUpdate
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*

/**
 * Created by Macro303 on 2019-Mar-04.
 */
object TurnTable : IntIdTable(name = "Turn") {
	private val playerCol: Column<EntityID<Int>> = reference(
		name = "playerID",
		foreign = PlayerTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	private val tickCol: Column<Int> = integer(name = "tick")
	private val economyCol: Column<Int> = integer(name = "economy")
	private val industryCol: Column<Int> = integer(name = "industry")
	private val scienceCol: Column<Int> = integer(name = "science")
	private val starsCol: Column<Int> = integer(name = "stars")
	private val fleetCol: Column<Int> = integer(name = "fleet")
	private val shipsCol: Column<Int> = integer(name = "ships")
	private val isActiveCol: Column<Boolean> = bool(name = "isActive")

	private val LOGGER = LogManager.getLogger()

	init {
		Util.query(description = "Create Turn table") {
			uniqueIndex(tickCol, playerCol)
			SchemaUtils.create(this)
		}
	}

	fun select(ID: Int): Turn? = Util.query(description = "Select Turn by ID: $ID") {
		select {
			id eq ID
		}.orderBy(playerCol to SortOrder.ASC, tickCol to SortOrder.DESC).map {
			it.parse()
		}.firstOrNull()
	}

	fun select(playerID: Int, tick: Int): Turn? =
		Util.query(description = "Select Turn by Player: $playerID and Tick: $tick") {
			select {
				playerCol eq playerID and (tickCol eq tick)
			}.orderBy(playerCol to SortOrder.ASC, tickCol to SortOrder.DESC).map {
				it.parse()
			}.firstOrNull()
		}

	fun searchByTick(tick: Int): List<Turn> = Util.query(description = "Search for Turns at Tick: $tick") {
		select {
			tickCol eq tick
		}.orderBy(playerCol to SortOrder.ASC, tickCol to SortOrder.DESC).map {
			it.parse()
		}
	}

	fun searchLatestByPlayer(playerID: Int): Turn? =
		Util.query(description = "Search for Latest Turn by Player: $playerID") {
			select {
				playerCol eq playerID
			}.orderBy(playerCol to SortOrder.ASC, tickCol to SortOrder.DESC).limit(1).map {
				it.parse()
			}.firstOrNull()
		}

	fun searchByPlayer(playerID: Int): List<Turn> = Util.query(description = "Search for Turns by Player: $playerID") {
		select {
			playerCol eq playerID
		}.orderBy(playerCol to SortOrder.ASC, tickCol to SortOrder.DESC).map {
			it.parse()
		}
	}

	fun insert(playerID: Int, tick: Int, update: PlayerUpdate): Boolean = Util.query(description = "Insert Turn") {
		try {
			insert {
				it[playerCol] = EntityID(id = playerID, table = PlayerTable)
				it[tickCol] = tick
				it[economyCol] = update.economy
				it[industryCol] = update.industry
				it[scienceCol] = update.science
				it[starsCol] = update.stars
				it[fleetCol] = update.fleet
				it[shipsCol] = update.ships
				it[isActiveCol] = update.conceded == 0
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	private fun ResultRow.parse(): Turn = Turn(
		ID = this[id].value,
		playerID = this[playerCol].value,
		tick = this[tickCol],
		economy = this[economyCol],
		industry = this[industryCol],
		science = this[scienceCol],
		stars = this[starsCol],
		fleet = this[fleetCol],
		ships = this[shipsCol],
		isActive = this[isActiveCol]
	)
}