package macro.neptunes.game

import macro.neptunes.Util
import org.jetbrains.exposed.dao.LongIdTable
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