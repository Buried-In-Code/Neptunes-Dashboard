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
object PlayerTurnTable : IntIdTable(name = "Player_Turns") {
	/*private val turnCol: Column<EntityID<Int>> = reference(
		name = "turnID",
		foreign = GameTurnTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)*/
	private val playerCol: Column<EntityID<Int>> = reference(
		name = "playerID",
		foreign = PlayerTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	private val economyCol: Column<Int> = integer(name = "economy")
	private val industryCol: Column<Int> = integer(name = "industry")
	private val scienceCol: Column<Int> = integer(name = "science")
	private val starsCol: Column<Int> = integer(name = "stars")
	private val fleetCol: Column<Int> = integer(name = "fleet")
	private val shipsCol: Column<Int> = integer(name = "ships")
	private val isActiveCol: Column<Boolean> = bool(name = "isActive")

	private val LOGGER = LogManager.getLogger(PlayerTurnTable::class.java)

	init {
		Util.query {
			//			uniqueIndex(turnCol, playerCol)
			SchemaUtils.create(this)
		}
	}

	fun select(ID: Int): PlayerTurn? = Util.query {
		select {
			id eq ID
		}.map {
			it.parse()
		}.sorted().firstOrNull()
	}

	/*fun searchByTurn(turnID: Int): List<PlayerTurn> = Util.query {
		select {
			turnCol eq turnID
		}.map {
			it.parse()
		}.sorted()
	}*/

	fun searchByPlayer(playerID: Int): List<PlayerTurn> = Util.query {
		select {
			playerCol eq playerID
		}.map {
			it.parse()
		}.sorted()
	}

	fun insert(turnID: Int, playerID: Int, update: PlayerUpdate): Boolean = Util.query {
		try {
			insert {
				//				it[turnCol] = EntityID(id = turnID, table = GameTurnTable)
				it[playerCol] = EntityID(id = playerID, table = PlayerTable)
				it[economyCol] = update.economy
				it[industryCol] = update.industry
				it[scienceCol] = update.science
				it[starsCol] = update.stars
				it[fleetCol] = update.fleet
				it[shipsCol] = update.ships
				it[isActiveCol] = update.isActive
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	private fun ResultRow.parse(): PlayerTurn = PlayerTurn(
		ID = this[id].value,
		turnID = /*this[turnCol].value*/ 42,
		playerID = this[playerCol].value,
		economy = this[economyCol],
		industry = this[industryCol],
		science = this[scienceCol],
		stars = this[starsCol],
		fleet = this[fleetCol],
		ships = this[shipsCol],
		isActive = this[isActiveCol]
	)
}