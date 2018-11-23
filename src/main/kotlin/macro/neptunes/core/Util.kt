package macro.neptunes.core

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import org.slf4j.LoggerFactory
import java.io.File
import java.text.NumberFormat
import java.time.LocalDateTime

/**
 * Created by Macro303 on 2018-Nov-12.
 */
object Util {
	private val LOGGER = LoggerFactory.getLogger(Util::class.java)
	private val GSON = GsonBuilder()
		.serializeNulls()
		.disableHtmlEscaping()
		.create()
	internal val INT_FORMAT = NumberFormat.getIntegerInstance()
	internal val PERCENT_FORMAT = NumberFormat.getPercentInstance()
	lateinit var lastUpdate: LocalDateTime

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
}