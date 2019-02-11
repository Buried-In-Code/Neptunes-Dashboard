package macro.neptunes.data

import macro.neptunes.core.Game
import macro.neptunes.core.Util
import macro.neptunes.core.Util.toJavaDateTime
import macro.neptunes.core.Util.toJodaDateTime
import macro.neptunes.network.RESTClient
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
	private val adminCol: Column<Int?> = integer(name = "admin").nullable()
	private val fleetSpeedCol: Column<Double?> = double(name = "fleetSpeed").nullable()
	private val isGameOverCol: Column<Boolean?> = bool(name = "isGameOver").nullable()
	private val isPausedCol: Column<Boolean?> = bool(name = "isPaused").nullable()
	private val isStartedCol: Column<Boolean?> = bool(name = "isStarted").nullable()
	private val isTurnBasedCol: Column<Boolean?> = bool(name = "isTurnBased").nullable()
	private val productionCounterCol: Column<Int?> = integer(name = "productionCounter").nullable()
	private val productionRateCol: Column<Int?> = integer(name = "productionRate").nullable()
	private val tickCol: Column<Int?> = integer(name = "tick").nullable()
	private val tickFragmentCol: Column<Int?> = integer(name = "tickFragment").nullable()
	private val tickRateCol: Column<Int?> = integer(name = "tickRate").nullable()
	private val tradeCostCol: Column<Int?> = integer(name = "tradeCost").nullable()
	private val tradeScannedCol: Column<Int?> = integer(name = "tradeScanned").nullable()
	private val turnBasedTimeoutCol: Column<Int?> = integer(name = "turnBasedTimeout").nullable()
	private val warCol: Column<Int?> = integer(name = "war").nullable()

	init {
		Util.query {
			uniqueIndex(startTimeCol, nameCol)
			SchemaUtils.create(this)
		}
	}

	fun search(): List<Game> = Util.query {
		selectAll().map {
			it.parse()
		}
	}

	fun select(ID: Long): Game? = Util.query {
		select {
			id eq ID
		}.map {
			it.parse()
		}.firstOrNull()
	}

	fun insert(ID: Long): Game? {
		val results = RESTClient(gameID = ID).getRequest(endpoint = "/full")
		if (results["Code"] == 200 && results["Data"] != null) {
			val data = results["Data"] as Map<String, Any?>
			val game = GameTable.insert(ID = ID, data = parseData(data = data))
			PlayerTable.insertAll(game = game, data = data["players"] as Map<String, Map<String, Any?>>)
			return game
		}
		return null
	}

	fun insert(ID: Long, data: Map<String, Any?>): Game = Util.query {
		var game = select(ID = ID)
		if (game == null){
			insert {
				it[id] = EntityID(ID, GameTable)
				it[nameCol] = data["name"] as String
				it[startTimeCol] = (data["startTime"] as LocalDateTime).toJodaDateTime()
				it[totalStarsCol] = data["totalStars"] as Int
				it[victoryStarsCol] = data["victoryStars"] as Int
				it[productionsCol] = data["productions"] as Int
				it[lastUpdatedCol] = LocalDateTime.now().toJodaDateTime()
				it[adminCol] = data["admin"] as Int
				it[fleetSpeedCol] = data["fleetSpeed"] as Double
				it[isGameOverCol] = data["isGameOver"] as Boolean
				it[isPausedCol] = data["isPaused"] as Boolean
				it[isStartedCol] = data["isStarted"] as Boolean
				it[isTurnBasedCol] = data["isTurnBased"] as Boolean
				it[productionCounterCol] = data["productionCounter"] as Int
				it[productionRateCol] = data["productionRate"] as Int
				it[tickCol] = data["tick"] as Int
				it[tickFragmentCol] = data["tickFragment"] as Int
				it[tickRateCol] = data["tickRate"] as Int
				it[tradeCostCol] = data["tradeCost"] as Int
				it[tradeScannedCol] = data["tradeScanned"] as Int
				it[turnBasedTimeoutCol] = data["turnBasedTimeout"] as Int
				it[warCol] = data["war"] as Int
			}
			game = select(ID = ID)!!
		}
		game
	}

	private fun parseData(data: Map<String, Any?>): Map<String, Any> = Util.query {
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
		mapOf(
			"name" to name,
			"startTime" to startTime,
			"totalStars" to totalStars,
			"victoryStars" to victoryStars,
			"productions" to productions,
			"admin" to admin,
			"fleetSpeed" to fleetSpeed,
			"isGameOver" to isGameOver,
			"isPaused" to isPaused,
			"isStarted" to isStarted,
			"isTurnBased" to isTurnBased,
			"productionCounter" to productionCounter,
			"productionRate" to productionRate,
			"tick" to tick,
			"tickFragment" to tickFragment,
			"tickRate" to tickRate,
			"tradeCost" to tradeCost,
			"tradeScanned" to tradeScanned,
			"turnBasedTimeout" to turnBasedTimeout,
			"war" to war
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