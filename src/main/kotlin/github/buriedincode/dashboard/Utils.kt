package github.buriedincode.dashboard

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import org.apache.logging.log4j.kotlin.Logging
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.DatabaseConfig
import org.jetbrains.exposed.sql.ExperimentalKeywordApi
import org.jetbrains.exposed.sql.Slf4jSqlDebugLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.sql.Connection
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale
import kotlin.io.path.div

object Utils : Logging {
    private val HOME_ROOT: Path = Paths.get(System.getProperty("user.home"))
    private val XDG_CACHE: Path = System.getenv("XDG_CACHE_HOME")?.let {
        Paths.get(it)
    } ?: (HOME_ROOT / ".cache")
    private val XDG_CONFIG: Path = System.getenv("XDG_CONFIG_HOME")?.let {
        Paths.get(it)
    } ?: (HOME_ROOT / ".config")
    private val XDG_DATA: Path = System.getenv("XDG_DATA_HOME")?.let {
        Paths.get(it)
    } ?: (HOME_ROOT / ".local" / "share")

    internal const val VERSION = "4.0.0"
    internal val CACHE_ROOT = XDG_CACHE / "neptunes-dashboard"
    internal val CONFIG_ROOT = XDG_CONFIG / "neptunes-dashboard"
    internal val DATA_ROOT = XDG_DATA / "neptunes-dashboard"

    private val DATABASE: Database by lazy {
        val settings = Settings.load()
        if (settings.database.source == Settings.Database.Source.MYSQL) {
            return@lazy Database.connect(
                url = settings.database.url,
                driver = "com.mysql.cj.jdbc.Driver",
                user = settings.database.user ?: "username",
                password = settings.database.password ?: "password",
                databaseConfig = DatabaseConfig {
                    @OptIn(ExperimentalKeywordApi::class)
                    preserveKeywordCasing = true
                },
            )
        } else if (settings.database.source == Settings.Database.Source.POSTGRES) {
            return@lazy Database.connect(
                url = settings.database.url,
                driver = "org.postgresql.Driver",
                user = settings.database.user ?: "user",
                password = settings.database.password ?: "password",
                databaseConfig = DatabaseConfig {
                    @OptIn(ExperimentalKeywordApi::class)
                    preserveKeywordCasing = true
                },
            )
        }
        return@lazy Database.connect(
            url = settings.database.url,
            driver = "org.sqlite.JDBC",
            databaseConfig = DatabaseConfig {
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
        if (!Files.exists(CACHE_ROOT)) {
            CACHE_ROOT.toFile().mkdirs()
        }
        if (!Files.exists(CONFIG_ROOT)) {
            CONFIG_ROOT.toFile().mkdirs()
        }
        if (!Files.exists(DATA_ROOT)) {
            DATA_ROOT.toFile().mkdirs()
        }
    }

    internal fun <T> query(block: () -> T): T {
        val startTime = LocalDateTime.now()
        val transaction = transaction(transactionIsolation = Connection.TRANSACTION_SERIALIZABLE, db = DATABASE) {
            addLogger(Slf4jSqlDebugLogger)
            this.repetitionAttempts = 1
            block()
        }
        logger.debug("Took ${ChronoUnit.MILLIS.between(startTime, LocalDateTime.now())}ms")
        return transaction
    }

    inline fun <reified T : Enum<T>> String.asEnumOrNull(): T? {
        return enumValues<T>().firstOrNull {
            it.name.replace("_", " ").equals(this.replace("_", " "), ignoreCase = true)
        }
    }

    inline fun <reified T : Enum<T>> T.titlecase(): String {
        return this.name.lowercase().split("_").joinToString(" ") {
            it.replaceFirstChar(Char::uppercaseChar)
        }
    }

    internal fun toHumanReadable(milliseconds: Float): String {
        val duration = Duration.ofMillis(milliseconds.toLong())
        val minutes = duration.toMinutes()
        val seconds = duration.minusMinutes(minutes).toSeconds()
        val millis = duration.minusMinutes(minutes).minusSeconds(seconds).toMillis()

        return when {
            minutes > 0 -> "${minutes}min ${seconds}sec ${millis}ms"
            seconds > 0 -> "${seconds}sec ${millis}ms"
            else -> "${duration.toMillis()}ms"
        }
    }

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

    fun LocalDateTime.toString(pattern: String): String {
        return this.format(DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH))
    }
}
