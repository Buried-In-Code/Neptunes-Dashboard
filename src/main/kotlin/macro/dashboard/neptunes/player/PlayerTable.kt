package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.team.TeamTable
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
internal object PlayerTable : LongIdTable(name = "Player") {
	val gameCol = reference(
		name = "gameId",
		foreign = GameTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	val aliasCol = text(name = "alias")
	val teamCol = reference(
		name = "teamId",
		foreign = TeamTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	val nameCol = text(name = "name").nullable()

	private val LOGGER = LogManager.getLogger(PlayerTable::class.java)

	init {
		if (!exists())
			transaction {
				uniqueIndex(gameCol, aliasCol)
				SchemaUtils.create(this@PlayerTable)
			}
	}
}