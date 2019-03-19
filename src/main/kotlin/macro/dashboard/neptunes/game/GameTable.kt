package macro.dashboard.neptunes.game

import macro.dashboard.neptunes.NotImplementedException
import macro.dashboard.neptunes.Table
import org.apache.logging.log4j.LogManager
import java.sql.ResultSet
import java.sql.SQLException
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

/**
 * Created by Macro303 on 2019-Mar-19.
 */
object GameTable : Table<Game>(tableName = "Game") {
	private val LOGGER = LogManager.getLogger()

	init {
		if (!checkExists())
			createTable()
	}

	@Throws(SQLException::class)
	override fun parse(result: ResultSet): Game {
		val ID = result.getLong("id")
		val name = result.getString("name")
		val totalStars = result.getInt("totalStars")
		val victoryStars = result.getInt("victoryStars")
		val admin = result.getInt("admin")
		val fleetSpeed = result.getDouble("fleetSpeed")
		val isTurnBased = result.getBoolean("isTurnBased")
		val productionRate = result.getInt("productionRate")
		val tickRate = result.getInt("tickRate")
		val tradeCost = result.getInt("tradeCost")
		val startTime =
			LocalDateTime.ofInstant(Instant.ofEpochMilli(result.getLong("startTime")), ZoneId.systemDefault())
		val production = result.getInt("production")
		val isGameOver = result.getBoolean("isGameOver")
		val isPaused = result.getBoolean("isPaused")
		val isStarted = result.getBoolean("isStarted")
		val productionCounter = result.getInt("productionCounter")
		val tick = result.getInt("tick")
		val tickFragment = result.getInt("tickFragment")
		val tradeScanned = result.getInt("tradeScanned")
		val war = result.getInt("war")
		val turnBasedTimeout = result.getLong("turnBasedTimeout")
		return Game(
			ID = ID,
			name = name,
			totalStars = totalStars,
			victoryStars = victoryStars,
			admin = admin,
			fleetSpeed = fleetSpeed,
			isTurnBased = isTurnBased,
			productionRate = productionRate,
			tickRate = tickRate,
			tradeCost = tradeCost,
			startTime = startTime,
			production = production,
			isGameOver = isGameOver,
			isPaused = isPaused,
			isStarted = isStarted,
			productionCounter = productionCounter,
			tick = tick,
			tickFragment = tickFragment,
			tradeScanned = tradeScanned,
			war = war,
			turnBasedTimeout = turnBasedTimeout
		)
	}

	override fun createTable() {
		val query = "CREATE TABLE $tableName(" +
				"id BIGINT PRIMARY KEY NOT NULL UNIQUE, " +
				"name TEXT NOT NULL, " +
				"totalStars INTEGER NOT NULL, " +
				"victoryStars INTEGER NOT NULL, " +
				"admin INTEGER NOT NULL, " +
				"fleetSpeed DOUBLE PRECISION NOT NULL, " +
				"isTurnBased BOOLEAN NOT NULL, " +
				"productionRate INTEGER NOT NULL, " +
				"tickRate INTEGER NOT NULL, " +
				"tradeCost INTEGER NOT NULL, " +
				"startTime TEXT NOT NULL, " +
				"production INTEGER NOT NULL, " +
				"isGameOver BOOLEAN NOT NULL, " +
				"isPaused BOOLEAN NOT NULL, " +
				"isStarted BOOLEAN NOT NULL, " +
				"productionCounter INTEGER NOT NULL, " +
				"tick INTEGER NOT NULL, " +
				"tickFragment INTEGER NOT NULL, " +
				"tradeScanned INTEGER NOT NULL, " +
				"war INTEGER NOT NULL, " +
				"turnBasedTimeout BIGINT NOT NULL)"
		insert(query = query)
	}

	fun insert(
		ID: Long,
		name: String,
		totalStars: Int,
		victoryStars: Int,
		admin: Int,
		fleetSpeed: Double,
		isTurnBased: Boolean,
		productionRate: Int,
		tickRate: Int,
		tradeCost: Int,
		startTime: LocalDateTime,
		production: Int,
		isGameOver: Boolean,
		isPaused: Boolean,
		isStarted: Boolean,
		productionCounter: Int,
		tick: Int,
		tickFragment: Int,
		tradeScanned: Int,
		war: Int,
		turnBasedTimeout: Long
	): Boolean {
		if (select(ID = ID) == null) {
			val query =
				"INSERT INTO $tableName(id, name, totalStars, victoryStars, admin, fleetSpeed, isTurnBased, productionRate, tickRate, tradeCost, startTime, production, isGameOver, isPaused, isStarted, productionCounter, tick, tickFragment, tradeScanned, war, turnBasedTimeout) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
			if (insert(
					ID,
					name,
					totalStars,
					victoryStars,
					admin,
					fleetSpeed,
					isTurnBased,
					productionRate,
					tickRate,
					tradeCost,
					startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
					production,
					isGameOver,
					isPaused,
					isStarted,
					productionCounter,
					tick,
					tickFragment,
					tradeScanned,
					war,
					turnBasedTimeout,
					query = query
				)
			) {
				LOGGER.info("Added Game: $ID - $name")
				return true
			}
		}
		LOGGER.info("Game: $ID already exists")
		return false
	}

	fun update(
		ID: Long,
		name: String,
		totalStars: Int,
		victoryStars: Int,
		admin: Int,
		fleetSpeed: Double,
		isTurnBased: Boolean,
		productionRate: Int,
		tickRate: Int,
		tradeCost: Int,
		startTime: LocalDateTime,
		production: Int,
		isGameOver: Boolean,
		isPaused: Boolean,
		isStarted: Boolean,
		productionCounter: Int,
		tick: Int,
		tickFragment: Int,
		tradeScanned: Int,
		war: Int,
		turnBasedTimeout: Long
	): Boolean {
		if (select(ID = ID) != null) {
			val query =
				"UPDATE $tableName SET name = ?, totalStars = ?, victoryStars = ?, admin = ?, fleetSpeed = ?, isTurnBased = ?, productionRate = ?, tickRate = ?, tradeCost = ?, startTime = ?, production = ?, isGameOver = ?, isPaused = ?, isStarted = ?, productionCounter = ?, tick = ?, tickFragment = ?, tradeScanned = ?, war = ?, turnBasedTimeout = ? WHERE ID = ?"
			if (update(
					name,
					totalStars,
					victoryStars,
					admin,
					fleetSpeed,
					isTurnBased,
					productionRate,
					tickRate,
					tradeCost,
					startTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
					production,
					isGameOver,
					isPaused,
					isStarted,
					productionCounter,
					tick,
					tickFragment,
					tradeScanned,
					war,
					turnBasedTimeout,
					ID,
					query = query
				)
			) {
				LOGGER.info("Updated Game: $ID - $name")
				return true
			}
		}
		LOGGER.info("Game: $ID doesn't exist")
		return false
	}

	fun delete(ID: Long) {
		throw NotImplementedException(message = "This Functionality hasn't been implemented yet. Feel free to make a pull request and add it.")
	}

	fun select(ID: Long): Game? {
		val query = "SELECT * FROM $tableName WHERE id = ?"
		return search(ID, query = query).firstOrNull()
	}

	fun search(name: String = ""): List<Game> {
		val query = "SELECT * FROM $tableName WHERE name LIKE ?"
		return search("%$name%", query = query)
	}
}