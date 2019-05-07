package macro.dashboard.neptunes.game

import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.Util.toJavaDateTime
import macro.dashboard.neptunes.Util.toJodaDateTime
import macro.dashboard.neptunes.backend.ProteusGame
import macro.dashboard.neptunes.backend.TritonGame
import macro.dashboard.neptunes.team.TeamTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.slf4j.LoggerFactory
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created by Macro303 on 2019-Feb-11.
 */
object GameTable : LongIdTable(name = "Game") {
	private val gameTypeCol = text(name = "gameType").default("Triton")
	private val fleetSpeedCol = double(name = "fleetSpeed")
	private val isPausedCol = bool(name = "isPaused")
	private val productionsCol = integer(name = "productions")
	private val fleetPriceCol = integer(name = "fleetPrice").nullable()
	private val tickFragmentCol = integer(name = "tickFragment")
	private val tickRateCol = integer(name = "tickRate")
	private val productionRateCol = integer(name = "productionRate")
	private val victoryStarsCol = integer(name = "victoryStars")
	private val isGameOverCol = bool(name = "isGameOver")
	private val isStartedCol = bool(name = "isStarted")
	private val startTimeCol = datetime(name = "startTime").uniqueIndex()
	private val totalStarsCol = integer(name = "totalStars")
	private val productionCounterCol = integer(name = "productionCounter")
	private val isTradeScannedCol = bool(name = "isTradeScanned")
	private val tickCol = integer(name = "tick")
	private val tradeCostCol = integer(name = "tradeCost")
	private val nameCol = text(name = "name")
	private val isTurnBasedCol = bool(name = "isTurnBased")
	private val warCol = integer(name = "war")
	private val turnBasedTimeoutCol = datetime(name = "turnBasedTimeout")

	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	init {
		Util.query(description = "Create Game table") {
			SchemaUtils.create(this)
		}
	}

	fun searchAll(): List<Game> = Util.query(description = "Select All Games") {
		selectAll().orderBy(startTimeCol to SortOrder.DESC).map {
			it.parse()
		}
	}

	fun select(ID: Long): Game? = Util.query(description = "Select Game by ID: $ID") {
		select {
			id eq ID
		}.orderBy(startTimeCol, SortOrder.DESC).limit(n = 1).firstOrNull()?.parse()
	}

	fun selectLatest(): Game = Util.query(description = "Select Latest Game") {
		selectAll().orderBy(startTimeCol to SortOrder.DESC).limit(n = 1).first().parse()
	}

