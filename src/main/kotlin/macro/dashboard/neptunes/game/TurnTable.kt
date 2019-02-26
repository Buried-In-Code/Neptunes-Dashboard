package macro.dashboard.neptunes.game

import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.Util.toJavaDateTime
import macro.dashboard.neptunes.Util.toJodaDateTime
import macro.dashboard.neptunes.backend.TurnUpdate
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created by Macro303 on 2019-Feb-25.
 */
object TurnTable : Table(name = "Game_Turns") {
	private val gameCol: Column<EntityID<Long>> = reference(
		name = "gameID",
		foreign = GameTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
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

	private val LOGGER = LogManager.getLogger(TurnTable::class.java)

	init {
		Util.query {
			uniqueIndex(gameCol, tickCol)
			SchemaUtils.create(this)
		}
	}

	fun select(ID: Long, tick: Int): Turn? = Util.query {
		select {
			gameCol eq ID and (tickCol eq tick)
		}.map {
			it.parse()
		}.sorted().firstOrNull()
	}

	fun search(ID: Long): List<Turn> = Util.query {
		select {
			gameCol eq ID
		}.map {
			it.parse()
		}.sorted()
	}

	fun insert(gameID: Long, update: TurnUpdate) = Util.query {
		try {
			insert {
				it[gameCol] = EntityID(id = gameID, table = GameTable)
				it[startTimeCol] = LocalDateTime.ofInstant(Instant.ofEpochMilli(update.startTime),ZoneId.systemDefault()).toJodaDateTime()
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
		} catch (esqle: ExposedSQLException) {
		}
	}

	private fun ResultRow.parse() = Turn(
		game = GameTable.select(ID = this[gameCol].value)!!,
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