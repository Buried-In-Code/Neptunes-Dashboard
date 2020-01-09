package macro.dashboard.neptunes.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory
import com.fasterxml.jackson.dataformat.yaml.YAMLGenerator
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import org.apache.logging.log4j.LogManager
import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by Macro303 on 2018-Nov-23.
 */
class Config {
	var proxy: Connection = Connection()
	var server: Connection = Connection("localhost", 5505)

	fun save(): Config {
		LOGGER.info("Saving Config")
		val mapper = ObjectMapper(YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
		mapper.findAndRegisterModules()
		mapper.registerModule(Jdk8Module())
		try {
			mapper.writeValue(File(filename), this)
		} catch (ioe: IOException) {
			LOGGER.error("Unable to write Config: {}", ioe.localizedMessage)
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
		@JvmStatic
		val INSTANCE: Config by lazy {
			load()
		}

		@JvmStatic
		fun load(): Config {
			LOGGER.info("Loading Config")
			val temp = File(filename)
			if (!temp.exists())
				Config().save()
			val mapper = ObjectMapper(YAMLFactory().disable(YAMLGenerator.Feature.WRITE_DOC_START_MARKER))
			mapper.findAndRegisterModules()
			mapper.registerModule(Jdk8Module())
			try {
				return mapper.readValue(File("config.yaml"), Config::class.java).save()
			} catch (ioe: IOException) {
				LOGGER.error("Unable to read Config: {}", ioe.localizedMessage)
			}
			return Config().save()
		}
	}
}