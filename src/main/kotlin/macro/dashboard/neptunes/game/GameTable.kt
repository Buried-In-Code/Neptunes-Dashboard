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
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created by Macro303 on 2019-Feb-11.
 */
object GameTable : LongIdTable(name = "Game") {
	private val nameCol = text(name = "name")
	private val totalStarsCol = integer(name = "totalStars")
	private val victoryStarsCol = integer(name = "victoryStars")
	private val adminCol = integer(name = "admin")
	private val fleetSpeedCol = double(name = "fleetSpeed")
	private val isTurnBasedCol = bool(name = "isTurnBased")
	private val productionRateCol = integer(name = "productionRate")
	private val tickRateCol = integer(name = "tickRate")
	private val tradeCostCol = integer(name = "tradeCost")
	private val startTimeCol = datetime(name = "startTime").uniqueIndex()
	private val productionCol = integer(name = "production")
	private val isGameOverCol = bool(name = "isGameOver")
	private val isPausedCol = bool(name = "isPaused")
	private val isStartedCol = bool(name = "isStarted")
	private val productionCounterCol = integer(name = "productionCounter")
	private val tickCol = integer(name = "tick")
	private val tickFragmentCol = integer(name = "tickFragment")
	private val tradeScannedCol = integer(name = "tradeScanned")
	private val warCol = integer(name = "war")
	private val turnBasedTimeoutCol = long(name = "turnBasedTimeout")

	private val LOGGER = LogManager.getLogger()

	init {
		Util.query(description = "Create Game table") {
			SchemaUtils.create(this)
		}
	}

	fun select(ID: Long): Game? = Util.query(description = "Select Game by ID: $ID") {
		select {
			id eq ID
		}.orderBy(startTimeCol, SortOrder.DESC).limit(n = 1).firstOrNull()?.parse()
	}

	fun selectLatest(): Game? = Util.query(description = "Select Latest Game") {
		selectAll().orderBy(startTimeCol to SortOrder.DESC).limit(n = 1).firstOrNull()?.parse()
	}

	fun search(name: String = ""): List<Game> = Util.query(description = "Search for Games with name: $name") {
		select {
			nameCol like "%$name%"
		}.orderBy(startTimeCol to SortOrder.DESC).map {
			it.parse()
		}
	}

	fun insert(ID: Long, update: GameUpdate): Boolean = Util.query(description = "Insert Game") {
		try {
			insert {
				it[id] = EntityID(ID, GameTable)
				it[nameCol] = update.name
				it[totalStarsCol] = update.totalStars
				it[victoryStarsCol] = update.victoryStars
				it[adminCol] = update.admin
				it[fleetSpeedCol] = update.fleetSpeed
				it[isTurnBasedCol] = update.turnBased == 1
				it[productionRateCol] = update.productionRate
				it[tickRateCol] = update.tickRate
				it[tradeCostCol] = update.tradeCost
				it[startTimeCol] = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(update.startTime), ZoneId.systemDefault()
				).toJodaDateTime()
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

	fun update(ID: Long, update: GameUpdate): Boolean = Util.query(description = "Update Game") {
		try {
			update({ id eq ID }) {
				it[startTimeCol] = LocalDateTime.ofInstant(
					Instant.ofEpochMilli(update.startTime), ZoneId.systemDefault()
				).toJodaDateTime()
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
			true
		} catch (esqle: ExposedSQLException) {
			false
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