package macro.dashboard.neptunes.team

import macro.dashboard.neptunes.GeneralException
import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*

/**
 * Created by Macro303 on 2019-Feb-11.
 */
object TeamTable : Table(name = "Team") {
	private val gameCol: Column<EntityID<Long>> = reference(
		name = "gameID",
		foreign = GameTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	val nameCol: Column<String> = text(name = "name")

	init {
		Util.query {
			uniqueIndex(gameCol, nameCol)
			SchemaUtils.create(this)
		}
	}

	fun search(gameID: Long? = null): List<Team> = Util.query {
		var temp = gameID
		if (temp == null)
			temp = GameTable.search().firstOrNull()?.ID ?: throw GeneralException()
		select {
			gameCol eq temp
		}.map {
			it.parse()
		}.filterNot {
			it.getPlayers().isEmpty()
		}.sorted()
	}

	fun select(gameID: Long? = null, name: String): Team? = Util.query {
		var temp = gameID
		if (temp == null)
			temp = GameTable.search().firstOrNull()?.ID ?: throw GeneralException()
		select {
			nameCol eq name and (gameCol eq temp)
		}.map {
			it.parse()
		}.sorted().firstOrNull()
	}

	fun insert(gameID: Long? = null, name: String): Team = Util.query {
		var temp = gameID
		if (temp == null)
			temp = GameTable.search().firstOrNull()?.ID ?: throw GeneralException()
		try {
			insert {
				it[gameCol] = EntityID(temp, GameTable)
				it[nameCol] = name
			}
			select(gameID = temp, name = name)!!
		} catch (esqle: ExposedSQLException) {
			select(gameID = temp, name = name)!!
		}
	}

	private fun ResultRow.parse(): Team = Team(
		gameID = this[gameCol].value,
		name = this[nameCol]
	)
}