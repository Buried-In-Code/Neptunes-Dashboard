package macro.dashboard.neptunes.game

import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.Util.toJavaDateTime
import macro.dashboard.neptunes.Util.toJodaDateTime
import macro.dashboard.neptunes.team.TeamTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

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

	private val LOGGER = LoggerFactory.getLogger(GameTable::class.java)

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

	fun insert(
		ID: Long,
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
		isTradeScanned: Boolean,
		tick: Int,
		tradeCost: Int,
		name: String,
		isTurnBased: Boolean,
		war: Int,
		turnTimeout: LocalDateTime,
		fleetPrice: Int? = null,
		gameType: String = "Triton"
	): Boolean = Util.query(description = "Insert Game") {
		try {
			insert {
				it[id] = EntityID(ID, GameTable)
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
				it[isTradeScannedCol] = isTradeScanned
				it[tickCol] = tick
				it[tradeCostCol] = tradeCost
				it[nameCol] = name
				it[isTurnBasedCol] = isTurnBased
				it[warCol] = war
				it[turnTimeoutCol] = turnTimeout.toJodaDateTime()
				it[fleetPriceCol] = fleetPrice
				it[gameTypeCol] = gameType
			}
			TeamTable.insert(gameID = ID, name = "Free For All")
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