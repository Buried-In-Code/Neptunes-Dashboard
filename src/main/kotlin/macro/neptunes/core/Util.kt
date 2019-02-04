package macro.neptunes.core

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.ktor.request.ApplicationRequest
import io.ktor.request.httpMethod
import io.ktor.request.path
import macro.neptunes.data.Message
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.io.File
import java.sql.Connection
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by Macro303 on 2018-Nov-12.
 */
object Util {
	private val LOGGER = LogManager.getLogger(Util::class.java)
	private val GSON = GsonBuilder()
		.serializeNulls()
		.disableHtmlEscaping()
		.create()
	private val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
	val JAVA_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT)
	val JODA_FORMATTER = DateTimeFormat.forPattern(DATE_FORMAT)
	private val database = Database.connect(url = "jdbc:sqlite:neptunes-pride.db", driver = "org.sqlite.JDBC")

	@Throws(JsonSyntaxException::class)
	internal fun String.fromJSON(): Map<String, Any> {
		if (this.isBlank()) return emptyMap()
		val typeOfHashMap = object : TypeToken<Map<String, Any>>() {
		}.type
		return GSON.fromJson(this, typeOfHashMap)
	}

	internal fun Any?.toJSON(): String = GSON.toJson(this)

	internal fun DateTime.toJavaDateTime(): LocalDateTime{
		val jodaString = this.toString(JODA_FORMATTER)
		return LocalDateTime.parse(jodaString, JAVA_FORMATTER)
	}

	internal fun LocalDateTime.toJodaDateTime(): DateTime{
		val javaString = this.format(JAVA_FORMATTER)
		return DateTime.parse(javaString, JODA_FORMATTER)
	}

	fun notImplementedMessage(request: ApplicationRequest): Message {
		val message = Message(
			title = "Not Implemented: ${request.httpMethod.value} ${request.path()}",
			content = "This endpoint hasn't been implemented yet, feel free to make a pull request and add it."
		)
		return message
	}

	internal fun <T> query(block: () -> T): T {
		return transaction(
			transactionIsolation = Connection.TRANSACTION_SERIALIZABLE,
			repetitionAttempts = 1,
			db = database
		) {
			addLogger(Slf4jSqlDebugLogger)
			block()
		}
	}
}