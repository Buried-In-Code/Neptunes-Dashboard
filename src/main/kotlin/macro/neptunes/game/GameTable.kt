package macro.neptunes.game

import macro.neptunes.Util
import macro.neptunes.Util.toJavaDateTime
import macro.neptunes.Util.toJodaDateTime
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import java.time.LocalDateTime

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

	fun select(): Game? = Util.query {
		selectAll().map {
			it.parse()
		}.firstOrNull()
	}

	fun insert(game: Game): Boolean = Util.query {
		try {
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
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun update(game: Game): Boolean = Util.query {
		try {
			update({ id eq game.ID }) {
				it[productionsCol] = game.productions
				it[lastUpdatedCol] = LocalDateTime.now().toJodaDateTime()
				it[isGameOverCol] = game.isGameOver
				it[isPausedCol] = game.isPaused
				it[isStartedCol] = game.isStarted
				it[tickCol] = game.tick
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
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

	fun Game.update(
		productions: Int = this.productions,
		isGameOver: Boolean = this.isGameOver,
		isPaused: Boolean = this.isPaused,
		isStarted: Boolean = this.isStarted,
		tick: Int = this.tick
	): Boolean {
		this.productions = productions
		this.isGameOver = isGameOver
		this.isPaused = isPaused
		this.isStarted = isStarted
		this.tick = tick
		return update(game = this)
	}
}