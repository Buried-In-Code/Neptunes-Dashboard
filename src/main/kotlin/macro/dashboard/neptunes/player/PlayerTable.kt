package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.NotImplementedException
import macro.dashboard.neptunes.Table
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.team.TeamTable
import org.apache.logging.log4j.LogManager
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by Macro303 on 2019-Mar-19.
 */
object PlayerTable : Table<Player>(tableName = "Player") {
	private val LOGGER = LogManager.getLogger()

	init {
		if (!checkExists())
			createTable()
	}

	@Throws(SQLException::class)
	override fun parse(result: ResultSet): Player {
		val ID = result.getInt("id")
		val gameID = result.getLong("gameID")
		val teamID = result.getInt("teamID")
		val alias = result.getString("alias")
		val name = result.getString("name")
		return Player(
			ID = ID,
			gameID = gameID,
			teamID = teamID,
			alias = alias,
			name = name
		)
	}

	override fun createTable() {
		val query = "CREATE TABLE $tableName(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
				"gameID BIGINT NOT NULL REFERENCES ${GameTable.tableName}(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
				"teamID INTEGER NOT NULL REFERENCES ${TeamTable.tableName}(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
				"alias TEXT NOT NULL, " +
				"name TEXT, " +
				"UNIQUE(gameID, alias))"
		insert(query = query)
	}

	fun insert(
		gameID: Long,
		teamID: Int,
		alias: String,
		name: String? = null
	): Boolean {
		if (select(gameID = gameID, alias = alias) == null) {
			val query =
				"INSERT INTO $tableName(gameID, teamID, alias, name) VALUES(?, ?, ?, ?)"
			if (insert(gameID, teamID, alias, name, query = query)) {
				LOGGER.info("Added Player: $gameID - $alias")
				return true
			}
		}
		LOGGER.info("Player: $gameID - $alias already exists")
		return false
	}

	fun update(
		ID: Int,
		gameID: Long,
		teamID: Int,
		alias: String,
		name: String? = null
	): Boolean {
		if (select(ID = ID) != null) {
			val query =
				"UPDATE $tableName SET teamID = ?, name = ? WHERE ID = ?"
			if (update(teamID, name, ID, query = query)) {
				LOGGER.info("Updated Player: $gameID - $alias")
				return true
			}
		}
		LOGGER.info("Player: $gameID - $alias doesn't exist")
		return false
	}

	fun delete(ID: Int) {
		throw NotImplementedException(message = "This Functionality hasn't been implemented yet. Feel free to make a pull request and add it.")
	}

	fun select(ID: Int): Player? {
		val query = "SELECT * FROM $tableName WHERE id = ?"
		return search(ID, query = query).firstOrNull()
	}

	fun select(gameID: Long, alias: String): Player? {
		val query = "SELECT * FROM $tableName WHERE gameID = ? AND alias LIKE ?"
		return search(gameID, alias, query = query).firstOrNull()
	}

	fun searchByGame(gameID: Long): List<Player> {
		val query = "SELECT * FROM $tableName WHERE gameID = ?"
		return search(gameID, query = query)
	}

	fun searchByTeam(teamID: Int): List<Player> {
		val query = "SELECT * FROM $tableName WHERE teamID = ?"
		return search(teamID, query = query)
	}

	fun Player.update(
		teamID: Int = this.teamID,
		name: String? = this.name
	){
		PlayerTable.update(ID = this.ID, gameID = this.gameID, teamID = teamID, alias = this.alias, name = name)
	}
}