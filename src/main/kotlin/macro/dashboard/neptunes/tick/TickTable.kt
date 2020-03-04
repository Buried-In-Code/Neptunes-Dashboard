package macro.dashboard.neptunes.tick

import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.PlayerTable
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Created by Macro303 on 2019-Mar-04.
 */
internal object TickTable : LongIdTable(name = "Tick") {
	val gameCol = reference(
		name = "gameId",
		foreign = GameTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	val playerCol = reference(
		name = "playerId",
		foreign = PlayerTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	val tickCol = integer(name = "tick")

	val economyCol = integer(name = "economy")
	val industryCol = integer(name = "industry")
	val scienceCol = integer(name = "science")
	val starsCol = integer(name = "stars")
	val fleetsCol = integer(name = "fleets")
	val shipsCol = integer(name = "ships")
	val isActiveCol = bool(name = "isActive")
	val scanningCol = integer(name = "scanning")
	val propulsionCol = integer(name = "propulsion")
	val terraformingCol = integer(name = "terraforming")
	val researchCol = integer(name = "research")
	val weaponsCol = integer(name = "weapons")
	val bankingCol = integer(name = "banking")
	val manufacturingCol = integer(name = "manufacturing")

	private val LOGGER = LogManager.getLogger(TickTable::class.java)

	init {
		if (!exists())
			transaction(db = Util.database) {
				SchemaUtils.create(this@TickTable)
			}
	}
}