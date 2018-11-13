package macro.neptunes

import com.google.gson.GsonBuilder
import org.apache.logging.log4j.LogManager
import spark.kotlin.*

/**
 * Created by Macro303 on 2018-Nov-12.
 */
object Application {
	private val LOGGER = LogManager.getLogger(Application::class.java)
	private val GSON = GsonBuilder()
			.disableHtmlEscaping()
			.serializeNulls()
			.create()

	@JvmStatic
	fun main(args: Array<String>) {
		val maxThreads = 8
		val minThreads = 2
		val timeOutMillis = 30000
		threadPool(maxThreads, minThreads, timeOutMillis)

		port(5001)

		before {
			when (request.requestMethod()) {
				"HEAD" -> LOGGER.info("${request.protocol()} ${request.requestMethod()} >> Endpoint: ${request.pathInfo()}, Content-Type: ${request.contentType()}, Agent: ${request.userAgent()}")
				"GET" -> LOGGER.info("${request.protocol()} ${request.requestMethod()} >> Endpoint: ${request.pathInfo()}, Content-Type: ${request.contentType()}, Agent: ${request.userAgent()}")
				"POST" -> LOGGER.info("${request.protocol()} ${request.requestMethod()} >> Endpoint: ${request.pathInfo()}, Content-Type: ${request.contentType()}, Body: ${request.body().toJSON()}, Agent: ${request.userAgent()}")
			}
		}
		finally {
			LOGGER.info("${request.protocol()} ${response.status()} << Content-Type: ${response.type()}, Body: ${response.body()}")
		}

		get("/") {
			when {
				request.contentType() == "text/plain" || request.contentType() == null -> {
					type(contentType = "text/plain")
					"Welcome to BIT 269's Neptune's Pride"
				}
				request.contentType() == "application/json" -> {
					type(contentType = "application/json")
					mapOf(Pair("Message", "Welcome to BIT 269's Neptune's Pride")).toJSON()
				}
				else -> contentTypeException(handler = this)
			}
		}

		get("/players") {
			when {
				request.contentType() == "text/plain" || request.contentType() == null -> {
					val playerName = request.queryParamOrDefault("name", null)
					LOGGER.info("Player Name: $playerName")
					type(contentType = "text/plain")
					"Player Details"
				}
				request.contentType() == "application/json" -> {
					val playerName = request.queryParamOrDefault("name", null)
					LOGGER.info("Player Name: $playerName")
					type(contentType = "application/json")
					mapOf(Pair("Player", Pair("Name", playerName))).toJSON()
				}
				else -> contentTypeException(handler = this)
			}
		}

		get("/refresh"){
			when {
				request.contentType() == "text/plain" || request.contentType() == null -> {
					response.status(204)
				}
				request.contentType() == "application/json" -> {
					response.status(204)
				}
				else -> contentTypeException(handler = this)
			}
		}

		notFound {
			when {
				request.contentType() == "text/plain" || request.contentType() == null -> {
					type(contentType = "text/plain")
					"Unable to find the endpoint you are looking for: '${request.pathInfo()}'"
				}
				else -> {
					type(contentType = "application/json")
					mapOf(Pair("Error", "Unable to find the endpoint you are looking for: '${request.pathInfo()}'")).toJSON()
				}
			}
		}
	}

	private fun Any?.toJSON(): String {
		return GSON.toJson(this)
	}

	private fun contentTypeException(handler: RouteHandler) {
		handler.response.type("application/json")
		val message = mapOf(Pair("Error", "This endpoint doesn't support '${handler.request.contentType()}' Content-Type")).toJSON()
		halt(400, message)
	}
}