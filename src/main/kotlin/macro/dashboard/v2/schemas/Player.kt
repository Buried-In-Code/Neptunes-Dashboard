package macro.dashboard.v2.schemas

import macro.dashboard.Utils
import macro.dashboard.v2.ISchema
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

/**
 * Created by Macro303 on 2018-Nov-08.
 */
class Player(id: EntityID<Long>) : LongEntity(id), ISchema {
	var username by PlayerTable.usernameCol
	var team by PlayerTable.teamCol

	var game by Game referencedOn PlayerTable.gameCol

	// Referenced Columns
	val turns by Turn referrersOn TurnTable.playerCol

	override fun toJson(vararg filterKeys: String): SortedMap<String, Any?> {
		val output = mapOf<String, Any?>(
			"username" to username,
			"team" to team,
		).filterKeys { !filterKeys.contains(it) }.toSortedMap()
		if (filterKeys.contains("turns").not())
			output["turns"] = turns.map { it.toJson(*filterKeys, "player") }
		return output
	}

	companion object : LongEntityClass<Player>(PlayerTable) {
		private val LOGGER = LogManager.getLogger()
	}
}

object PlayerTable : LongIdTable(name = "Players") {
	val gameCol = reference(
		name = "game_id",
		foreign = GameTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	val usernameCol = text("username")
	val teamCol = text("team").nullable()

	private val LOGGER = LogManager.getLogger()

	init {
		if (!exists())
			transaction(db = Utils.DATABASE) {
				uniqueIndex(gameCol, usernameCol)
				SchemaUtils.create(this@PlayerTable)
			}
	}
}