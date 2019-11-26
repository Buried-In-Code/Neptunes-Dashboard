package macro.dashboard.neptunes.game

import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.Util.toJavaDateTime
import macro.dashboard.neptunes.Util.toJodaDateTime
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*

/**
 * Created by Macro303 on 2019-Feb-11.
 */
internal object GameTable : LongIdTable(name = "Game") {
	val fleetSpeedCol = double(name = "fleetSpeed")
	val isPausedCol = bool(name = "isPaused")
	val productionsCol = integer(name = "productions")
	val tickFragmentCol = integer(name = "tickFragment")
	val tickRateCol = integer(name = "tickRate")
	val productionRateCol = integer(name = "productionRate")
	val victoryStarsCol = integer(name = "victoryStars")
	val isGameOverCol = bool(name = "isGameOver")
	val isStartedCol = bool(name = "isStarted")
	val startTimeCol = datetime(name = "startTime")
	val totalStarsCol = integer(name = "totalStars")
	val productionCounterCol = integer(name = "productionCounter")
	val isTradeScannedCol = bool(name = "isTradeScanned")
	val tickCol = integer(name = "tick")
	val tradeCostCol = integer(name = "tradeCost")
	val nameCol = text(name = "name")
	val isTurnBasedCol = bool(name = "isTurnBased")
	val warCol = integer(name = "war")
	val turnTimeoutCol = datetime(name = "turnBasedTimeout")

	val fleetPriceCol = integer(name = "fleetPrice").nullable()

	val gameTypeCol = text(name = "gameType").default("Triton")

	private val LOGGER = LogManager.getLogger(GameTable::class.java)

	init {
		Util.query(description = "Create Game table") {
			SchemaUtils.create(this)
		}
	}

	fun select(gameId: Long): Game? = Util.query(description = "Select Game by GameId: $gameId") {
		select {
			id eq gameId
		}.limit(1).firstOrNull()?.parse()
	}

	fun insert(item: Game): Boolean = Util.query(description = "Insert Game") {
		try {
			insert {
				it[id] = EntityID(item.ID, GameTable)
				it[fleetSpeedCol] = item.fleetSpeed
				it[isPausedCol] = item.isPaused
				it[productionsCol] = item.productions
				it[tickFragmentCol] = item.tickFragment
				it[tickRateCol] = item.tickRate
				it[productionRateCol] = item.productionRate
				it[victoryStarsCol] = item.victoryStars
				it[isGameOverCol] = item.isGameOver
				it[isStartedCol] = item.isStarted
				it[startTimeCol] = item.startTime.toJodaDateTime()
				it[totalStarsCol] = item.totalStars
				it[productionCounterCol] = item.productionCounter
				it[isTradeScannedCol] = item.isTradeScanned
				it[tickCol] = item.tick
				it[tradeCostCol] = item.tradeCost
				it[nameCol] = item.name
				it[isTurnBasedCol] = item.isTurnBased
				it[warCol] = item.war
				it[turnTimeoutCol] = item.turnTimeout.toJodaDateTime()
				it[fleetPriceCol] = item.fleetPrice
				it[gameTypeCol] = item.gameType
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun update(item: Game): Boolean = Util.query(description = "Update Game") {
		try {
			update({ id eq item.ID }) {
				it[isPausedCol] = item.isPaused
				it[productionsCol] = item.productions
				it[tickFragmentCol] = item.tickFragment
				it[isGameOverCol] = item.isGameOver
				it[isStartedCol] = item.isStarted
				it[startTimeCol] = item.startTime.toJodaDateTime()
				it[productionCounterCol] = item.productionCounter
				it[tickCol] = item.tick
				it[warCol] = item.war
				it[turnTimeoutCol] = item.turnTimeout.toJodaDateTime()
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun delete(item: Game): Boolean = Util.query(description = "Delete Game") {
		try {
			deleteWhere { id eq item.ID }
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	private fun ResultRow.parse() = Game(
		ID = this[id].value,
		fleetSpeed = this[fleetSpeedCol],
		isPaused = this[isPausedCol],
		productions = this[productionsCol],
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
		turnTimeout = this[turnTimeoutCol].toJavaDateTime(),
		fleetPrice = this[fleetPriceCol],
		gameType = this[gameTypeCol]
	)
}