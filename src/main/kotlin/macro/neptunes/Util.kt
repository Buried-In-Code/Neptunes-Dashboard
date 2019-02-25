package macro.neptunes

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import macro.neptunes.config.Config.Companion.CONFIG
import macro.neptunes.game.Game
import macro.neptunes.player.Player
import macro.neptunes.player.PlayerDeserializer
import macro.neptunes.technology.PlayerTechnology
import macro.neptunes.technology.TechnologyDeserializer
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

/**
 * Created by Macro303 on 2018-Nov-12.
 */
object Util {
	private val LOGGER = LogManager.getLogger(Util::class.java)
	private const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
	private val database = Database.connect(url = "jdbc:sqlite:${CONFIG.databaseFile}", driver = "org.sqlite.JDBC")
	internal const val ENDPOINT = "http://nptriton.cqproject.net/game/"
	internal val GSON = GsonBuilder()
		.serializeNulls()
		.disableHtmlEscaping()
		.registerTypeAdapter(Player::class.java, PlayerDeserializer)
		.registerTypeAdapter(PlayerTechnology::class.java, TechnologyDeserializer)
		.create()
	val JAVA_FORMATTER: java.time.format.DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
	val JODA_FORMATTER: org.joda.time.format.DateTimeFormatter = DateTimeFormat.forPattern(DATE_FORMAT)

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

	@Throws(JsonSyntaxException::class)
	internal fun String.JsonToMap(): Map<String, Any> {
		if (this.isBlank()) return emptyMap()
		val type = object : TypeToken<Map<String, Any>>() {
		}.type
		return GSON.fromJson(this, type) ?: emptyMap()
	}

	@Throws(JsonSyntaxException::class)
	internal fun String.JsonToPlayerMap(): Map<String, Player?> {
		if (this.isBlank()) return emptyMap()
		val type = object : TypeToken<Map<String, Player?>>() {
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