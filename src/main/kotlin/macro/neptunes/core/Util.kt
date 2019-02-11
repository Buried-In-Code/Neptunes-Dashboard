package macro.neptunes.core

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.ktor.http.HttpStatusCode
import io.ktor.request.ApplicationRequest
import io.ktor.request.httpMethod
import macro.neptunes.data.ErrorMessage
import org.apache.logging.log4j.LogManager
import java.time.format.DateTimeFormatter

/**
 * Created by Macro303 on 2018-Nov-12.
 */
object Util {
	private val LOGGER = LogManager.getLogger(Util::class.java)
	private val GSON = GsonBuilder()
		.serializeNulls()
		.disableHtmlEscaping()
		.create()
	private val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
	val JAVA_FORMATTER = DateTimeFormatter.ofPattern(DATE_FORMAT)

	@Throws(JsonSyntaxException::class)
	internal fun String.fromJSON(): Map<String, Any> {
		if (this.isBlank()) return emptyMap()
		val typeOfHashMap = object : TypeToken<Map<String, Any>>() {
		}.type
		return GSON.fromJson(this, typeOfHashMap) ?: emptyMap()
	}

	internal fun Any?.toJSON(): String = GSON.toJson(this)

	fun notImplementedMessage(request: ApplicationRequest): ErrorMessage {
		val error = ErrorMessage(
			code = HttpStatusCode.NotImplemented,
			request = "${request.httpMethod.value} ${request.local.uri}",
			message = "This endpoint hasn't been implemented yet, feel free to make a pull request and add it."
		)
		return error
	}

	fun notFoundMessage(request: ApplicationRequest, type: String, field: String, value: Any?): ErrorMessage {
		val error = ErrorMessage(
			code = HttpStatusCode.NotFound,
			request = "${request.httpMethod.value} ${request.local.uri}",
			message = "No $type was found with the $field: $value"
		)
		return error
	}
}