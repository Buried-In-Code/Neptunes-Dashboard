package macro.neptunes

import com.google.gson.JsonSyntaxException
import macro.neptunes.core.Config
import macro.neptunes.core.Util
import macro.neptunes.core.Util.fromJSON
import macro.neptunes.core.Util.toJSON
import macro.neptunes.data.*
import macro.neptunes.data.ContentType.JSON
import macro.neptunes.data.ContentType.TEXT
import org.apache.logging.log4j.LogManager
import spark.kotlin.*
import kotlin.system.exitProcess

/**
 * Created by Macro303 on 2018-Nov-12.
 */
object Application {
	private val LOGGER = LogManager.getLogger(Application::class.java)

	init {
		if (Config.gameID == null) {
			LOGGER.fatal("Requires a Game ID")
			exitProcess(0)
		}
		refreshData()
	}

	@JvmStatic
	fun main(args: Array<String>) {
		val maxThreads = 8
		val minThreads = 2
		val timeOutMillis = 30000
		threadPool(maxThreads, minThreads, timeOutMillis)

		port(Config.port)

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
			response.type(request.contentType())
			when {
				request.contentType() == TEXT.value || request.contentType() == null -> "Welcome to BIT 269's Neptune's Pride"
				request.contentType() == JSON.value -> mapOf(Pair("Message", "Welcome to BIT 269's Neptune's Pride")).toJSON()
				else -> Exceptions.contentType(request = request, response = response)
			}
		}

		get("/game") {
			response.type(request.contentType())
			when {
				request.contentType() == TEXT.value || request.contentType() == null -> TextEndpoints.Game.get(request = request, response = response)
				request.contentType() == JSON.value -> JSONEndpoints.Game.get(request = request, response = response)
				else -> Exceptions.contentType(request = request, response = response)
			}
		}

		get("/players") {
			response.type(request.contentType())
			when {
				request.contentType() == TEXT.value || request.contentType() == null -> TextEndpoints.Player.getAll(request = request, response = response)
				request.contentType() == JSON.value -> JSONEndpoints.Player.getAll(request = request, response = response)
				else -> Exceptions.contentType(request = request, response = response)
			}
		}

		post("/player") {
			response.type(request.contentType())
			when {
				request.contentType() == TEXT.value || request.contentType() == null -> TextEndpoints.Player.add(request = request, response = response)
				request.contentType() == JSON.value -> JSONEndpoints.Player.add(request = request, response = response)
				else -> Exceptions.contentType(request = request, response = response)
			}
			if(response.status() != 400) {
				Config.saveConfig()
				refreshData()
				response.status(204)
			}
		}

		get("/player") {
			response.type(request.contentType())
			when {
				request.contentType() == TEXT.value || request.contentType() == null -> TextEndpoints.Player.get(request = request, response = response)
				request.contentType() == JSON.value -> JSONEndpoints.Player.get(request = request, response = response)
				else -> Exceptions.contentType(request = request, response = response)
			}
		}

		get("/refresh") {
			response.type(request.contentType())
			when {
				request.contentType() == TEXT.value || request.contentType() == null || request.contentType() == JSON.value -> {
					refreshData()
					response.status(204)
				}
				else -> Exceptions.contentType(request = request, response = response)
			}
		}

		post("/config") {
			if (request.contentType() != JSON.value || !validAuth(handler = this))
				Exceptions.forbidden(request = request, response = response)
			try {
				val temp: Map<String, Any> = request.body().fromJSON()
				if (temp.containsKey("Game ID"))
					Config.gameID = temp["Game ID"] as Long
				if (temp.containsKey("Refresh Rate"))
					Config.refreshRate = temp["Refresh Rate"] as Int
				Config.saveConfig()
				refreshData()
			} catch (jse: JsonSyntaxException) {
				Exceptions.forbidden(request = request, response = response)
			}
		}

		notFound {
			val message = "Unable to find the endpoint you are looking for: '${request.pathInfo()}'"
			when {
				request.contentType() == "text/plain" || request.contentType() == null -> {
					type(contentType = request.contentType())
					message
				}
				else -> {
					type(contentType = "application/json")
					mapOf(Pair("Error", message)).toJSON()
				}
			}
		}
	}

	@Suppress("UNCHECKED_CAST")
	private fun refreshData() {
		val response = RESTClient.getRequest(endpoint = "/basic")
		val game = Parser.parseGame(data = response["Data"] as Map<String, Any?>)
		if (game == null) {
			LOGGER.fatal("Unable to find game")
			exitProcess(status = 0)
		}
		Util.game = game
	}

	private fun validAuth(handler: RouteHandler): Boolean {
		return false
	}
}