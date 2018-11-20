package macro.neptunes.core

import com.google.gson.GsonBuilder
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import org.apache.logging.log4j.LogManager
import java.io.File
import java.time.LocalDateTime

/**
 * Created by Macro303 on 2018-Nov-12.
 */
object Util {
	private val LOGGER = LogManager.getLogger(Util::class.java)
	private val GSON = GsonBuilder()
		.serializeNulls()
		.disableHtmlEscaping()
		.create()
	internal const val ENDPOINT = "http://nptriton.cqproject.net/game/"
	lateinit var lastUpdate: LocalDateTime

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

	internal fun htmlToString(location: String): String {
		return Util::class.java.getResource("/public/$location.html").readText()
	}

	fun addHTML(bodyHTML: String, title: String): String {
		var output = """
			<!DOCTYPE html>
			<html lang="en">
				<head>
					<title>$title</title>
					<meta charset="utf-8">
					<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css">
					<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
					<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js"></script>
					<script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js"></script>
				</head>
				<body>
		""".trimIndent()
		output += bodyHTML
		output += """
				</body>
			</html>
		""".trimIndent()
		return output
	}
}