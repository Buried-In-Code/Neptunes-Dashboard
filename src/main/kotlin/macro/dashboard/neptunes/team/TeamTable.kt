package macro.dashboard.neptunes.team

import macro.dashboard.neptunes.GeneralException
import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.game.GameTable
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*

/**
 * Created by Macro303 on 2019-Feb-11.
 */
object TeamTable : IntIdTable(name = "Team") {
	private val gameCol = reference(
		name = "gameID",
		foreign = GameTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	private val nameCol = text(name = "name")

	private val LOGGER = LogManager.getLogger()

	init {
		Util.query(description = "Create Team table") {
			uniqueIndex(gameCol, nameCol)
			SchemaUtils.create(this)
		}
	}

	fun select(ID: Int): Team? = Util.query(description = "Select Team by ID") {
		select {
			id eq ID
		}.orderBy(gameCol to SortOrder.ASC, nameCol to SortOrder.ASC).limit(n = 1).firstOrNull()?.parse()
	}

	fun select(gameID: Long, name: String): Team? =
		Util.query(description = "Select Team by Game: $gameID and Name: $name") {
			select {
				gameCol eq gameID and (nameCol like name)
			}.orderBy(gameCol to SortOrder.ASC, nameCol to SortOrder.ASC).limit(n = 1).firstOrNull()?.parse()
		}

	fun searchByGame(gameID: Long): List<Team> =
		Util.query(description = "Search for Teams from Game: $gameID") {
			select {
				gameCol eq gameID
			}.orderBy(gameCol to SortOrder.ASC, nameCol to SortOrder.ASC).map {
				it.parse()
			}
		}

	fun insert(gameID: Long?, name: String): Boolean = Util.query(description = "Insert Team") {
		val temp = gameID ?: GameTable.search().firstOrNull()?.ID ?: throw GeneralException()
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

	fun Team.update(
		name: String = this.name
	) {
		this.name = name
		TeamTable.update(ID = this.ID, name = name)
	}
}