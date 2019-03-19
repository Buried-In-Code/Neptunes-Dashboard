package macro.dashboard.neptunes

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import macro.dashboard.neptunes.Config.Companion.CONFIG
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.sql.Connection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Created by Macro303 on 2018-Nov-12.
 */
object Util {
	private val LOGGER = LogManager.getLogger()
	private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
	private val database = Database.connect(url = "jdbc:sqlite:${CONFIG.databaseFile}", driver = "org.sqlite.JDBC")
	internal val GSON = GsonBuilder()
		.serializeNulls()
		.disableHtmlEscaping()
		.create()
	val JAVA_FORMATTER: java.time.format.DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
	val JODA_FORMATTER: org.joda.time.format.DateTimeFormatter = DateTimeFormat.forPattern(DATE_FORMAT)

	internal fun <T> query(description: String, block: () -> T): T {
		val startTime = LocalDateTime.now()
		val transaction = transaction(
			transactionIsolation = Connection.TRANSACTION_SERIALIZABLE,
			repetitionAttempts = 1,
			db = database
		) {
			addLogger(Slf4jSqlDebugLogger)
			block()
		}
		LOGGER.debug("Took ${ChronoUnit.MILLIS.between(startTime, LocalDateTime.now())}ms to $description")
		return transaction
	}

	@Throws(JsonSyntaxException::class)
	internal fun String.JsonToMap(): Map<String, Any> {
		if (this.isBlank()) return emptyMap()
		val type = object : TypeToken<Map<String, Any>>() {
		}.type
		return GSON.fromJson(this, type) ?: emptyMap()
	}

	internal fun Any?.toJSON(): String = GSON.toJson(this)

	internal fun DateTime.toJavaDateTime(): LocalDateTime {
		val jodaString = this.toString(JODA_FORMATTER)
		return LocalDateTime.parse(jodaString, JAVA_FORMATTER)
	}

	internal fun LocalDateTime.toJodaDateTime(): DateTime {
		val javaString = this.format(JAVA_FORMATTER)
		return DateTime.parse(javaString, JODA_FORMATTER)
	}
}