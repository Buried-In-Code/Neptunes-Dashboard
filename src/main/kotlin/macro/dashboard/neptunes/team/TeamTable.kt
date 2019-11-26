package macro.dashboard.neptunes.team

import macro.dashboard.neptunes.StringIdTable
import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.game.GameTable
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import java.util.*

/**
 * Created by Macro303 on 2019-Feb-11.
 */
object TeamTable : StringIdTable(name = "Team") {
	val gameCol = reference(
		name = "gameId",
		foreign = GameTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	val nameCol = text(name = "name")

	private val LOGGER = LogManager.getLogger(TeamTable::class.java)

	init {
		Util.query(description = "Create Team table") {
			uniqueIndex(gameCol, nameCol)
			SchemaUtils.create(this)
		}
	}

	fun select(uuid: UUID): Team? = Util.query(description = "Select Team by UUID: $uuid") {
		select {
			id eq uuid.toString()
		}.limit(1).firstOrNull()?.parse()
	}

	fun select(gameId: Long, name: String): Team? =
		Util.query(description = "Select Team by GameId: $gameId, Name: $name") {
			select {
				gameCol eq gameId and (nameCol eq name)
			}.limit(1).firstOrNull()?.parse()
		}

	fun search(gameId: Long): List<Team> = Util.query(description = "Search Teams in Game: $gameId") {
		select {
			gameCol eq gameId
		}.map { it.parse() }
	}

	fun insert(item: Team): Boolean = Util.query(description = "Insert Team") {
		try {
			insert {
				it[id] = EntityID(item.uuid.toString(), TeamTable)
				it[gameCol] = EntityID(item.gameId, GameTable)
				it[nameCol] = item.name
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun update(item: Team): Boolean = Util.query(description = "Update Team") {
		try {
			update({ id eq item.uuid.toString() }) {
				it[nameCol] = item.name
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun delete(item: Team): Boolean = Util.query(description = "Delete Team") {
		try {
			deleteWhere { id eq item.uuid.toString() }
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	private fun ResultRow.parse(): Team = Team(
		uuid = UUID.fromString(this[id].value),
		gameId = this[gameCol].value,
		name = this[nameCol]
	)
}