package macro.dashboard.neptunes.game

import macro.dashboard.neptunes.Config.Companion.CONFIG
import macro.dashboard.neptunes.NotFoundResponse
import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.Util.toJavaDateTime
import macro.dashboard.neptunes.Util.toJodaDateTime
import macro.dashboard.neptunes.backend.ProteusGame
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
	val gameTypeCol = text(name = "gameType").default("Triton")
	val fleetSpeedCol = double(name = "fleetSpeed")
	val isPausedCol = bool(name = "isPaused")
	val productionsCol = integer(name = "productions")
	val fleetPriceCol = integer(name = "fleetPrice").nullable()
	val tickFragmentCol = integer(name = "tickFragment")
	val tickRateCol = integer(name = "tickRate")
	val productionRateCol = integer(name = "productionRate")
	val victoryStarsCol = integer(name = "victoryStars")
	val isGameOverCol = bool(name = "isGameOver")
	val isStartedCol = bool(name = "isStarted")
	val startTimeCol = datetime(name = "startTime").uniqueIndex()
	val totalStarsCol = integer(name = "totalStars")
	val productionCounterCol = integer(name = "productionCounter")
	val isTradeScannedCol = bool(name = "isTradeScanned")
	val tickCol = integer(name = "tick")
	val tradeCostCol = integer(name = "tradeCost")
	val nameCol = text(name = "name")
	val isTurnBasedCol = bool(name = "isTurnBased")
	val warCol = integer(name = "war")
	val cycleTimeoutCol = datetime(name = "turnBasedTimeout")

	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	init {
		Util.query(description = "Create Game table") {
			SchemaUtils.create(this)
		}
	}

	fun searchAll(): List<Game> = Util.query(description = "Select all Games") {
		selectAll().orderBy(startTimeCol, SortOrder.DESC).map {
			it.parse()
		}
	}

	fun select(ID: Long): Game? = Util.query(description = "Select Game by ID: $ID") {
		select {
			id eq ID
		}.orderBy(startTimeCol, SortOrder.DESC).limit(n = 1).firstOrNull()?.parse()
	}

	fun select(): Game = select(ID = CONFIG.gameID)
		?: throw NotFoundResponse(message = "No Game was found with the ID => ${CONFIG.gameID}")

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
				it[cycleTimeoutCol] = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(update.cycleTimeout), ZoneId.of("Pacific/Auckland")
				).toJodaDateTime()
			}
			TeamTable.insert(gameID = ID, name = "Free For All")
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun update(update: ProteusGame): Boolean = Util.query(description = "Update Proteus Game") {
		try {
			update({ id eq CONFIG.gameID }) {
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
				it[cycleTimeoutCol] = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(update.cycleTimeout), ZoneId.of("Pacific/Auckland")
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
		cycleTimeout = this[cycleTimeoutCol].toJavaDateTime()
	)
}