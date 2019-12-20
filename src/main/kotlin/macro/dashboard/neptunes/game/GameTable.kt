package macro.dashboard.neptunes.game

import macro.dashboard.neptunes.Util
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Created by Macro303 on 2019-Feb-11.
 */
internal object GameTable : LongIdTable(name = "Game") {
	val codeCol = text(name = "code")
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
		if (!exists())
			transaction(db = Util.database) {
				SchemaUtils.create(this@GameTable)
			}
	}
}