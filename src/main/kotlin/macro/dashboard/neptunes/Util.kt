package macro.dashboard.neptunes

import com.google.gson.GsonBuilder
import com.mashape.unirest.http.ObjectMapper
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.exceptions.UnirestException
import macro.dashboard.neptunes.config.Config.Companion.CONFIG
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.sql.Connection
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

/**
 * Created by Macro303 on 2018-Nov-12.
 */
object Util {
	private val LOGGER = LoggerFactory.getLogger(Util::class.java)
	const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
	internal const val SQLITE_DATABASE = "Neptunes-Dashboard.sqlite"
	private val database = Database.connect(url = "jdbc:sqlite:$SQLITE_DATABASE", driver = "org.sqlite.JDBC")
	internal val GSON = GsonBuilder()
		.serializeNulls()
		.disableHtmlEscaping()
		.create()
	internal val JAVA_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT)
	internal val JODA_FORMATTER = DateTimeFormat.forPattern(DATE_FORMAT)
	internal val HEADERS = mapOf(
		"Accept" to "application/json; charset=UTF-8",
		"Content-Type" to "application/json; charset=UTF-8",
		"User-Agent" to "Neptune's Dashboard"
	)

	init {
		if (CONFIG.proxy.getHttpHost() != null)
			Unirest.setProxy(CONFIG.proxy.getHttpHost()!!)
		Unirest.setObjectMapper(object : ObjectMapper {
			override fun <T> readValue(value: String, valueType: Class<T>): T {
				return GSON.fromJson(value, valueType)
			}

			override fun writeValue(value: Any): String {
				return GSON.toJson(value)
			}
		})
	}

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

	internal fun DateTime.toJavaDateTime(): LocalDateTime =
		LocalDateTime.parse(this.toString(JODA_FORMATTER), JAVA_FORMATTER)

	internal fun LocalDateTime.toJodaDateTime(): DateTime =
		DateTime.parse(this.format(JAVA_FORMATTER), JODA_FORMATTER)

	private fun clean(url: String): String {
		val regex = "^.+access_key=(.*?)&.+$".toRegex()
		val items = regex.matchEntire(url)?.groupValues ?: emptyList()
		if (items.size == 2)
			return items[0].replace(items[1], "<HIDDEN>")
		return url
	}

	internal fun postRequest(url: String, gameID: Long, code: String): String? {
		return try {
			LOGGER.debug("${"POST".padEnd(4)}: >>> ${clean(url)}")
			val boundary = "===${System.currentTimeMillis()}==="
			val request = Unirest.post(url)
				.header("content-type", "multipart/form-data; boundary=$boundary")
				.header("User-Agent", "Neptune's Dashboard")
				.body("--$boundary\r\nContent-Disposition: form-data; name=\"api_version\"\r\n\r\n0.1\r\n--$boundary\r\nContent-Disposition: form-data; name=\"game_number\"\r\n\r\n$gameID\r\n--$boundary\r\nContent-Disposition: form-data; name=\"code\"\r\n\r\n$code\r\n--$boundary--")
			val response = request.asString()
			when {
				response.status < 100 -> LOGGER.error("${"POST".padEnd(4)}: ${response.status} ${response.statusText} - ${clean(url)}")
				response.status < 200 -> LOGGER.info("${"POST".padEnd(4)}: ${response.status} ${response.statusText} - ${clean(url)}")
				response.status < 300 -> LOGGER.info("${"POST".padEnd(4)}: ${response.status} ${response.statusText} - ${clean(url)}")
				response.status < 400 -> LOGGER.warn("${"POST".padEnd(4)}: ${response.status} ${response.statusText} - ${clean(url)}")
				response.status < 500 -> LOGGER.warn("${"POST".padEnd(4)}: ${response.status} ${response.statusText} - ${clean(url)}")
				else -> LOGGER.error("${"POST".padEnd(4)}: ${response.status} ${response.statusText} - ${clean(url)}")
			}
			if (response.status != 200) null else response.body
		} catch (ue: UnirestException) {
			LOGGER.error("Unable to retrieve URL: ${ue.localizedMessage}")
			null
		}
	}

	internal fun getRequest(url: String, headers: Map<String, String> = HEADERS): String? {
		return try {
			LOGGER.debug("${"GET".padEnd(4)}: >>> - ${clean(url)} - $headers")
			var request = Unirest.get(url)
			headers.forEach { (key, value) ->
				request = request.header(key, value)
			}
			val response = request.asString()
			when {
				response.status < 100 -> LOGGER.error("${"GET".padEnd(4)}: ${response.status} ${response.statusText} - ${clean(url)}")
				response.status < 200 -> LOGGER.info("${"GET".padEnd(4)}: ${response.status} ${response.statusText} - ${clean(url)}")
				response.status < 300 -> LOGGER.info("${"GET".padEnd(4)}: ${response.status} ${response.statusText} - ${clean(url)}")
				response.status < 400 -> LOGGER.warn("${"GET".padEnd(4)}: ${response.status} ${response.statusText} - ${clean(url)}")
				response.status < 500 -> LOGGER.warn("${"GET".padEnd(4)}: ${response.status} ${response.statusText} - ${clean(url)}")
				else -> LOGGER.error("${"GET".padEnd(4)}: ${response.status} ${response.statusText} - ${clean(url)}")
			}
			if (response.status != 200) null else response.body
		} catch (ue: UnirestException) {
			LOGGER.error("Unable to retrieve URL: ${ue.localizedMessage}")
			null
		}
	}
}