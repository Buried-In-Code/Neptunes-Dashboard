package macro.dashboard.neptunes

import com.google.gson.GsonBuilder
import kong.unirest.HttpResponse
import kong.unirest.JsonNode
import kong.unirest.Unirest
import kong.unirest.UnirestException
import macro.dashboard.neptunes.config.Config
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.TransactionManager
import java.sql.Connection

/**
 * Created by Macro303 on 2018-Nov-12.
 */
object Util {
	private val LOGGER = LogManager.getLogger(Util::class.java)
	internal val database = Database.connect(url = "jdbc:sqlite:Neptunes-Dashboard.sqlite", driver = "org.sqlite.JDBC")
	internal val HEADERS = mapOf(
		"Accept" to "application/json; charset=UTF-8",
		"Content-Type" to "application/json; charset=UTF-8",
		"User-Agent" to "Neptune's Dashboard"
	)
	internal val GSON = GsonBuilder()
		.setPrettyPrinting()
		.serializeNulls()
		.disableHtmlEscaping()
		.create()

	init {
		TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
		Unirest.config().enableCookieManagement(false)
		if (Config.INSTANCE.proxy.hostName != null && Config.INSTANCE.proxy.port != null)
			Unirest.config().proxy(Config.INSTANCE.proxy.hostName, Config.INSTANCE.proxy.port!!)
	}

	internal fun postRequest(url: String, gameId: Long, code: String): JsonNode? {
		val boundary = "===${System.currentTimeMillis()}==="
		val request = Unirest.post(url)
			.header("content-type", "multipart/form-data; boundary=$boundary")
			.header("User-Agent", "Neptune's Dashboard")
			.body("--$boundary\r\nContent-Disposition: form-data; name=\"api_version\"\r\n\r\n0.1\r\n--$boundary\r\nContent-Disposition: form-data; name=\"game_number\"\r\n\r\n$gameId\r\n--$boundary\r\nContent-Disposition: form-data; name=\"code\"\r\n\r\n$code\r\n--$boundary--")
		LOGGER.debug("GET : >>> - ${request.url}")
		val response: HttpResponse<JsonNode>
		try {
			response = request.asJson()
		} catch (ue: UnirestException) {
			LOGGER.error("Unable to load URL: $ue")
			return null
		}

		var level = Level.ERROR
		when {
			response.status < 100 -> level = Level.ERROR
			response.status < 200 -> level = Level.INFO
			response.status < 300 -> level = Level.INFO
			response.status < 400 -> level = Level.WARN
			response.status < 500 -> level = Level.WARN
		}
		LOGGER.log(level, "GET: ${response.status} ${response.statusText} - ${request.url}")
		LOGGER.debug("Response: ${response.body}")
		return if (response.status != 200) null else response.body
	}
}