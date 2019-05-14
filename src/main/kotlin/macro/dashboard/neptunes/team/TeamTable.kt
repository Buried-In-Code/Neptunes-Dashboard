package macro.dashboard.neptunes.team

import macro.dashboard.neptunes.Config.Companion.CONFIG
import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.game.GameTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2019-Feb-11.
 */
object TeamTable : IntIdTable(name = "Team") {
	val gameCol = reference(
		name = "gameID",
		foreign = GameTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	val nameCol = text(name = "name")

	private val LOGGER = LoggerFactory.getLogger(this::class.java)

	init {
		Util.query(description = "Create Team table") {
			uniqueIndex(gameCol, nameCol)
			SchemaUtils.create(this)
		}
	}

	fun select(ID: Int): Team? = Util.query(description = "Select Team by ID") {
		select {
			id eq ID
		}.orderBy(nameCol to SortOrder.ASC).limit(n = 1).firstOrNull()?.parse()
	}

	fun search(name: String): List<Team> = Util.query(description = "Search for Teams by Name => $name") {
		TeamTable.select {
			nameCol like name
		}.orderBy(nameCol to SortOrder.ASC).map {
			it.parse()
		}
	}

	fun insert(gameID: Long?, name: String): Boolean = Util.query(description = "Insert Team") {
		val temp = gameID ?: CONFIG.gameID
		try {
			insert {
				it[gameCol] = EntityID(temp, GameTable)
				it[nameCol] = name
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun update(ID: Int, name: String): Boolean = Util.query(description = "Update Team") {
		try {
			update({ id eq ID }) {
				it[nameCol] = name
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	private fun ResultRow.parse(): Team = Team(
		ID = this[id].value,
		gameID = this[gameCol].value,
		name = this[nameCol]
	)
}