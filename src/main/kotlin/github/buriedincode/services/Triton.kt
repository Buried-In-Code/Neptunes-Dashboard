package github.buriedincode.services

import github.buriedincode.Utils.JSON_MAPPER
import github.buriedincode.Utils.log
import github.buriedincode.VERSION
import github.buriedincode.services.triton.Response
import github.buriedincode.services.triton.ScanData
import io.github.oshai.kotlinlogging.KotlinLogging
import io.github.oshai.kotlinlogging.Level
import java.io.IOException
import java.net.URI
import java.net.URLEncoder
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.nio.charset.StandardCharsets
import java.time.Duration
import java.util.stream.Collectors

object Triton {
  @JvmStatic private val LOGGER = KotlinLogging.logger {}
  private const val BASE_API = "https://np.ironhelmet.com/api"
  private val CLIENT =
    HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).connectTimeout(Duration.ofSeconds(5)).build()

  private fun encodeURI(endpoint: String, params: Map<String, String> = HashMap()): URI {
    var encodedUrl: String = "$BASE_API$endpoint"
    if (params.isNotEmpty()) {
      encodedUrl =
        params.keys
          .stream()
          .sorted()
          .map { "$it=${URLEncoder.encode(params[it], StandardCharsets.UTF_8)}" }
          .collect(Collectors.joining("&", "$BASE_API$endpoint?", ""))
    }
    return URI.create(encodedUrl)
  }

  private fun getFormDataAsString(formData: Map<String, String>): String {
    val builder = StringBuilder()
    formData.forEach { (key, value) ->
      if (builder.isNotEmpty()) {
        builder.append("&")
      }
      builder.append(URLEncoder.encode(key, StandardCharsets.UTF_8))
      builder.append("=")
      builder.append(URLEncoder.encode(value, StandardCharsets.UTF_8))
    }
    return builder.toString()
  }

  private fun sendRequest(uri: URI, body: Map<String, String>): String? {
    try {
      val request =
        HttpRequest.newBuilder()
          .uri(uri)
          .setHeader("Accept", "application/json")
          .setHeader("Content-Type", "application/x-www-form-urlencoded")
          .setHeader("User-Agent", "Neptunes-Dashboard_v${VERSION}/Kotlin_v${KotlinVersion.CURRENT}")
          .POST(HttpRequest.BodyPublishers.ofString(getFormDataAsString(body)))
          .build()
      val response = CLIENT.send(request, HttpResponse.BodyHandlers.ofString())
      val level =
        when {
          response.statusCode() in (100 until 200) -> Level.WARN
          response.statusCode() in (200 until 300) -> Level.INFO
          response.statusCode() in (300 until 400) -> Level.INFO
          response.statusCode() in (400 until 500) -> Level.WARN
          else -> Level.ERROR
        }
      LOGGER.log(level) { "GET: ${response.statusCode()} - $uri" }
      if (response.statusCode() == 200) {
        return response.body()
      }
      LOGGER.error { response.body() }
    } catch (exc: IOException) {
      LOGGER.error(exc) { "Unable to make request to: ${uri.path}" }
    } catch (exc: InterruptedException) {
      LOGGER.error(exc) { "Unable to make request to: ${uri.path}" }
    }
    return null
  }

  fun getGame(gameId: Long, apiKey: String): ScanData? {
    val uri = encodeURI(endpoint = "")
    val content: String? =
      sendRequest(uri = uri, body = mapOf("api_version" to "0.1", "game_number" to "$gameId", "code" to apiKey))
    return if (content != null) JSON_MAPPER.decodeFromString<Response>(content).scanningData else null
  }
}
