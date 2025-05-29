package github.buriedincode

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.Connection
import java.text.NumberFormat
import java.time.Duration
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.io.path.createDirectories
import kotlin.io.path.div
import kotlin.io.path.exists
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.ExperimentalKeywordApi
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction

internal const val VERSION = "4.1.0"
internal const val PROJECT_NAME = "Neptunes-Dashboard"

object Utils {
  @JvmStatic private val LOGGER = KotlinLogging.logger {}

  private val USER_HOME: Path = Paths.get(System.getProperty("user.home"))
  private val XDG_CACHE_HOME: Path = System.getenv("XDG_CACHE_HOME")?.let { Paths.get(it) } ?: (USER_HOME / ".cache")
  private val XDG_CONFIG_HOME: Path = System.getenv("XDG_CONFIG_HOME")?.let { Paths.get(it) } ?: (USER_HOME / ".config")
  private val XDG_DATA_HOME: Path =
    System.getenv("XDG_DATA_HOME")?.let { Paths.get(it) } ?: (USER_HOME / ".local" / "share")

  internal val CACHE_ROOT = XDG_CACHE_HOME / PROJECT_NAME.lowercase()
  internal val CONFIG_ROOT = XDG_CONFIG_HOME / PROJECT_NAME.lowercase()
  internal val DATA_ROOT = XDG_DATA_HOME / PROJECT_NAME.lowercase()

  private val DATABASE: Database by lazy {
    val settings = Settings.load()
    return@lazy Database.connect(
      url = "jdbc:sqlite:${settings.database.url}",
      driver = "org.sqlite.JDBC",
      databaseConfig =
        DatabaseConfig {
          @OptIn(ExperimentalKeywordApi::class)
          preserveKeywordCasing = true
        },
    )
  }

  @OptIn(ExperimentalSerializationApi::class)
  val JSON_MAPPER: Json = Json {
    prettyPrint = true
    encodeDefaults = true
    namingStrategy = JsonNamingStrategy.SnakeCase
  }

  init {
    listOf(CACHE_ROOT, CONFIG_ROOT, DATA_ROOT).forEach { if (!it.exists()) it.createDirectories() }
  }

  internal fun <T> transaction(block: () -> T): T {
    val startTime = Clock.System.now()
    val transaction =
      transaction(transactionIsolation = Connection.TRANSACTION_SERIALIZABLE, db = DATABASE) {
        addLogger(Slf4jSqlDebugLogger)
        block()
      }
    val difference = Clock.System.now() - startTime
    LOGGER.debug { "Transaction took ${toHumanReadable(difference.inWholeMilliseconds)}" }
    return transaction
  }

  internal fun KLogger.log(level: Level, message: () -> Any?) {
    when (level) {
      Level.TRACE -> this.trace(message)
      Level.DEBUG -> this.debug(message)
      Level.INFO -> this.info(message)
      Level.WARN -> this.warn(message)
      Level.ERROR -> this.error(message)
      else -> return
    }
  }

  internal fun toHumanReadable(milliseconds: Float): String = toHumanReadable(milliseconds.toLong())

  internal fun toHumanReadable(milliseconds: Long): String {
    val duration = Duration.ofMillis(milliseconds)
    val minutes = duration.toMinutes()
    val seconds = duration.seconds - minutes * 60
    val millis = duration.toMillis() - (minutes * 60000 + seconds * 1000)
    return when {
      minutes > 0 -> "${minutes}m ${seconds}s ${millis}ms"
      seconds > 0 -> "${seconds}s ${millis}ms"
      else -> "${millis}ms"
    }
  }

  inline fun <reified T : Enum<T>> String.asEnumOrNull(): T? =
    enumValues<T>().firstOrNull { it.name.replace("_", " ").equals(this.replace("_", " "), ignoreCase = true) }

  inline fun <reified T : Enum<T>> T.titlecase(): String =
    this.name.lowercase().split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }

  private fun getDayNumberSuffix(day: Int): String {
    return if (day in 11..13) {
      "th"
    } else {
      when (day % 10) {
        1 -> "st"
        2 -> "nd"
        3 -> "rd"
        else -> "th"
      }
    }
  }

  fun LocalDateTime.toHumanReadable(): String {
    val pattern = "d'${getDayNumberSuffix(this.dayOfMonth)}' MMM yyyy HH:mm"
    return this.toString(pattern)
  }

  fun LocalDateTime.toString(pattern: String): String =
    this.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH))

  fun Int.localeString(): String = NumberFormat.getNumberInstance().format(this)

  fun Long.localeString(): String = NumberFormat.getNumberInstance().format(this)
}
