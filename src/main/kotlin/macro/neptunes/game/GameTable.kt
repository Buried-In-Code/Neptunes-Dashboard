package macro.neptunes.game

import macro.neptunes.Util
import macro.neptunes.backend.GameUpdate
import macro.neptunes.team.TeamTable
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*

/**
 * Created by Macro303 on 2019-Feb-11.
 */
object GameTable : LongIdTable(name = "Game") {
	private val nameCol: Column<String> = text(name = "name").uniqueIndex()
	private val totalStarsCol: Column<Int> = integer(name = "totalStars")
	private val victoryStarsCol: Column<Int> = integer(name = "victoryStars")
	private val adminCol: Column<Int> = integer(name = "admin")
	private val fleetSpeedCol: Column<Double> = double(name = "fleetSpeed")
	private val isTurnBasedCol: Column<Boolean> = bool(name = "isTurnBased")
	private val productionRateCol: Column<Int> = integer(name = "productionRate")
	private val tickRateCol: Column<Int> = integer(name = "tickRate")
	private val tradeCostCol: Column<Int> = integer(name = "tradeCost")

	private val LOGGER = LogManager.getLogger(GameTable::class.java)

	init {
		Util.query {
			SchemaUtils.create(this)
		}
	}

	fun select(ID: Long): Game? = Util.query {
		select {
			id eq ID
		}.map {
			it.parse()
		}.sorted().firstOrNull()
	}

	fun search(): List<Game> = Util.query {
		selectAll().map {
			it.parse()
		}.sorted()
	}

	fun insert(gameID: Long, update: GameUpdate) = Util.query {
		try {
			insert {
				it[id] = EntityID(gameID, GameTable)
				it[nameCol] = update.name
				it[totalStarsCol] = update.totalStars
				it[victoryStarsCol] = update.victoryStars
				it[adminCol] = update.admin
				it[fleetSpeedCol] = update.fleetSpeed
				it[isTurnBasedCol] = update.turnBased == 1
				it[productionRateCol] = update.productionRate
				it[tickRateCol] = update.tickRate
				it[tradeCostCol] = update.tradeCost
			}
			val game = select(ID = gameID)
			TeamTable.insert(game = game, name = "Free For All")
		} catch (esqle: ExposedSQLException) {
		}
	}

	private fun ResultRow.parse() = Game(
		ID = this[id].value,
		name = this[nameCol],
		totalStars = this[totalStarsCol],
		victoryStars = this[victoryStarsCol],
		admin = this[adminCol],
		fleetSpeed = this[fleetSpeedCol],
		isTurnBased = this[isTurnBasedCol],
		productionRate = this[productionRateCol],
		tickRate = this[tickRateCol],
		tradeCost = this[tradeCostCol]
	)
}