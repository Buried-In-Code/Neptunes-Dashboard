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
			val details = when {
				name == null -> Config.players.filter { it.value.equals(alias, ignoreCase = true) }.map { "${it.key}=${it.value}" }
				alias == null -> Config.players.filter { it.key.equals(name, ignoreCase = true) }.map { "${it.key}=${it.value}" }
				else -> Config.players.filter { it.key.equals(name, ignoreCase = true) }.filter { it.value.equals(alias, ignoreCase = true) }.map { "${it.key}=${it.value}" }
			}
			response.body(details.joinToString("\n"))
		}

		internal fun getAll(request: Request, response: Response) {
			val details = Config.players.map { "${it.key}=${it.value}" }
			response.body(details.joinToString("\n"))
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