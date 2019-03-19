package macro.dashboard.neptunes

import macro.dashboard.neptunes.Config.Companion.CONFIG
import org.apache.logging.log4j.LogManager
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.util.*

/**
 * Created by Macro303 on 2019-Mar-19.
 */
abstract class Table<T> protected constructor(val tableName: String) {
	private val LOGGER = LogManager.getLogger()
	private val DATABASE_URL = "jdbc:sqlite:${CONFIG.databaseFile}"

	protected abstract fun parse(result: ResultSet): T
	protected abstract fun createTable()

	fun checkExists(): Boolean {
		val query = "SELECT name FROM sqlite_master WHERE type = ? AND name = ?"
		val values = arrayOf("table", tableName)
		LOGGER.trace("Query: $query, Values: ${Arrays.toString(values)}")
		try {
			DriverManager.getConnection(DATABASE_URL).use { connection ->
				connection.prepareStatement(query).use { statement ->
					values.forEachIndexed { index, value ->
						statement.setObject(index + 1, value)
					}
					val results = statement.executeQuery()
					return results.next()
				}
			}
		} catch (sqle: SQLException) {
			LOGGER.debug("Check Exists Error | Query: $query, Values: ${Arrays.toString(values)}", sqle)
		}
		return false
	}

	fun search(vararg values: Any?, query: String): List<T> {
		LOGGER.trace("Query: $query, Values: ${Arrays.toString(values)}")
		val items = ArrayList<T>()
		try {
			DriverManager.getConnection(DATABASE_URL).use { connection ->
				connection.prepareStatement(query).use { statement ->
					values.forEachIndexed { index, value ->
						statement.setObject(index + 1, value)
					}
					val result = statement.executeQuery()
					while (result.next()) {
						val item = parse(result = result)
						items.add(item)
					}
				}
			}
		} catch (sqle: SQLException) {
			LOGGER.debug("Search Error | Query: $query, Values: ${Arrays.toString(values)}", sqle)
			items.clear()
		}
		return items
	}

	fun searchAll(): List<T> {
		val query = "SELECT * FROM $tableName"
		return search(query = query)
	}

	protected fun insert(vararg values: Any?, query: String): Boolean = update(values = *values, query = query)

	protected fun update(vararg values: Any?, query: String): Boolean {
		LOGGER.trace("Query: $query, Values: ${Arrays.toString(values)}")
		try {
			DriverManager.getConnection(DATABASE_URL).use { connection ->
				connection.autoCommit = false
				try {
					connection.prepareStatement(query).use { statement ->
						values.forEachIndexed { index, value ->
							LOGGER.debug("Index: $index")
							LOGGER.debug("Value: $value")
							statement.setObject(index + 1, value)
						}
						statement.executeUpdate()
						connection.commit()
						return true
					}
				} catch (sqle: SQLException) {
					connection.rollback()
					throw sqle
				}
			}
		} catch (sqle: SQLException) {
			LOGGER.debug("Update Error | Query: $query, Values: ${Arrays.toString(values)}", sqle)
		}
		return false
	}

	protected fun delete(vararg values: Any?, query: String): Boolean = update(values = *values, query = query)
}