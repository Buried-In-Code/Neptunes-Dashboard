package macro.neptunes.core

import macro.neptunes.core.Util.logger
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
data class Config internal constructor(
	var proxyHostname: String?,
	var proxyPort: Int?,
	var gameID: Long?,
	var port: Int = 5000,
	var refreshRate: Int = 60,
	var starPercentage: Double = 50.0,
	var players: Map<String, String> = emptyMap(),
	var enableTeams: Boolean = false,
	var teams: Map<String, List<String>> = emptyMap()
) {
	val proxy: Proxy?
		get() = if (proxyHostname == null || proxyPort == null)
			null
		else
			Proxy(Proxy.Type.HTTP, InetSocketAddress(proxyHostname!!, proxyPort!!))

	companion object {
		private val LOGGER = logger()
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
			return temp!!
		}

		internal fun saveConfig(config: Config = CONFIG) {
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
			val gameID: Long? = data["Game ID"] as Long?
			val port: Int = data["Port"] as Int?
				?: 5000
			val refreshRate: Int = data["Refresh Rate"] as Int?
				?: 60
			val starPercentage: Double = data["Star Percentage"] as Double?
				?: 50.0
			val players: Map<String, String> = data["Players"] as Map<String, String>?
				?: mapOf("Alias" to "Name")
			val enableTeams: Boolean = data["Enable Teams"] as Boolean?
				?: false
			val teams: Map<String, List<String>> = data["Teams"] as Map<String, List<String>>?
				?: mapOf("Team" to listOf("Name"))
			return Config(
				proxyHostname = proxyHostname,
				proxyPort = proxyPort,
				gameID = gameID,
				port = port,
				refreshRate = refreshRate,
				starPercentage = starPercentage,
				players = players,
				enableTeams = enableTeams,
				teams = teams
			)
		}
	}
}

internal fun Config.toMap(): Map<String, Any?> {
	val data: Map<String, Any?> = mapOf(
		"Proxy" to mapOf(
			"Host Name" to this.proxyHostname,
			"Port" to this.proxyPort
		),
		"Game ID" to this.gameID,
		"Port" to this.port,
		"Refresh Rate" to this.refreshRate,
		"Star Percentage" to this.starPercentage,
		"Players" to this.players,
		"Enable Teams" to enableTeams,
		"Teams" to this.teams
	)
	return (if (!this.enableTeams) data.filterNot { it.key == "Teams" } else data).toSortedMap()
}