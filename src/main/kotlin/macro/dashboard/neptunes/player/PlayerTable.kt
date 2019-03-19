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

	private val LOGGER = LogManager.getLogger()

	init {
		Util.query(description = "Create Player table") {
			uniqueIndex(gameCol, aliasCol)
			SchemaUtils.create(this)
		}
	}

	fun select(ID: Int): Player? = Util.query(description = "Select Player by ID: $ID") {
		select {
			id eq ID
		}.orderBy(gameCol to SortOrder.ASC, teamCol to SortOrder.ASC, aliasCol to SortOrder.ASC).limit(1).firstOrNull()
			?.parse()
	}

	fun select(gameID: Long, alias: String): Player? =
		Util.query(description = "Select Player by Game ID: $gameID and Alias: $alias") {
			select {
				gameCol eq gameID and (aliasCol like alias)
			}.orderBy(gameCol to SortOrder.ASC, teamCol to SortOrder.ASC, aliasCol to SortOrder.ASC).limit(1)
				.firstOrNull()?.parse()
		}

	fun searchByGame(gameID: Long): List<Player> = Util.query(description = "Search for Players from Game: $gameID") {
		select {
			gameCol eq gameID
		}.orderBy(gameCol to SortOrder.ASC, teamCol to SortOrder.ASC, aliasCol to SortOrder.ASC).map {
			it.parse()
		}
	}

	fun searchByTeam(teamID: Int): List<Player> = Util.query(description = "Search for Players in team: $teamID") {
		select {
			teamCol eq teamID
		}.orderBy(gameCol to SortOrder.ASC, teamCol to SortOrder.ASC, aliasCol to SortOrder.ASC).map {
			it.parse()
		}
	}

	fun insert(gameID: Long, update: PlayerUpdate): Boolean = Util.query(description = "Insert Player") {
		val teamID = TeamTable.select(gameID = gameID, name = "Free For All")?.ID
			?: throw GeneralException()
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

	fun update(ID: Int, teamID: Int, name: String?) = Util.query(description = "Update Player") {
		try {
			update({ id eq ID }) {
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
		PlayerTable.update(ID = this.ID, teamID = this.teamID, name = this.name)
	}
}