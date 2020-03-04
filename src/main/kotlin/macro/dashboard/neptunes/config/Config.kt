package macro.dashboard.neptunes.config

import org.apache.logging.log4j.LogManager
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by Macro303 on 2018-Nov-23.
 */
class Config {
	var proxy: Connection = Connection()
	var server: Connection = Connection("localhost", 6790)

	fun save(): Config {
		Files.newBufferedWriter(Paths.get(filename)).use {
			it.write(YAML.dumpAsMap(this))
		}
		return this
	}

	companion object {
		private val LOGGER = LogManager.getLogger(Config::class.java)
		private const val filename = "config.yaml"
		private val YAML: Yaml by lazy {
			val options = DumperOptions()
			options.defaultFlowStyle = DumperOptions.FlowStyle.FLOW
			options.isPrettyFlow = true
			Yaml(options)
		}
		val INSTANCE: Config by lazy {
			load()
		}

		fun load(): Config {
			val temp = File(filename)
			if (!temp.exists())
				Config().save()
			return Files.newBufferedReader(Paths.get(filename)).use {
				YAML.loadAs(it, Config::class.java)
			}.save()
		}
	}
}