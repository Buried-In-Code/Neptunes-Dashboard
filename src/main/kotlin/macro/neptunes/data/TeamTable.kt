package macro.neptunes.data

import macro.neptunes.core.Game
import macro.neptunes.core.Team
import macro.neptunes.core.Util
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

/**
 * Created by Macro303 on 2019-Feb-11.
 */
object TeamTable : IntIdTable(name = "Team") {
	private val gameCol: Column<EntityID<Long>> = reference(
		name = "game",
		foreign = GameTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	private val nameCol: Column<String> = text(name = "name")

	init {
		Util.query {
			SchemaUtils.create(this)
		}
	}

	fun search(): List<Team> = Util.query {
		selectAll().map {
			it.parse()
		}
	}

	fun search(name: String): List<Team> = Util.query {
		select {
			nameCol eq name
		}.map {
			it.parse()
		}
	}

	fun select(ID: Int): Team? = Util.query {
		select {
			id eq ID
		}.map {
			it.parse()
		}.firstOrNull()
	}

	fun select(game: Game, name: String): Team? = Util.query {
		select {
			gameCol eq game.ID and (nameCol eq name)
		}.map {
			it.parse()
		}.firstOrNull()
	}

	fun insert(game: Game, name: String): Team = Util.query {
		val team = select(game = game, name = name)
		if (team == null) {
			insert {
				it[gameCol] = EntityID(game.ID, GameTable)
				it[nameCol] = name
			}
			select(game = game, name = name)!!
		} else
			team
	}

	private fun ResultRow.parse() = Team(
		ID = this[id].value,
		game = GameTable.select(ID = this[gameCol].value)!!,
		name = this[nameCol]
	)
}