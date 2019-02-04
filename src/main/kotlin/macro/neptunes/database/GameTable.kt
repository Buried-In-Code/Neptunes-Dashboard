package macro.neptunes.database

import macro.neptunes.core.Util
import macro.neptunes.core.Util.toJavaDateTime
import macro.neptunes.core.Util.toJodaDateTime
import macro.neptunes.core.game.Game
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import java.time.LocalDateTime

/**
 * Created by Macro303 on 2019-Feb-05.
 */
object GameTable : Table(name = "Game") {
	private val fleetSpeedCol: Column<Double> = double(name = "fleetSpeed")
	private val isPausedCol: Column<Boolean> = bool(name = "isPaused")
	private val productionsCol: Column<Int> = integer(name = "productions")
	private val tickFragmentCol: Column<Int> = integer(name = "tickFragment")
	private val tickRateCol: Column<Int> = integer(name = "tickRate")
	private val productionRateCol: Column<Int> = integer(name = "productionRate")
	private val victoryStarsCol: Column<Int> = integer(name = "victoryStars")
	private val isGameOverCol: Column<Boolean> = bool(name = "isGameOver")
	private val isStartedCol: Column<Boolean> = bool(name = "isStarted")
	private val startTimeCol: Column<DateTime> = datetime(name = "startTime")
	private val totalStarsCol: Column<Int> = integer(name = "totalStars")
	private val productionCounterCol: Column<Int> = integer(name = "productionCounter")
	private val tradeScannedCol: Column<Int> = integer(name = "tradeScanned")
	private val tickCol: Column<Int> = integer(name = "tick")
	private val tradeCostCol: Column<Int> = integer(name = "tradeCost")
	private val nameCol: Column<String> = text(name = "name")
	private val adminCol: Column<Int> = integer(name = "admin")
	private val isTurnBasedCol: Column<Boolean> = bool(name = "isTurnBased")
	private val warCol: Column<Int> = integer(name = "war")
	private val turnBasedTimeoutCol: Column<Int> = integer(name = "turnBasedTimeout")

	init {
		Util.query {
			uniqueIndex(nameCol, tickCol)
			SchemaUtils.create(this)
		}
	}

	fun select(name: String): Game? = Util.query {
		GameTable.select {
			nameCol eq name
		}.orderBy(tickCol).map {
			Game(
				fleetSpeed = it[fleetSpeedCol],
				isPaused = it[isPausedCol],
				productions = it[productionsCol],
				tickRate = it[tickRateCol],
				productionRate = it[productionRateCol],
				victoryStars = it[victoryStarsCol],
				isGameOver = it[isGameOverCol],
				isStarted = it[isStartedCol],
				startTime = it[startTimeCol].toJavaDateTime(),
				totalStars = it[totalStarsCol],
				productionCounter = it[productionCounterCol],
				tick = it[tickCol],
				tradeCost = it[tradeCostCol],
				name = it[nameCol],
				isTurnBased = it[isTurnBasedCol]
			)
		}.firstOrNull()
	}

	fun insert(
		fleetSpeed: Double,
		isPaused: Boolean,
		productions: Int,
		tickFragment: Int,
		tickRate: Int,
		productionRate: Int,
		victoryStars: Int,
		isGameOver: Boolean,
		isStarted: Boolean,
		startTime: LocalDateTime,
		totalStars: Int,
		productionCounter: Int,
		tradeScanned: Int,
		tick: Int,
		tradeCost: Int,
		name: String,
		admin: Int,
		isTurnBased: Boolean,
		war: Int,
		turnBasedTimeout: Int
	): Game = Util.query {
		val game = select(name = name)
		if (game == null) {
			GameTable.insert {
				it[fleetSpeedCol] = fleetSpeed
				it[isPausedCol] = isPaused
				it[productionsCol] = productions
				it[tickFragmentCol] = tickFragment
				it[tickRateCol] = tickRate
				it[productionRateCol] = productionRate
				it[victoryStarsCol] = victoryStars
				it[isGameOverCol] = isGameOver
				it[isStartedCol] = isStarted
				it[startTimeCol] = startTime.toJodaDateTime()
				it[totalStarsCol] = totalStars
				it[productionCounterCol] = productionCounter
				it[tradeScannedCol] = tradeScanned
				it[tickCol] = tick
				it[tradeCostCol] = tradeCost
				it[nameCol] = name
				it[adminCol] = admin
				it[isTurnBasedCol] = isTurnBased
				it[warCol] = war
				it[turnBasedTimeoutCol] = turnBasedTimeout
			}
			select(name = name)!!
		} else
			game
	}
}