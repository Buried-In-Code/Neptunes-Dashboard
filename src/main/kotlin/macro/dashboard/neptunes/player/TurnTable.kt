package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.NotImplementedException
import macro.dashboard.neptunes.Table
import org.apache.logging.log4j.LogManager
import java.sql.ResultSet
import java.sql.SQLException

/**
 * Created by Macro303 on 2019-Mar-19.
 */
object TurnTable : Table<Turn>(tableName = "Turn") {
	private val LOGGER = LogManager.getLogger()

	init {
		if (!checkExists())
			createTable()
	}

	@Throws(SQLException::class)
	override fun parse(result: ResultSet): Turn {
		val ID = result.getInt("id")
		val playerID = result.getInt("playerID")
		val tick = result.getInt("tick")
		val economy = result.getInt("economy")
		val industry = result.getInt("industry")
		val science = result.getInt("science")
		val stars = result.getInt("stars")
		val fleet = result.getInt("fleet")
		val ships = result.getInt("ships")
		val isActive = result.getBoolean("isActive")
		return Turn(
			ID = ID,
			playerID = playerID,
			tick = tick,
			economy = economy,
			industry = industry,
			science = science,
			stars = stars,
			fleet = fleet,
			ships = ships,
			isActive = isActive
		)
	}

	override fun createTable() {
		val query = "CREATE TABLE $tableName(" +
				"id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE, " +
				"playerID INTEGER NOT NULL REFERENCES ${PlayerTable.tableName}(id) ON DELETE CASCADE ON UPDATE CASCADE, " +
				"tick INTEGER NOT NULL, " +
				"economy INTEGER NOT NULL, " +
				"industry INTEGER NOT NULL, " +
				"science INTEGER NOT NULL, " +
				"stars INTEGER NOT NULL, " +
				"fleet INTEGER NOT NULL, " +
				"ships INTEGER NOT NULL, " +
				"isActive BOOLEAN NOT NULL" +
				"UNIQUE(playerID, tick));"
		insert(query = query)
	}

	fun insert(
		playerID: Int,
		tick: Int,
		economy: Int,
		industry: Int,
		science: Int,
		stars: Int,
		fleet: Int,
		ships: Int,
		isActive: Boolean
	): Boolean {
		if (select(playerID = playerID, tick = tick) == null) {
			val query =
				"INSERT INTO $tableName(playerID, tick, economy, industry, science, stars, fleet, ships, isActive) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?);"
			if (insert(playerID, tick, economy, industry, science, stars, fleet, ships, isActive, query = query)) {
				LOGGER.info("Added Turn: $playerID - $tick")
				return true
			}
		}
		LOGGER.info("Turn: $playerID - $tick already exists")
		return false
	}

	fun update(
		ID: Int,
		playerID: Int,
		tick: Int,
		economy: Int,
		industry: Int,
		science: Int,
		stars: Int,
		fleet: Int,
		ships: Int,
		isActive: Boolean
	): Boolean {
		if (select(ID = ID) != null) {
			val query =
				"UPDATE $tableName SET economy = ?, industry = ?, science = ?, stars = ?, fleet = ?, ships = ?, isActive = ? WHERE ID = ?;"
			if (update(economy, industry, science, stars, fleet, ships, isActive, ID, query = query)) {
				LOGGER.info("Updated Turn: $playerID - $tick")
				return true
			}
		}
		LOGGER.info("Turn: $playerID - $tick doesn't exist")
		return false
	}

	fun delete(ID: Int) {
		throw NotImplementedException(message = "This Functionality hasn't been implemented yet. Feel free to make a pull request and add it.")
	}

	fun select(ID: Int): Turn? {
		val query = "SELECT * FROM $tableName WHERE id = ? LIMIT 1;"
		return search(ID, query = query).firstOrNull()
	}

	fun select(playerID: Int, tick: Int): Turn? {
		val query = "SELECT * FROM $tableName WHERE playerID = ? AND tick = ? LIMIT 1;"
		return search(playerID, tick, query = query).firstOrNull()
	}

	fun selectLatest(playerID: Int): Turn?{
		val query = "SELECT * FROM $tableName WHERE playerID = ? ORDER BY tick DESC LIMIT 1;"
		return search(playerID, query = query).firstOrNull()
	}

	fun searchByPlayer(playerID: Int): List<Turn> {
		val query = "SELECT * FROM $tableName WHERE playerID = ? ORDER BY tick DESC;"
		return search(playerID, query = query)
	}

	fun searchByTick(tick: Int): List<Turn> {
		val query = "SELECT * FROM $tableName WHERE tick = ? ORDER BY playerID ASC;"
		return search(tick, query = query)
	}
}