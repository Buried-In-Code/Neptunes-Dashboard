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
	private val gameCol: Column<EntityID<Long>> = reference(
		name = "gameID",
		foreign = GameTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	private val nameCol: Column<String> = text(name = "name")

	private val LOGGER = LogManager.getLogger(TeamTable::class.java)

	init {
		Util.query {
			uniqueIndex(gameCol, nameCol)
			SchemaUtils.create(this)
		}
	}

	fun select(ID: Int): Team? = Util.query {
		select {
			id eq ID
		}.map {
			it.parse()
		}.sorted().firstOrNull()
	}

	fun search(gameID: Long? = null, name: String = "%"): List<Team> = Util.query {
		val temp = gameID ?: GameTable.search().firstOrNull()?.ID ?: throw GeneralException()
		select{
			gameCol eq temp and(nameCol like name)
		}.map {
			it.parse()
		}.sorted()
	}

	fun insert(gameID: Long?, name: String): Boolean = Util.query {
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

	fun update(ID: Int, name: String): Boolean = Util.query {
		try {
			update({id eq ID}) {
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
		TeamTable.update(ID = this.ID, name = this.name)
	}
}