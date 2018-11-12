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
			if (request.requestMethod() == "HEAD" || request.requestMethod() == "GET")
				LOGGER.info("${request.protocol()} ${request.requestMethod()} >> Endpoint: ${request.pathInfo()}, Content-Type: ${request.contentType()}, Agent: ${request.userAgent()}")
			if (request.requestMethod() == "POST")
				LOGGER.info("${request.protocol()} ${request.requestMethod()} >> Endpoint: ${request.pathInfo()}, Content-Type: ${request.contentType()}, Body: ${request.body().toJSON()}, Agent: ${request.userAgent()}")
		}
		finally {
			LOGGER.info("${request.protocol()} ${response.status()} << Content-Type: ${response.type()}, Body: ${response.body()}")
		}

		get("/hello") {
			type("application/json")
			if (request.contentType() != "application/json")
				contentException(request.contentType())
			mapOf(Pair("Message", "Hello")).toJSON()
		}

		notFound {
			type("application/json")
			val message = mapOf(Pair("Message", "Unable to find the endpoint you are looking for: '${request.pathInfo()}'")).toJSON()
			message
		}
	}

	fun Any?.toJSON(): String {
		return GSON.toJson(this)
	}

	fun contentException(content: String) {
		val message = mapOf(Pair("Message", "This endpoint doesn't support '$content' Content-Type")).toJSON()
		halt(403, message)
	}
}