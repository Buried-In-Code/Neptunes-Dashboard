package macro.neptunes

import com.google.gson.GsonBuilder
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy
import java.util.*

/**
 * Created by Macro303 on 2018-05-07.
 */
class Config(val gameID: Long) {
	val playerNames = HashMap<String, String>()
	val teams = HashMap<String, ArrayList<String>>()
	private val proxyHostname: String? = null
	private val proxyPort: Int? = null

	val proxy: Proxy?
		get() = if (proxyHostname == null || proxyPort == null)
			null
		else
			Proxy(Proxy.Type.HTTP, InetSocketAddress(proxyHostname, proxyPort))

	companion object {
		private val gson = GsonBuilder()
			.serializeNulls()
			.setPrettyPrinting()
			.disableHtmlEscaping()
			.create()
		private val configFile: File by lazy {
			val data = File("bin")
			if (!data.exists())
				data.mkdirs()
			File(data, "config.json")
		}

		fun loadConfig(): Config {
			var config: Config? = null
			try {
				FileReader(configFile).use { reader -> config = gson.fromJson(reader, Config::class.java) }
			} catch (ioe: IOException) {
				println("Unable to Load Config: ${ioe.message}")
			} finally {
				if (config == null) {
					println("Config couldn't be loaded")
					saveConfig(Config(gameID = 1L))
					config = loadConfig()
				}
			}
			return config!!
		}

		private fun saveConfig(config: Config) {
			try {
				FileWriter(configFile).use { writer -> gson.toJson(config, writer) }
			} catch (ioe: IOException) {
				println("Unable to Save Config: ${ioe.message}")
			}
		}
	}
}