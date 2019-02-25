package macro.neptunes.game

import macro.neptunes.Util
import macro.neptunes.Util.toJavaDateTime
import macro.neptunes.Util.toJodaDateTime
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.joda.time.DateTime
import java.time.LocalDateTime

/**
 * Created by Macro303 on 2019-Feb-25.
 */
object GameTurnTable : Table(name = "Game Turns") {
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

	init {
		Util.query {
			uniqueIndex(gameCol, tickCol)
			SchemaUtils.create(this)
		}
	}

	fun selectLatest(ID: Long): GameTurn? = Util.query {
		search(ID = ID).firstOrNull()
	}

	fun select(ID: Long, tick: Int): GameTurn? = Util.query {
		select{
			gameCol eq ID and (tickCol eq tick)
		}.map{
			it.parse()
		}.firstOrNull()
	}

	fun search(ID: Long): List<GameTurn> = Util.query {
		select {
			gameCol eq ID
		}.map {
			it.parse()
		}.sorted()
	}

	fun insert(
		game: Game,
		startTime: LocalDateTime,
		production: Int,
		isGameOver: Boolean,
		isPaused: Boolean,
		isStarted: Boolean,
		productionCounter: Int,
		tick: Int,
		tickFragment: Int,
		tradeScanned: Int,
		war: Int
	): GameTurn = Util.query {
		try{
			insert{
				it[gameCol] = EntityID(id = game.ID, table = GameTable)
				it[startTimeCol] = startTime.toJodaDateTime()
				it[productionCol] = production
				it[isGameOverCol] = isGameOver
				it[isPausedCol] = isPaused
				it[isStartedCol] = isStarted
				it[productionCounterCol] = productionCounter
				it[tickCol] = tick
				it[tickFragmentCol] = tickFragment
				it[tradeScannedCol] = tradeScanned
				it[warCol] = war
			}
			select(ID = game.ID, tick = tick)!!
		}catch (esqle: ExposedSQLException){
			select(ID = game.ID, tick = tick)!!
		}
	}

	private fun ResultRow.parse() = GameTurn(
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
		war = this[warCol]
	)
}