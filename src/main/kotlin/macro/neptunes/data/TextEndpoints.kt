package macro.neptunes.data

import macro.neptunes.core.Config
import macro.neptunes.core.Util
import spark.Request
import spark.Response

/**
 * Created by Macro303 on 2018-Nov-14.
 */
internal object TextEndpoints {

	internal object Game {
		internal fun get(request: Request, response: Response) {
			val details = Util.game.toString()
			response.body(details)
		}
	}

	internal object Player {
		internal fun get(request: Request, response: Response) {
			val name = request.queryMap("name").value()
			val alias = request.queryMap("alias").value()
			var result = ""
			when {
				name == null -> Config.players.filter { it.value.equals(alias, ignoreCase = true) }.forEach { result += "${it.key}=${it.value}\n" }
				alias == null -> Config.players.filter { it.key.equals(name, ignoreCase = true) }.forEach { result += "${it.key}=${it.value}\n" }
				else -> Config.players.filter { it.key.equals(name, ignoreCase = true) }.filter { it.value.equals(alias, ignoreCase = true) }.forEach { result += "${it.key}=${it.value}\n" }
			}
			response.body(result.dropLast(1))
		}

		internal fun getAll(request: Request, response: Response) {
			var details = ""
			Config.players.forEach { details += "${it.key}=${it.value}\n" }
			response.body(details.dropLast(1))
		}

		internal fun add(request: Request, response: Response) {
			val lines = request.body().split("\n".toRegex())
			for (line: String in lines) {
				val details = line.split("=")
				Config.players = Config.players.plus(Pair(details[0], details[1]))
			}
		}
	}
}