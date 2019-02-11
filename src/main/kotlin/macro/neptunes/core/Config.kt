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
	databaseFile: String? = null,
	serverAddress: String? = null,
	serverPort: Int? = null,
	val proxyHostname: String? = null,
	val proxyPort: Int? = null,
	gameID: Long? = null,
	refreshRate: Int? = null,
	players: Map<String, String>? = null,
	teams: Map<String, List<String>>? = null
) {
	var databaseFile: File
	val serverAddress: String = serverAddress ?: "localhost"
	val serverPort: Int = serverPort ?: 5505
	val proxy: Proxy?
		get() = if (proxyHostname == null || proxyPort == null)
			null
		else
			Proxy(Proxy.Type.HTTP, InetSocketAddress(proxyHostname, proxyPort))
	var gameID: Long = gameID ?: 1L
	var refreshRate: Int = refreshRate ?: 60
	var players: Map<String, String> = players ?: mapOf("Alias" to "Name")
	var teams: Map<String, List<String>> = teams ?: mapOf("Team" to listOf("Name"))

	init {
		this.databaseFile = when (databaseFile) {
			null -> File(Util.BIN, "Neptunes-Pride.db")
			else -> File(databaseFile)
		}
	}

	companion object {
		private val LOGGER = LogManager.getLogger(Config::class.java)
		private val CONFIG_FILE: File = File(Util.BIN, "config.yaml")
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
			return try {
				var temp: Config? = null
				FileReader(CONFIG_FILE).use {
					val loadedConfig: Map<String, Any?> = YAML.load(it)
					temp = fromMap(data = loadedConfig)
				}
				saveConfig(config = temp!!)
				temp!!
			} catch (ioe: IOException) {
				LOGGER.warn("Config File is missing, creating `$CONFIG_FILE`")
				saveConfig()
			}
		}

		internal fun saveConfig(config: Config = Config()): Config {
			try {
				FileWriter(CONFIG_FILE).use {
					YAML.dump(config.toMap(), it)
				}
			} catch (ioe: IOException) {
				LOGGER.error("Unable to Save Config", ioe)
			}
			return config
		}

		@Suppress("UNCHECKED_CAST")
		private fun fromMap(data: Map<String, Any?>): Config {
			val databaseFile = data["Database File"] as String?
			val proxyHostname = (data["Proxy"] as Map<String, Any?>?)?.get("Host Name") as String?
			val proxyPort = (data["Proxy"] as Map<String, Any?>?)?.get("Port") as Int?
			val gameID = (data["Game"] as Map<String, Any?>?)?.get("ID")?.toString()?.toLongOrNull()
			val serverAddress = (data["Server"] as Map<String, Any?>?)?.get("Address") as String?
			val serverPort = (data["Server"] as Map<String, Any?>?)?.get("Port") as Int?
			val refreshRate = data["Refresh Rate"] as Int?
			val players = data["Players"] as Map<String, String>?
			val teams = data["Teams"] as Map<String, List<String>>?
			return Config(
				databaseFile = databaseFile,
				serverAddress = serverAddress,
				serverPort = serverPort,
				proxyHostname = proxyHostname,
				proxyPort = proxyPort,
				gameID = gameID,
				refreshRate = refreshRate,
				players = players,
				teams = teams
			)
		}
	}
}

internal fun Config.toMap(): Map<*, *> {
	val data = mapOf(
		"Database File" to this.databaseFile.path,
		"Server" to mapOf(
			"Address" to this.serverAddress,
			"Port" to this.serverPort
		),
		"Proxy" to mapOf(
			"Host Name" to this.proxyHostname,
			"Port" to this.proxyPort
		),
		"Game" to mapOf(
			"ID" to this.gameID
		),
		"Refresh Rate" to this.refreshRate,
		"Players" to this.players,
		"Teams" to this.teams
	)
	return data.toSortedMap()
}