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

	private val LOGGER = LogManager.getLogger(TurnTable::class.java)

	init {
		Util.query {
			uniqueIndex(tickCol, playerCol)
			SchemaUtils.create(this)
		}
	}

	fun select(ID: Int): Turn? = Util.query {
		select {
			id eq ID
		}.map {
			it.parse()
		}.sorted().firstOrNull()
	}

	fun select(playerID: Int, tick: Int): Turn? = Util.query {
		select {
			playerCol eq playerID and(tickCol eq tick)
		}.map {
			it.parse()
		}.sorted().firstOrNull()
	}

	fun searchByTick(tick: Int): List<Turn> = Util.query {
		select {
			tickCol eq tick
		}.map {
			it.parse()
		}.sorted()
	}

	fun searchByPlayer(playerID: Int): List<Turn> = Util.query {
		select {
			playerCol eq playerID
		}.map {
			it.parse()
		}.sorted()
	}

	fun insert(playerID: Int, tick: Int, update: PlayerUpdate): Boolean = Util.query {
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