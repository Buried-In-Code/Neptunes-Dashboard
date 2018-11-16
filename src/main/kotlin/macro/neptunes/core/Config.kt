package macro.neptunes.core

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
	var port: Int = 5000
	var proxyHostname: String? = null
	var proxyPort: Int? = null
	val proxy: Proxy?
		get() = if (proxyHostname == null || proxyPort == null)
			null
		else
			Proxy(Proxy.Type.HTTP, InetSocketAddress(proxyHostname!!, proxyPort!!))
	var players: Map<String, String> = mapOf(
		Pair("Alias 1", "Name"),
		Pair("Alias 2", "Name"),
		Pair("Alias 3", "Name")
	)
	var gameID: Long? = null
	var refreshRate: Int = 1
	var winPercentage: Double = 50.0
	var enableTeams: Boolean = false
	var teams: Map<String, List<String>> = mapOf(
		Pair("Team 1", listOf("Name 1", "Name 2")),
		Pair("Team 2", listOf("Name 3"))
	)

	init {
		val options = DumperOptions()
		options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
		options.isPrettyFlow = true
		YAML = Yaml(options)
		loadConfig()
		saveConfig()
	}

	private fun loadConfig() {
		try {
			FileReader(CONFIG_FILE).use {
				val loadedConfig: Map<String, Any?> = YAML.load(it)
				assignConfig(data = loadedConfig)
			}
		} catch (ioe: IOException) {
			LOGGER.warn("Unable to Load Config", ioe)
			LOGGER.warn("Config File is missing, creating `$CONFIG_FILE`")
			saveConfig()
		}
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
		if (data.containsKey("Port")) port = data["Port"] as Int
		if (data.containsKey("Players")) players = data["Players"] as Map<String, String>
		if (data.containsKey("Game ID")) gameID = data["Game ID"] as Long?
		if (data.containsKey("Refresh Rate")) refreshRate = data["Refresh Rate"] as Int
		if (data.containsKey("Win Percentage")) winPercentage = data["Win Percentage"] as Double
		if (data.containsKey("Enable Teams")) enableTeams = data["Enable Teams"] as Boolean
		if (data.containsKey("Teams") && enableTeams) teams = data["Teams"] as Map<String, List<String>>
	}

	private fun configToMap(): Map<String, Any?> {
		val proxyMap: Map<String, Any?> = mapOf(
			Pair("Host Name", proxyHostname),
			Pair("Port", proxyPort)
		)
		var dataMap: Map<String, Any?> = mapOf(
			Pair("Proxy", proxyMap),
			Pair("Port", port),
			Pair("Players", players),
			Pair("Game ID", gameID),
			Pair("Refresh Rate", refreshRate),
			Pair("Win Percentage", winPercentage),
			Pair("Enable Teams", enableTeams)
		)
		if (enableTeams)
			dataMap = dataMap.plus(Pair("Teams", teams))
		return dataMap.toSortedMap()
	}

	internal fun saveConfig() {
		try {
			FileWriter(CONFIG_FILE).use {
				YAML.dump(configToMap(), it)
			}
		} catch (ioe: IOException) {
			LOGGER.error("Unable to Save Config", ioe)
		}
		loadConfig()
	}
}