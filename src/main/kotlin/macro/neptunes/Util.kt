package macro.neptunes

object Util {
	val API = "http://nptriton.cqproject.net/game/"
	val config: macro.neptunes.Config by lazy { macro.neptunes.Config.loadConfig() }
}