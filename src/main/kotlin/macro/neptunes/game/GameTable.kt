package macro.neptunes.game

import macro.neptunes.Util
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
	private val turnBasedTimeoutCol: Column<Int> = integer(name = "turnBasedTimeout")

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
		}.firstOrNull()
	}

	fun select(): Game? = Util.query {
		selectAll().map {
			it.parse()
		}.firstOrNull()
	}

	fun insert(
		ID: Long,
		name: String,
		totalStars: Int,
		victoryStars: Int,
		admin: Int,
		fleetSpeed: Double,
		isTurnBased: Boolean,
		productionRate: Int,
		tickRate: Int,
		tradeCost: Int,
		turnBasedTimeout: Int
	): Game = Util.query {
		try {
			insert {
				it[id] = EntityID(id = ID, table = GameTable)
				it[nameCol] = name
				it[totalStarsCol] = totalStars
				it[victoryStarsCol] = victoryStars
				it[adminCol] = admin
				it[fleetSpeedCol] = fleetSpeed
				it[isTurnBasedCol] = isTurnBased
				it[productionRateCol] = productionRate
				it[tickRateCol] = tickRate
				it[tradeCostCol] = tradeCost
				it[turnBasedTimeoutCol] = turnBasedTimeout
			}
			select(ID = ID)!!
		} catch (esqle: ExposedSQLException) {
			select(ID = ID)!!
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
		tradeCost = this[tradeCostCol],
		turnBasedTimeout = this[turnBasedTimeoutCol]
	)
}