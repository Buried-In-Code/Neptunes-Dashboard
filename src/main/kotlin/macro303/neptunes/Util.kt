package macro303.neptunes

object Util {
	val API = "http://nptriton.cqproject.net/game/"
	val config: Config by lazy { Config.loadConfig() }
}