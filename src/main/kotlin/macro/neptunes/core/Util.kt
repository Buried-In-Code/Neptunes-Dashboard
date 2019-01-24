package macro.neptunes.core

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import io.ktor.request.ApplicationRequest
import io.ktor.request.httpMethod
import io.ktor.request.path
import macro.neptunes.data.Message
import org.apache.logging.log4j.LogManager
import java.io.File
import java.text.NumberFormat

/**
 * Created by Macro303 on 2018-Nov-12.
 */
object Util {
	private val LOGGER = LogManager.getLogger(Util::class.java)
	private val GSON = GsonBuilder()
		.serializeNulls()
		.disableHtmlEscaping()
		.create()
	internal val INT_FORMAT = NumberFormat.getIntegerInstance()
	internal val PERCENT_FORMAT = NumberFormat.getPercentInstance()

	init {
		PERCENT_FORMAT.minimumFractionDigits = 2
		PERCENT_FORMAT.maximumFractionDigits = 2
		PERCENT_FORMAT.minimumIntegerDigits = 2
		PERCENT_FORMAT.maximumIntegerDigits = 2
	}

	val BIN: File by lazy {
		val temp = File("bin")
		if (!temp.exists()) {
			LOGGER.info("Bin Folder is missing, creating `$temp`")
			temp.mkdirs()
		}
		temp
	}

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
}