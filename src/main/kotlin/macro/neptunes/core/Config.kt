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
 * Created by Macro303 on 2018-Nov-23.
 */
class Config internal constructor(
	val serverAddress: String = "localhost",
	val serverPort: Int = 5000,
	val proxyHostname: String? = null,
	val proxyPort: Int? = null,
	var gameID: Long = 1,
	var gameName: String = "Unknown",
	var refreshRate: Int = 60,
	var players: Map<String, String> = mapOf("Alias" to "Name"),
	var enableTeams: Boolean = false,
	var teams: Map<String, List<String>> = mapOf("Team" to listOf("Name"))
) {
	val proxy: Proxy?
		get() = if (proxyHostname == null || proxyPort == null)
			null
		else
			Proxy(Proxy.Type.HTTP, InetSocketAddress(proxyHostname, proxyPort))

	companion object {
		private val LOGGER = LogManager.getLogger(Config::class.java)
		private val CONFIG_FILE: File = File("config.yaml")
		private val options: DumperOptions by lazy {
			val options = DumperOptions()
			options.defaultFlowStyle = DumperOptions.FlowStyle.BLOCK
			options.isPrettyFlow = true
			options
		}
		private var YAML: Yaml = Yaml(options)
		val CONFIG: Config by lazy {
			loadConfig()
		}

		internal fun loadConfig(): Config {
			var temp: Config? = null
			try {
				FileReader(CONFIG_FILE).use {
					val loadedConfig: Map<String, Any?> = YAML.load(it)
					temp = fromMap(data = loadedConfig)
				}
			} catch (ioe: IOException) {
				LOGGER.warn("Unable to Load Config", ioe)
				LOGGER.warn("Config File is missing, creating `$CONFIG_FILE`")
				saveConfig()
			}
			saveConfig(config = temp!!)
			return temp!!
		}

		internal fun saveConfig(config: Config = Config()) {
			try {
				FileWriter(CONFIG_FILE).use {
					YAML.dump(config.toMap(), it)
				}
			} catch (ioe: IOException) {
				LOGGER.error("Unable to Save Config", ioe)
			}
		}

		@Suppress("UNCHECKED_CAST")
		private fun fromMap(data: Map<String, Any?>): Config {
			val proxyHostname: String? = (data["Proxy"] as Map<String, Any?>?)?.get("Host Name") as String?
			val proxyPort: Int? = (data["Proxy"] as Map<String, Any?>?)?.get("Port") as Int?
			val gameID: Long = (data["Game"] as Map<String, Any?>?)?.get("ID") as Long?
				?: 1
			val gameName: String = (data["Game"] as Map<String, Any?>?)?.get("Name") as String?
				?: "Unknown"
			val serverAddress: String = (data["Server"] as Map<String, Any?>?)?.get("Address") as String?
				?: "localhost"
			val serverPort: Int = (data["Server"] as Map<String, Any?>?)?.get("Port") as Int?
				?: 5000
			val refreshRate: Int = data["Refresh Rate"] as Int?
				?: 60
			val players: Map<String, String> = data["Players"] as Map<String, String>?
				?: mapOf("Alias" to "Name")
			val enableTeams: Boolean = data["Enable Teams"] as Boolean?
				?: false
			val teams: Map<String, List<String>> = data["Teams"] as Map<String, List<String>>?
				?: mapOf("Team" to listOf("Name"))
			return Config(
				serverAddress = serverAddress,
				serverPort = serverPort,
				proxyHostname = proxyHostname,
				proxyPort = proxyPort,
				gameID = gameID,
				gameName = gameName,
				refreshRate = refreshRate,
				players = players,
				enableTeams = enableTeams,
				teams = teams
			)
		}
	}
}

internal fun Config.toMap(): Map<String, Any?> {
	val data: Map<String, Any?> = mapOf(
		"Server" to mapOf(
			"Address" to this.serverAddress,
			"Port" to this.serverPort
		),
		"Proxy" to mapOf(
			"Host Name" to this.proxyHostname,
			"Port" to this.proxyPort
		),
		"Game" to mapOf(
			"ID" to this.gameID,
			"Name" to this.gameName
		),
		"Refresh Rate" to this.refreshRate,
		"Players" to this.players,
		"Enable Teams" to this.enableTeams,
		"Teams" to this.teams
	)
	return (if (!this.enableTeams) data.filterNot { it.key == "Teams" } else data).toSortedMap()
}