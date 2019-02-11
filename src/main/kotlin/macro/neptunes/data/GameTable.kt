package macro.neptunes.data

import macro.neptunes.core.Game
import macro.neptunes.core.Util
import macro.neptunes.core.Util.toJavaDateTime
import macro.neptunes.core.Util.toJodaDateTime
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.math.roundToInt
import kotlin.math.roundToLong

/**
 * Created by Macro303 on 2019-Feb-11.
 */
object GameTable : LongIdTable(name = "Game") {
	private val nameCol: Column<String> = text(name = "name")
	private val startTimeCol: Column<DateTime> = datetime(name = "startTime")
	private val totalStarsCol: Column<Int> = integer(name = "totalStars")
	private val victoryStarsCol: Column<Int> = integer(name = "victoryStars")
	private val productionsCol: Column<Int> = integer(name = "productions")
	private val lastUpdatedCol: Column<DateTime> = datetime(name = "lastUpdated").default(DateTime.parse("1900-01-01 00:00:00", Util.JODA_FORMATTER))
	private val adminCol: Column<Int> = integer(name = "admin")
	private val fleetSpeedCol: Column<Double> = double(name = "fleetSpeed")
	private val isGameOverCol: Column<Boolean> = bool(name = "isGameOver")
	private val isPausedCol: Column<Boolean> = bool(name = "isPaused")
	private val isStartedCol: Column<Boolean> = bool(name = "isStarted")
	private val isTurnBasedCol: Column<Boolean> = bool(name = "isTurnBased")
	private val productionCounterCol: Column<Int> = integer(name = "productionCounter")
	private val productionRateCol: Column<Int> = integer(name = "productionRate")
	private val tickCol: Column<Int> = integer(name = "tick")
	private val tickFragmentCol: Column<Int> = integer(name = "tickFragment")
	private val tickRateCol: Column<Int> = integer(name = "tickRate")
	private val tradeCostCol: Column<Int> = integer(name = "tradeCost")
	private val tradeScannedCol: Column<Int> = integer(name = "tradeScanned")
	private val turnBasedTimeoutCol: Column<Int> = integer(name = "turnBasedTimeout")
	private val warCol: Column<Int> = integer(name = "war")

	init {
		Util.query {
			uniqueIndex(startTimeCol, nameCol)
			SchemaUtils.create(this)
		}
	}

	fun selectCurrent():Game? = Util.query{
		selectAll().orderBy(startTimeCol).map{
			it.parse()
		}.firstOrNull()
	}

	fun select(ID: Long): Game? = Util.query {
		select {
			id eq ID
		}.map {
			it.parse()
		}.firstOrNull()
	}

	fun insert(game: Game): Game = Util.query {
		insert {
			it[id] = EntityID(game.ID, GameTable)
			it[nameCol] = game.name
			it[startTimeCol] = game.startTime.toJodaDateTime()
			it[totalStarsCol] = game.totalStars
			it[victoryStarsCol] = game.victoryStars
			it[productionsCol] = game.productions
			it[lastUpdatedCol] = LocalDateTime.now().toJodaDateTime()
			it[adminCol] = game.admin
			it[fleetSpeedCol] = game.fleetSpeed
			it[isGameOverCol] = game.isGameOver
			it[isPausedCol] = game.isPaused
			it[isStartedCol] = game.isStarted
			it[isTurnBasedCol] = game.isTurnBased
			it[productionCounterCol] = game.productionCounter
			it[productionRateCol] = game.productionRate
			it[tickCol] = game.tick
			it[tickFragmentCol] = game.tickFragment
			it[tickRateCol] = game.tickRate
			it[tradeCostCol] = game.tradeCost
			it[tradeScannedCol] = game.tradeScanned
			it[turnBasedTimeoutCol] = game.turnBasedTimeout
			it[warCol] = game.war
		}
		select(ID = game.ID)!!
	}

	fun update(game: Game): Game = Util.query {
		update({id eq game.ID}){
			it[productionsCol] = game.productions
			it[lastUpdatedCol] = LocalDateTime.now().toJodaDateTime()
			it[isGameOverCol] = game.isGameOver
			it[isPausedCol] = game.isPaused
			it[isStartedCol] = game.isStarted
			it[tickCol] = game.tick
		}
		select(ID = game.ID)!!
	}

	fun mapToGame(ID: Long, data: Map<String, Any?>): Game = Util.query {
		val name: String = data["name"] as String
		val startTimeLong = (data["start_time"] as Double).roundToLong()
		val startTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTimeLong), ZoneId.systemDefault())
		val totalStars = (data["total_stars"] as Double).roundToInt()
		val victoryStars = (data["stars_for_victory"] as Double).roundToInt()
		val productions = (data["productions"] as Double).roundToInt()
		val admin = (data["admin"] as Double).roundToInt()
		val fleetSpeed = data["fleet_speed"] as Double
		val isGameOver = data["game_over"].toString().toBoolean()
		val isPaused = data["paused"].toString().toBoolean()
		val isStarted = data["started"].toString().toBoolean()
		val isTurnBased = data["turn_based"].toString().toBoolean()
		val productionCounter = (data["production_counter"] as Double).roundToInt()
		val productionRate = (data["production_rate"] as Double).roundToInt()
		val tick = (data["tick"] as Double).roundToInt()
		val tickFragment = (data["tick_fragment"] as Double).roundToInt()
		val tickRate = (data["tick_rate"] as Double).roundToInt()
		val tradeCost = (data["trade_cost"] as Double).roundToInt()
		val tradeScanned = (data["trade_scanned"] as Double).roundToInt()
		val turnBasedTimeout = (data["turn_based_time_out"] as Double).roundToInt()
		val war = (data["war"] as Double).roundToInt()
		Game(
			ID = ID,
			name = name,
			startTime = startTime,
			totalStars = totalStars,
			victoryStars = victoryStars,
			productions = productions,
			lastUpdated = LocalDateTime.now(),
			admin = admin,
			fleetSpeed = fleetSpeed,
			isGameOver = isGameOver,
			isPaused = isPaused,
			isStarted = isStarted,
			isTurnBased = isTurnBased,
			productionCounter = productionCounter,
			productionRate = productionRate,
			tick = tick,
			tickFragment = tickFragment,
			tickRate = tickRate,
			tradeCost = tradeCost,
			tradeScanned = tradeScanned,
			turnBasedTimeout = turnBasedTimeout,
			war = war
		)
	}

	private fun ResultRow.parse() = Game(
		ID = this[id].value,
		name = this[nameCol],
		startTime = this[startTimeCol].toJavaDateTime(),
		totalStars = this[totalStarsCol],
		victoryStars = this[victoryStarsCol],
		productions = this[productionsCol],
		lastUpdated = this[lastUpdatedCol].toJavaDateTime(),
		admin = this[adminCol],
		fleetSpeed = this[fleetSpeedCol],
		isGameOver = this[isGameOverCol],
		isPaused = this[isPausedCol],
		isStarted = this[isStartedCol],
		isTurnBased = this[isTurnBasedCol],
		productionCounter = this[productionCounterCol],
		productionRate = this[productionRateCol],
		tick = this[tickCol],
		tickFragment = this[tickFragmentCol],
		tickRate = this[tickRateCol],
		tradeCost = this[tradeCostCol],
		tradeScanned = this[tradeScannedCol],
		turnBasedTimeout = this[turnBasedTimeoutCol],
		war = this[warCol]
	)
}