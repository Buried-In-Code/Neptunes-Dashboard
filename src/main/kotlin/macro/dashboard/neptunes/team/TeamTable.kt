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

	fun search(game: Game? = null): List<Team> = Util.query {
		var temp = game
		if (temp == null)
			temp = GameTable.search().firstOrNull() ?: throw GeneralException()
		select {
			gameCol eq temp.ID
		}.map {
			it.parse()
		}.filterNot {
			it.getPlayers().isEmpty()
		}.sorted()
	}

	fun select(game: Game? = null, name: String): Team? = Util.query {
		var temp = game
		if (temp == null)
			temp = GameTable.search().firstOrNull() ?: throw GeneralException()
		select {
			nameCol eq name and (gameCol eq temp.ID)
		}.map {
			it.parse()
		}.sorted().firstOrNull()
	}

	fun count(game: Game? = null): Int = Util.query {
		var temp = game
		if (temp == null)
			temp = GameTable.search().firstOrNull() ?: throw GeneralException()
		select {
			gameCol eq temp.ID
		}.count()
	}

	fun insert(game: Game? = null, name: String): Team = Util.query {
		var temp = game
		if (temp == null)
			temp = GameTable.search().firstOrNull() ?: throw GeneralException()
		try {
			insert {
				it[gameCol] = EntityID(temp.ID, GameTable)
				it[nameCol] = name
			}
			select(game = game, name = name)!!
		} catch (esqle: ExposedSQLException) {
			select(game = game, name = name)!!
		}
	}

	private fun ResultRow.parse(): Team = Team(
		gameID = this[gameCol].value,
		name = this[nameCol]
	)
}