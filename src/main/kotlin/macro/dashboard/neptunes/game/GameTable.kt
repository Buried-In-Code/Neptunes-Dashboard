package macro.dashboard.neptunes.game

import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.Util.toJavaDateTime
import macro.dashboard.neptunes.Util.toJodaDateTime
import macro.dashboard.neptunes.backend.GameUpdate
import macro.dashboard.neptunes.team.TeamTable
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created by Macro303 on 2019-Feb-11.
 */
object GameTable : LongIdTable(name = "Game") {
	private val codeCol: Column<String> = text(name = "code")
	private val nameCol: Column<String> = text(name = "name")
	private val totalStarsCol: Column<Int> = integer(name = "totalStars")
	private val victoryStarsCol: Column<Int> = integer(name = "victoryStars")
	private val adminCol: Column<Int> = integer(name = "admin")
	private val fleetSpeedCol: Column<Double> = double(name = "fleetSpeed")
	private val isTurnBasedCol: Column<Boolean> = bool(name = "isTurnBased")
	private val productionRateCol: Column<Int> = integer(name = "productionRate")
	private val tickRateCol: Column<Int> = integer(name = "tickRate")
	private val tradeCostCol: Column<Int> = integer(name = "tradeCost")
	private val startTimeCol: Column<DateTime> = datetime(name = "startTime")
	private val productionCol: Column<Int> = integer(name = "production")
	private val isGameOverCol: Column<Boolean> = bool(name = "isGameOver")
	private val isPausedCol: Column<Boolean> = bool(name = "isPaused")
	private val isStartedCol: Column<Boolean> = bool(name = "isStarted")
	private val productionCounterCol: Column<Int> = integer(name = "productionCounter")
	private val tickCol: Column<Int> = integer(name = "tick")
	private val tickFragmentCol: Column<Int> = integer(name = "tickFragment")
	private val tradeScannedCol: Column<Int> = integer(name = "tradeScanned")
	private val warCol: Column<Int> = integer(name = "war")
	private val turnBasedTimeoutCol: Column<Long> = long(name = "turnBasedTimeout")

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

	fun search(name: String = ""): List<Game> = Util.query {
		select {
			nameCol like "%$name%"
		}.map {
			it.parse()
		}.sorted()
	}

	fun insert(ID: Long, code: String, update: GameUpdate): Boolean = Util.query {
		try {
			insert {
				it[id] = EntityID(ID, GameTable)
				it[codeCol] = code
				it[nameCol] = update.name
				it[totalStarsCol] = update.totalStars
				it[victoryStarsCol] = update.victoryStars
				it[adminCol] = update.admin
				it[fleetSpeedCol] = update.fleetSpeed
				it[isTurnBasedCol] = update.turnBased == 1
				it[productionRateCol] = update.productionRate
				it[tickRateCol] = update.tickRate
				it[tradeCostCol] = update.tradeCost
				it[startTimeCol] =
					LocalDateTime.ofInstant(Instant.ofEpochMilli(update.startTime), ZoneId.systemDefault())
						.toJodaDateTime()
				it[productionCol] = update.production
				it[isGameOverCol] = update.gameOver == 1
				it[isPausedCol] = update.isPaused
				it[isStartedCol] = update.isStarted
				it[productionCounterCol] = update.productionCounter
				it[tickCol] = update.tick
				it[tickFragmentCol] = update.tickFragment
				it[tradeScannedCol] = update.tradeScanned
				it[warCol] = update.war
				it[turnBasedTimeoutCol] = update.turnBasedTimeout
			}
			TeamTable.insert(gameID = ID, name = "Free For All")
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun update(ID: Long, update: GameUpdate): Boolean = Util.query {
		try {
			update({ id eq ID }) {
				it[isGameOverCol] = update.gameOver == 1
				it[isPausedCol] = update.isPaused
				it[isStartedCol] = update.isStarted
				it[startTimeCol] =
					LocalDateTime.ofInstant(Instant.ofEpochMilli(update.startTime), ZoneId.systemDefault())
						.toJodaDateTime()
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	private fun ResultRow.parse() = Game(
		ID = this[id].value,
		code = this[codeCol],
		name = this[nameCol],
		totalStars = this[totalStarsCol],
		victoryStars = this[victoryStarsCol],
		admin = this[adminCol],
		fleetSpeed = this[fleetSpeedCol],
		isTurnBased = this[isTurnBasedCol],
		productionRate = this[productionRateCol],
		tickRate = this[tickRateCol],
		tradeCost = this[tradeCostCol],
		startTime = this[startTimeCol].toJavaDateTime(),
		production = this[productionCol],
		isGameOver = this[isGameOverCol],
		isPaused = this[isPausedCol],
		isStarted = this[isStartedCol],
		productionCounter = this[productionCounterCol],
		tick = this[tickCol],
		tickFragment = this[tickFragmentCol],
		tradeScanned = this[tradeScannedCol],
		war = this[warCol],
		turnBasedTimeout = this[turnBasedTimeoutCol]
	)
}