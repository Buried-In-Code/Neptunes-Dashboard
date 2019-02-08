package macro.neptunes.data

import macro.neptunes.core.Config.Companion.CONFIG
import macro.neptunes.core.Util.fromJSON
import macro.neptunes.core.Util.toJSON
import org.apache.logging.log4j.LogManager
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import java.util.*

/**
 * Created by Macro303 on 2018-Nov-08.
 */
internal object RESTClient {
	private val LOGGER = LogManager.getLogger(RESTClient::class.java)
	private const val ENDPOINT = "http://nptriton.cqproject.net/game/"
	private const val JSON = "application/json"

	internal fun getRequest(
		endpoint: String,
		headers: Map<String, String> = mapOf(Pair("Content-Type", JSON)),
		parameters: Map<String, Any>? = null
	): Map<String, Any> {
		var response: Map<String, Any> = HashMap()
		var connection: HttpURLConnection? = null
		try {
			connection = setupConnection(
				endpoint = endpoint,
				requestMethod = "GET",
				headers = headers,
				parameters = parameters
			)
			connection.connect()
			response = getResponse(connection)
		} catch (ignored: IOException) {
		} finally {
			connection?.disconnect()
			LOGGER.info("GET << Code: {}, Message: {}", response["Code"], response["Message"])
			LOGGER.debug("Response Data: {}", response["Data"])
		}
		return response
	}

	internal fun postRequest(
		endpoint: String,
		headers: Map<String, String> = mapOf(Pair("Content-Type", JSON)),
		parameters: Map<String, Any>? = null,
		body: Map<String, Any>
	): Map<String, Any> {
		var response: Map<String, Any> = HashMap()
		var connection: HttpURLConnection? = null
		try {
			connection = setupConnection(
				endpoint = endpoint,
				requestMethod = "POST",
				headers = headers,
				parameters = parameters
			)
			LOGGER.debug("POST >> Body: " + body.toJSON())
			val wr = OutputStreamWriter(connection.outputStream)
			wr.write(body.toJSON())
			wr.flush()
			connection.connect()
			response = getResponse(connection)
		} catch (ignored: IOException) {
		} finally {
			connection?.disconnect()
			LOGGER.info("POST << Code: {}, Message: {}", response["Code"], response["Message"])
			LOGGER.debug("Response Data: {}", response["Data"])
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

	@Throws(IOException::class)
	private fun setupConnection(
		endpoint: String,
		requestMethod: String,
		headers: Map<String, String>,
		parameters: Map<String, Any>? = null
	): HttpURLConnection {
		var urlString = ENDPOINT + CONFIG.gameID + endpoint
		if (parameters != null)
			urlString = addParameters(endpoint = urlString, parameters = parameters)
		LOGGER.info("$requestMethod >> URL: $urlString, Headers: $headers")
		val url = URL(urlString)
		val connection = when {
			CONFIG.proxy == null -> url.openConnection() as HttpURLConnection
			else -> url.openConnection(CONFIG.proxy!!) as HttpURLConnection
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
		val response = mapOf(
			"Code" to connection.responseCode,
			"Message" to connection.responseMessage
		)
		if (connection.responseCode != 204) {
			val returnedData: String =
				if (connection.responseCode == 200 || connection.responseCode == 201 || connection.responseCode == 202)
					readAll(BufferedReader(InputStreamReader(connection.inputStream, Charset.forName("UTF-8"))))
				else
					readAll(BufferedReader(InputStreamReader(connection.errorStream, Charset.forName("UTF-8"))))
			return response.plus("Data" to returnedData.fromJSON())
		}
		return response
	}
}