package macro.neptunes.core

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.ktor.request.ApplicationRequest
import io.ktor.request.httpMethod
import io.ktor.request.path
import macro.neptunes.data.Message
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
		return GSON.fromJson(this, typeOfHashMap)
	}

	internal fun Any?.toJSON(): String = GSON.toJson(this)

	fun notImplementedMessage(request: ApplicationRequest): Message {
		val message = Message(
			title = "Not Implemented: ${request.httpMethod.value} ${request.path()}",
			content = "This endpoint hasn't been implemented yet, feel free to make a pull request and add it."
		)
		return message
	}

	fun notFoundMessage(type: String, field: String, value: Any?): Message {
		val message = Message(
			title = "No $type Found",
			content = "No $type was found with the field $field: $value"
		)
		return message
	}
}