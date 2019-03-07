package macro.dashboard.neptunes.backend

import com.mashape.unirest.http.Unirest
import macro.dashboard.neptunes.config.Config.Companion.CONFIG
import org.apache.http.HttpHost
import org.apache.http.auth.AuthScope
import org.apache.http.auth.UsernamePasswordCredentials
import org.apache.http.impl.client.BasicCredentialsProvider
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.impl.client.ProxyAuthenticationStrategy
import org.apache.logging.log4j.LogManager
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.Charset
import khttp.post as httpPost

/**
 * Created by Macro303 on 2019-Feb-26.
 */
object RESTClient {
	private val LOGGER = LogManager.getLogger(RESTClient::class.java)
	private val HEADERS = mapOf(
		"Content-Type" to "application/json",
		"User-Agent" to "Neptune's Dashboard"
	)
	private const val LINE_FEED = "\r\n"

	fun postRequest(url: String, gameID: Long, code: String): Map<String, Any?> {
		unirestRequest(url = url, gameID = gameID, code = code)
		return if (CONFIG.proxy == null)
			khttpRequest(url = url, gameID = gameID, code = code)
		else {
			unirestRequest(url = url, gameID = gameID, code = code)
//			httpRequest(url = url, gameID = gameID, code = code)
		}
	}

	private fun khttpRequest(url: String, gameID: Long, code: String): Map<String, Any?> {
		val response = httpPost(
			url = url,
			headers = HEADERS,
			data = mapOf(
				"api_version" to 0.1,
				"game_number" to gameID,
				"code" to code
			)
		)
		LOGGER.info("Status: ${response.statusCode}")
		LOGGER.info("Object: ${response.jsonObject}")
		return mapOf(
			"Code" to response.statusCode,
			"Response" to response.jsonObject["scanning_data"].toString()
		)
	}

	private fun unirestRequest(url: String, gameID: Long, code: String): Map<String, Any>{
		Unirest.setProxy(HttpHost(CONFIG.proxyHostname, CONFIG.proxyPort!!))
		val boundary = "===${System.currentTimeMillis()}==="
		val response = Unirest.post(url)
			.header("content-type", "multipart/form-data; boundary=$boundary")
			.body("--$boundary\r\nContent-Disposition: form-data; name=\"api_version\"\r\n\r\n0.1\r\n--$boundary\r\nContent-Disposition: form-data; name=\"game_number\"\r\n\r\n$gameID\r\n--$boundary\r\nContent-Disposition: form-data; name=\"code\"\r\n\r\n$code\r\n--$boundary--")
			.asString()
		LOGGER.info("Status: ${response.status}")
		LOGGER.info("Body: ${response.body}")
		return mapOf(
			"Code" to response.status,
			"Response" to response.jsonObject["scanning_data"].toString()
		)
	}

	private fun httpRequest(url: String, gameID: Long, code: String): Map<String, Any?> {
		val boundary = "===${System.currentTimeMillis()}==="
		val headers = HEADERS.plus("Content-Type" to "multipart/form-data; boundry=$boundary")
		val connection = setupConnection(url = url, method = "POST", headers = headers)
		try {
			connection.outputStream.use {
				BufferedWriter(OutputStreamWriter(it, Charset.forName("UTF-8"))).use { writer ->
					writer.append("--$boundary$LINE_FEED")
					writer.append("Content-Disposition: form-data; name=\"api_version\"${LINE_FEED + LINE_FEED}0.1$LINE_FEED")
					writer.append("Content-Disposition: form-data; name=\"game_number\"${LINE_FEED + LINE_FEED + gameID + LINE_FEED}")
					writer.append("Content-Disposition: form-data; name=\"code\"${LINE_FEED + LINE_FEED + code + LINE_FEED}")
					writer.append("$boundary--")
					writer.flush()
				}
			}
			LOGGER.info("Calling: POST - $url")
			connection.connect()
			return mapOf(
				"Code" to connection.responseCode,
				"Data" to getResponse(connection = connection)
			)
		} catch (_: IOException) {
		} finally {
			LOGGER.info("Received: ${connection.responseCode}")
			connection.disconnect()
		}
		return emptyMap()
	}

	private fun setupConnection(
		url: String,
		method: String,
		headers: Map<String, String> = HEADERS
	): HttpURLConnection {
		val connection = when {
			CONFIG.proxy == null -> URL(url).openConnection() as HttpURLConnection
			else -> URL(url).openConnection(CONFIG.proxy!!) as HttpURLConnection
		}
		connection.connectTimeout = 5 * 1000
		connection.readTimeout = 5 * 1000
		connection.doInput = true
		connection.doOutput = true
		headers.forEach { key, value ->
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