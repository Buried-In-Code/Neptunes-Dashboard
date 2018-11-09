package macro.neptunes.data

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import macro.neptunes.core.Config
import org.apache.logging.log4j.LogManager
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.*

/**
 * Created by Macro303 on 2018-Nov-08.
 */
internal object RestClient {
	private val LOGGER = LogManager.getLogger(RestClient::class.java)
	private const val ENDPOINT = "http://nptriton.cqproject.net/game/"
	private val GSON = GsonBuilder()
			.serializeNulls()
			.disableHtmlEscaping()
			.create()

	private fun pingURL(): Boolean {
		val responseCode = headRequest()
		return responseCode == 200 || responseCode == 201 || responseCode == 204
	}

	private fun headRequest(endpoint: String = "/", headers: Map<String, String> = mapOf(Pair("Content-Type", "application/json"))): Int {
		var response: Map<String, Any> = HashMap()
		var connection: HttpURLConnection? = null
		try {
			connection = setupConnection(endpoint = endpoint, requestMethod = "HEAD", headers = headers)
			connection.connect()
			response = getResponse(connection)
		} catch (ioe: IOException) {
			LOGGER.debug(ioe)
		} finally {
			connection?.disconnect()
			LOGGER.debug("HEAD << $response")
		}
		return response.getOrDefault("Code", 500) as Int
	}

	internal fun getRequest(endpoint: String, headers: Map<String, String> = mapOf(Pair("Content-Type", "application/json")), parameters: Map<String, Any>? = null): Map<String, Any> {
		if (!pingURL())
			throw RuntimeException("Server is Unavailable")
		var response: Map<String, Any> = HashMap()
		var connection: HttpURLConnection? = null
		try {
			connection = setupConnection(endpoint = endpoint, requestMethod = "GET", headers = headers, parameters = parameters)
			connection.connect()
			response = getResponse(connection)
		} catch (ignored: IOException) {
		} finally {
			connection?.disconnect()
			LOGGER.debug("GET << $response")
		}
		return response
	}

	internal fun postRequest(endpoint: String, headers: Map<String, String> = mapOf(Pair("Content-Type", "application/json")), parameters: Map<String, Any>? = null, body: Map<String, Any>): Map<String, Any> {
		if (!pingURL())
			throw RuntimeException("Server is Unavailable")
		var response: Map<String, Any> = HashMap()
		var connection: HttpURLConnection? = null
		try {
			connection = setupConnection(endpoint = endpoint, requestMethod = "POST", headers = headers, parameters = parameters)
			LOGGER.debug("POST >> Body: " + toJson(body))
			val wr = OutputStreamWriter(connection.outputStream)
			wr.write(toJson(body))
			wr.flush()
			connection.connect()
			response = getResponse(connection)
		} catch (ignored: IOException) {
		} finally {
			connection?.disconnect()
			LOGGER.debug("POST << $response")
		}
		return response
	}

	@Throws(IOException::class)
	private fun readAll(rd: Reader): String {
		val sb = StringBuilder()
		var cp: Int = rd.read()
		while (cp != -1) {
			sb.append(cp.toChar())
			cp = rd.read()
		}
		return sb.toString()
	}

	private fun toJson(data: Map<String, Any>): String {
		return GSON.toJson(data)
	}

	@Throws(JsonSyntaxException::class)
	private fun fromJson(json: String): Map<String, Any> {
		if (json.isBlank()) return emptyMap()
		val typeOfHashMap = object : TypeToken<Map<String, Any>>() {
		}.type
		return GSON.fromJson(json, typeOfHashMap)
	}

	@Throws(IOException::class)
	private fun setupConnection(endpoint: String, requestMethod: String, headers: Map<String, String>, parameters: Map<String, Any>? = null): HttpURLConnection {
		var urlString = ENDPOINT + Config.gameID + endpoint
		if (parameters != null)
			urlString = addParameters(endpoint = urlString, parameters = parameters)
		LOGGER.debug("$requestMethod >> URL: $urlString, Headers: $headers")
		val url = URL(urlString)
		val connection = when {
			Config.proxy == null -> url.openConnection() as HttpURLConnection
			else -> url.openConnection(Config.proxy!!) as HttpURLConnection
		}
		connection.connectTimeout = 5 * 1000
		connection.readTimeout = 5 * 1000
		headers.forEach { key, value -> connection.setRequestProperty(key, value) }
		connection.requestMethod = requestMethod
		if (requestMethod == "POST") {
			connection.doInput = true
			connection.doOutput = true
		}
		return connection
	}

	private fun addParameters(endpoint: String, parameters: Map<String, Any>): String {
		val endpointBuilder = StringBuilder(endpoint)
		parameters.forEach { key, value -> endpointBuilder.append(",").append(key).append("=").append(value) }
		return endpointBuilder.toString().replaceFirst(",".toRegex(), "?")
	}

	@Throws(IOException::class)
	private fun getResponse(connection: HttpURLConnection): Map<String, Any> {
		val response = HashMap<String, Any>()
		response["Code"] = connection.responseCode
		response["Message"] = connection.responseMessage
		if (connection.responseCode != 204) {
			val returnedData: String = if (connection.responseCode == 200 || connection.responseCode == 201 || connection.responseCode == 202)
				readAll(BufferedReader(InputStreamReader(connection.inputStream, Charset.forName("UTF-8"))))
			else
				readAll(BufferedReader(InputStreamReader(connection.errorStream, Charset.forName("UTF-8"))))
			response["Data"] = fromJson(returnedData)
		}
		return response
	}
}