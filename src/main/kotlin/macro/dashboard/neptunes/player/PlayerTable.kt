package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.GeneralException
import macro.dashboard.neptunes.Util
import macro.dashboard.neptunes.backend.PlayerUpdate
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.team.TeamTable
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.exceptions.ExposedSQLException
import org.jetbrains.exposed.sql.*

/**
 * Created by Macro303 on 2019-Feb-11.
 */
object PlayerTable : IntIdTable(name = "Player") {
	private val gameCol: Column<EntityID<Long>> = reference(
		name = "gameID",
		foreign = GameTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	private val teamCol: Column<EntityID<Int>> = reference(
		name = "teamID",
		foreign = TeamTable,
		onUpdate = ReferenceOption.CASCADE,
		onDelete = ReferenceOption.CASCADE
	)
	private val aliasCol: Column<String> = text(name = "alias")
	private val nameCol: Column<String?> = text(name = "name").nullable()

	private val LOGGER = LogManager.getLogger(PlayerTable::class.java)

	init {
		Util.query {
			uniqueIndex(gameCol, aliasCol)
			SchemaUtils.create(this)
		}
	}

	fun select(ID: Int): Player? = Util.query {
		select {
			id eq ID
		}.map {
			it.parse()
		}.sorted().firstOrNull()
	}

	fun search(gameID: Long? = null, alias: String = ""): List<Player> = Util.query {
		val temp = gameID ?: GameTable.search().firstOrNull()?.ID ?: throw GeneralException()
		select {
			gameCol eq temp and (aliasCol like alias)
		}.map {
			it.parse()
		}.sorted()
	}

	fun searchByTeam(teamID: Int): List<Player> = Util.query {
		select {
			teamCol eq teamID
		}.map {
			it.parse()
		}.sorted()
	}

	fun insert(gameID: Long, update: PlayerUpdate): Boolean = Util.query {
		val teamID = TeamTable.search(gameID = gameID, name = "Free For All").firstOrNull()?.ID ?: throw GeneralException()
		try {
			insert {
				it[gameCol] = EntityID(id = gameID, table = GameTable)
				it[aliasCol] = update.alias
				it[teamCol] = EntityID(id = teamID, table = TeamTable)
			}
			true
		} catch (esqle: ExposedSQLException) {
			false
		}
	}

	fun update(gameID: Long, alias: String, teamID: Int, name: String?) = Util.query {
		try {
			update({ aliasCol like alias and (gameCol eq gameID) }) {
				it[teamCol] = EntityID(id = teamID, table = TeamTable)
				it[nameCol] = name
			}
		} catch (esqle: ExposedSQLException) {
		}
	}

	private fun ResultRow.parse(): Player = Player(
		ID = this[id].value,
		gameID = this[gameCol].value,
		teamID = this[teamCol].value,
		name = this[nameCol],
		alias = this[aliasCol]
	)

	fun Player.update(
		teamID: Int = this.teamID,
		name: String? = this.name
	) {
		this.teamID = teamID
		this.name = name
		PlayerTable.update(gameID = this.gameID, alias = this.alias, teamID = this.teamID, name = this.name)
	}
}