	fun insert(ID: Long, update: TritonGame): Boolean = Util.query(description = "Insert Triton Game") {
		try {
			insert {
				it[id] = EntityID(ID, GameTable)
				it[gameTypeCol] = "Triton"
				it[fleetSpeedCol] = update.fleetSpeed
				it[isPausedCol] = update.isPaused
				it[productionsCol] = update.productions
				it[fleetPriceCol] = null
				it[tickFragmentCol] = update.tickFragment
				it[tickRateCol] = update.tickRate
				it[productionRateCol] = update.productionRate
				it[victoryStarsCol] = update.victoryStars
				it[isGameOverCol] = update.gameOver == 1
				it[isStartedCol] = update.isStarted
				it[startTimeCol] = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(update.startTime), ZoneId.of("Pacific/Auckland")
				).toJodaDateTime()
				it[totalStarsCol] = update.totalStars
				it[productionCounterCol] = update.productionCounter
				it[isTradeScannedCol] = update.tradeScanned == 1
				it[tickCol] = update.tick
				it[tradeCostCol] = update.tradeCost
				it[nameCol] = update.name
				it[isTurnBasedCol] = update.turnBased == 1
				it[warCol] = update.war
				it[turnBasedTimeoutCol] = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(update.turnBasedTimeout), ZoneId.of("Pacific/Auckland")
				).toJodaDateTime()
			}
			TeamTable.insert(gameID = ID, name = "Free For All")
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun insert(ID: Long, update: ProteusGame): Boolean = Util.query(description = "Insert Proteus Game") {
		try {
			insert {
				it[id] = EntityID(ID, GameTable)
				it[gameTypeCol] = "Proteus"
				it[fleetSpeedCol] = update.fleetSpeed
				it[isPausedCol] = update.isPaused
				it[productionsCol] = update.productions
				it[fleetPriceCol] = update.fleetPrice
				it[tickFragmentCol] = update.tickFragment
				it[tickRateCol] = update.tickRate
				it[productionRateCol] = update.productionRate
				it[victoryStarsCol] = update.victoryStars
				it[isGameOverCol] = update.gameOver == 1
				it[isStartedCol] = update.isStarted
				it[startTimeCol] = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(update.startTime), ZoneId.of("Pacific/Auckland")
				).toJodaDateTime()
				it[totalStarsCol] = update.totalStars
				it[productionCounterCol] = update.productionCounter
				it[isTradeScannedCol] = update.tradeScanned == 1
				it[tickCol] = update.tick
				it[tradeCostCol] = update.tradeCost
				it[nameCol] = update.name
				it[isTurnBasedCol] = update.turnBased == 1
				it[warCol] = update.war
				it[turnBasedTimeoutCol] = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(update.turnBasedTimeout), ZoneId.of("Pacific/Auckland")
				).toJodaDateTime()
			}
			TeamTable.insert(gameID = ID, name = "Free For All")
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun update(ID: Long, update: TritonGame): Boolean = Util.query(description = "Update Triton Game") {
		try {
			update({ id eq ID }) {
				it[isPausedCol] = update.isPaused
				it[productionsCol] = update.productions
				it[tickFragmentCol] = update.tickFragment
				it[isGameOverCol] = update.gameOver == 1
				it[isStartedCol] = update.isStarted
				it[startTimeCol] = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(update.startTime), ZoneId.of("Pacific/Auckland")
				).toJodaDateTime()
				it[productionCounterCol] = update.productionCounter
				it[tickCol] = update.tick
				it[warCol] = update.war
				it[turnBasedTimeoutCol] = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(update.turnBasedTimeout), ZoneId.of("Pacific/Auckland")
				).toJodaDateTime()
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun update(ID: Long, update: ProteusGame): Boolean = Util.query(description = "Update Proteus Game") {
		try {
			update({ id eq ID }) {
				it[isPausedCol] = update.isPaused
				it[productionsCol] = update.productions
				it[tickFragmentCol] = update.tickFragment
				it[isGameOverCol] = update.gameOver == 1
				it[isStartedCol] = update.isStarted
				it[startTimeCol] = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(update.startTime), ZoneId.of("Pacific/Auckland")
				).toJodaDateTime()
				it[productionCounterCol] = update.productionCounter
				it[tickCol] = update.tick
				it[warCol] = update.war
				it[turnBasedTimeoutCol] = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(update.turnBasedTimeout), ZoneId.of("Pacific/Auckland")
				).toJodaDateTime()
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	private fun ResultRow.parse() = Game(
		ID = this[id].value,
		gameType = this[gameTypeCol],
		fleetSpeed = this[fleetSpeedCol],
		isPaused = this[isPausedCol],
		productions = this[productionsCol],
		fleetPrice = this[fleetPriceCol],
		tickFragment = this[tickFragmentCol],
		tickRate = this[tickRateCol],
		productionRate = this[productionRateCol],
		victoryStars = this[victoryStarsCol],
		isGameOver = this[isGameOverCol],
		isStarted = this[isStartedCol],
		startTime = this[startTimeCol].toJavaDateTime(),
		totalStars = this[totalStarsCol],
		productionCounter = this[productionCounterCol],
		isTradeScanned = this[isTradeScannedCol],
		tick = this[tickCol],
		tradeCost = this[tradeCostCol],
		name = this[nameCol],
		isTurnBased = this[isTurnBasedCol],
		war = this[warCol],
		turnTimeout = this[turnBasedTimeoutCol].toJavaDateTime()
	)
}