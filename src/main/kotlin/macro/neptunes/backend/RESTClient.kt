package macro.neptunes.backend

import macro.neptunes.config.Config.Companion.CONFIG
import org.apache.logging.log4j.LogManager
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.io.Reader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset

/**
 * Created by Macro303 on 2018-Nov-08.
 */
internal class RESTClient(private val endpointUrl: String) {
	private val LOGGER = LogManager.getLogger(RESTClient::class.java)

	internal fun getRequest(
		endpoint: String,
		headers: Map<String, String> = mapOf("Content-Type" to "application/json"),
		parameters: Map<String, Any>? = null
	): Map<String, Any> {
		var response: Map<String, Any> = emptyMap()
		var connection: HttpURLConnection? = null
		try {
			connection = setupConnection(
				endpoint = endpoint,
				headers = headers,
				parameters = parameters
			)
			connection.connect()
			response = getResponse(connection = connection)
		} catch (ignored: IOException) {
		} finally {
			connection?.disconnect()
			LOGGER.info("GET << Code: ${response["Code"]}, Message: ${response["Message"]}")
			LOGGER.debug("Response Data: ${response["Data"]}")
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
		headers: Map<String, String>,
		parameters: Map<String, Any>?
	): HttpURLConnection {
		var urlString = endpointUrl + endpoint
		if (parameters != null)
			urlString = addParameters(endpoint = urlString, parameters = parameters)
		LOGGER.info("GET >> URL: $urlString, Headers: $headers")
		val url = URL(urlString)
		val connection = when {
			CONFIG.proxy == null -> url.openConnection() as HttpURLConnection
			else -> url.openConnection(CONFIG.proxy!!) as HttpURLConnection
		}
		connection.connectTimeout = 5 * 1000
		connection.readTimeout = 5 * 1000
		headers.forEach { key, value -> connection.setRequestProperty(key, value) }
		connection.requestMethod = "GET"
		return connection
	}

	private fun addParameters(endpoint: String, parameters: Map<String, Any>): String {
		val endpointBuilder = StringBuilder(endpoint)
		parameters.forEach { key, value -> endpointBuilder.append(",").append(key).append("=").append(value) }
		return endpointBuilder.toString().replaceFirst(",".toRegex(), "?")
	}

	@Throws(IOException::class)
	private fun getResponse(connection: HttpURLConnection): Map<String, Any> {
		val response = mapOf<String, Any>(
			"Code" to connection.responseCode,
			"Message" to connection.responseMessage
		)
		if (connection.responseCode != 204) {
			val returnedData =
				if (connection.responseCode == 200 || connection.responseCode == 201 || connection.responseCode == 202)
					readAll(BufferedReader(InputStreamReader(connection.inputStream, Charset.forName("UTF-8"))))
				else
					readAll(BufferedReader(InputStreamReader(connection.errorStream, Charset.forName("UTF-8"))))
			return response.plus("Data" to returnedData)
		}
		return response
	}
}