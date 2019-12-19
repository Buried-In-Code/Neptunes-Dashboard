package macro.dashboard.neptunes.team

import macro.dashboard.neptunes.game.GameTable
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.LongIdTable
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction

/**
 * Created by Macro303 on 2019-Feb-11.
 */
internal object TeamTable : LongIdTable(name = "Team") {
	val gameCol = reference(
		name = "gameId",
		foreign = GameTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	val nameCol = text(name = "name")

	private val LOGGER = LogManager.getLogger(TeamTable::class.java)

	init {
		if (!exists())
			transaction {
				uniqueIndex(gameCol, nameCol)
				SchemaUtils.create(this@TeamTable)
			}
	}
}