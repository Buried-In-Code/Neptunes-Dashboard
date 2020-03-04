package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.game.GameTable
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.id.LongIdTable
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
	val teamCol = text(name = "team").nullable()
	val nameCol = text(name = "name").nullable()

	private val LOGGER = LogManager.getLogger(PlayerTable::class.java)

	init {
		if (!exists())
			transaction(db = Util.database) {
				SchemaUtils.create(this@PlayerTable)
			}
	}
}