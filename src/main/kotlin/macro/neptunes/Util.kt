package macro.neptunes

import org.apache.logging.log4j.LogManager
import java.io.File

object Util {
	private val LOGGER = LogManager.getLogger(Util::class.java)
	internal const val ENDPOINT = "http://nptriton.cqproject.net/game/"

	val BIN: File by lazy {
		val temp = File("bin")
		if (!temp.exists()) {
			LOGGER.info("Bin Folder is missing, creating `$temp`")
			temp.mkdirs()
		}
		temp
	}
}