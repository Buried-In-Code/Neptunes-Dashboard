package macro.neptunes.backend

import org.apache.logging.log4j.LogManager
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.InetSocketAddress
import java.net.Proxy
import java.net.URL
import java.nio.charset.Charset

/**
 * Created by Macro303 on 2019-Feb-26.
 */
data class RESTClient(val baseUrl: String, val gameID: Long) {
	private val LOGGER = LogManager.getLogger(RESTClient::class.java)
	private val HEADERS = mapOf(
		"Content-Type" to "application/json",
		"User-Agent" to "Neptune's Dashboard"
	)

	fun getRequest(endpoint: String): Map<String, Any?> {
		var response: Map<String, Any?> = mapOf("Code" to 500)
		val connection = setupConnection(endpoint = endpoint, method = "GET")
		try {
			LOGGER.info("Calling: GET - /$gameID$endpoint")
			connection.connect()
			response = mapOf(
				"Code" to connection.responseCode,
				"Data" to getResponse(connection = connection)
			)
		} catch (_: IOException) {
		} finally {
			connection.disconnect()
			LOGGER.info("Received: ${response["Code"]}")
		}
		return response
	}

	private fun setupConnection(endpoint: String, method: String): HttpURLConnection {
		val url = URL("$baseUrl/$gameID$endpoint")
		val connection = url.openConnection(Proxy(Proxy.Type.HTTP, InetSocketAddress("DNZWGPX3.datacom.co.nz", 8080))) as HttpURLConnection
		connection.connectTimeout = 5 * 1000
		connection.readTimeout = 5 * 1000
		HEADERS.forEach { key, value ->
			connection.setRequestProperty(key, value)
		}
		connection.requestMethod = method
		return connection
	}

	@Throws(IOException::class)
	private fun getResponse(connection: HttpURLConnection): String? {
		if (connection.responseCode != 204) {
			return BufferedReader(InputStreamReader(connection.inputStream, Charset.forName("UTF-8"))).use {
				it.readText()
			}
		}
		return null
	}
}