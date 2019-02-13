package macro.neptunes.team

import macro.neptunes.config.Config.Companion.CONFIG
import macro.neptunes.game.GameTable
import macro.neptunes.Util
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
	val nameCol: Column<String> = text(name = "name").uniqueIndex()

	init {
		Util.query {
			SchemaUtils.create(this)
		}
	}

	fun search(): List<Team> = Util.query {
		selectAll().map {
			it.parse()
		}.filterNot {
			it.getPlayers().isEmpty()
		}
	}

	fun select(name: String): Team? = Util.query {
		select {
			nameCol eq name
		}.map {
			it.parse()
		}.firstOrNull()
	}

	fun selectCreate(name: String): Team = Util.query {
		val team = select(name = name)
		if (team == null) {
			insert(name = name)
			select(name = name)!!
		} else
			team
	}

	fun insert(name: String): Boolean = Util.query {
		try {
			insert {
				it[gameCol] = EntityID(CONFIG.gameID, GameTable)
				it[nameCol] = name
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	private fun update(team: Team): Boolean = Util.query {
		try {
			update({ nameCol eq team.name }) {
				it[nameCol] = team.name
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	private fun ResultRow.parse(): Team = Team(
		gameID = this[gameCol].value,
		name = this[nameCol]
	)

	fun Team.update(name: String = this.name): Boolean {
		this.name = name
		return update(team = this)
	}
}