package macro.dashboard.neptunes.config

import org.slf4j.LoggerFactory
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.time.ZoneId

/**
 * Created by Macro303 on 2018-Nov-23.
 */
internal class Config {
	var server: Connection = Connection().apply {
		hostName = "localhost"
		port = 5505
	}
	var proxy: Connection = Connection()
	var game: Game = Game()
	var timeZone: String = "Pacific/Auckland"

	fun getZone(): ZoneId = ZoneId.of(timeZone)

	companion object {
		private val LOGGER = LoggerFactory.getLogger(Config::class.java)
		private val YAML: Yaml by lazy {
			val options = DumperOptions()
			options.defaultFlowStyle = DumperOptions.FlowStyle.FLOW
			options.isPrettyFlow = true
			Yaml(options)
		}
		var CONFIG = loadConfig()

		fun loadConfig(): Config {
			val temp = File("config.yaml")
			if (!temp.exists())
				Config().saveConfig()
			return Files.newBufferedReader(Paths.get("config.yaml")).use {
				YAML.loadAs(it, Config::class.java)
			}.saveConfig()
		}
	}

	fun saveConfig(): Config {
		Files.newBufferedWriter(Paths.get("config.yaml")).use {
			it.write(YAML.dumpAsMap(this))
		}
		return this
	}

	override fun toString(): String {
		return "Config(server=$server, proxy=$proxy, game=$game, timeZone='$timeZone')"
	}
}