package macro.neptunes

import org.apache.logging.log4j.LogManager
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Proxy

/**
 * Created by Macro303 on 2018-05-07.
 */
object Config {
	private val LOGGER = LogManager.getLogger(Config::class.java)
	private val CONFIG_FILE: File = File(Util.BIN, "config.yaml")
	private var YAML: Yaml = Yaml()
	var proxyHostname: String? = null
	var proxyPort: Int? = null
	val proxy: Proxy?
		get() = if (proxyHostname == null || proxyPort == null)
			null
		else
			Proxy(Proxy.Type.HTTP, InetSocketAddress(proxyHostname!!, proxyPort!!))
	var players: Map<String, Map<String, String>> = mapOf(
			Pair("Team 1", mapOf(
					Pair("Player 1 Alias", "Name"),
					Pair("Player 2 Alias", "Name")
			)),
			Pair("Team 2", mapOf(
					Pair("Player 3 Alias", "Name")
			))
	)
	var gameID: Long? = null

	init {
		val options = DumperOptions()
		options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
		options.isPrettyFlow = true
		YAML = Yaml(options)
		loadConfig()
	}

	private fun loadConfig() {
		try {
			FileReader(CONFIG_FILE).use {
				val loadedConfig: Map<String, Any?> = YAML.load(it)
				assignConfig(data = loadedConfig)
			}
		} catch (ioe: IOException) {
			LOGGER.error("Unable to Load Config", ioe)
			LOGGER.info("Config File is missing, creating `$CONFIG_FILE`")
		}
		saveConfig()
	}

	@Suppress("UNCHECKED_CAST")
	private fun assignConfig(data: Map<String, Any?>) {
		if (data.containsKey("Proxy")) {
			val proxySettings = data["Proxy"] as Map<*, *>
			if (proxySettings.containsKey("Host Name"))
				proxyHostname = proxySettings["Host Name"] as String?
			if (proxySettings.containsKey("Port"))
				proxyPort = proxySettings["Port"] as Int?
		}
		if (data.containsKey("Players"))
			players = data["Players"] as Map<String, Map<String, String>>
		if (data.containsKey("Game ID"))
			gameID = data["Game ID"] as Long?
	}

	private fun configToMap(): Map<String, Any?> {
		val dataMap: Map<String, Any?>
		val proxyMap: Map<String, Any?> = mapOf(Pair("Host Name", proxyHostname), Pair("Port", proxyPort))
		dataMap = mapOf(Pair("Proxy", proxyMap), Pair("Players", players), Pair("Game ID", gameID))
		return dataMap.toSortedMap()
	}

	private fun saveConfig() {
		try {
			FileWriter(CONFIG_FILE).use {
				YAML.dump(configToMap(), it)
			}
		} catch (ioe: IOException) {
			LOGGER.error("Unable to Save Config", ioe)
		}
	}
}