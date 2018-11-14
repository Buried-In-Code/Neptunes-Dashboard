package macro.neptunes.data

import macro.neptunes.core.Config
import macro.neptunes.core.Util
import macro.neptunes.core.Util.fromJSON
import macro.neptunes.core.Util.toJSON
import spark.Request
import spark.Response

/**
 * Created by Macro303 on 2018-Nov-14.
 */
internal object JSONEndpoints {

	internal object Game {
		internal fun get(request: Request, response: Response) {
			val details = Util.game.toJSON()
			response.body(details)
		}
	}

	internal object Player {
		internal fun get(request: Request, response: Response) {
			val playerName = request.queryParamOrDefault("name", null)
					?: return Exceptions.missingParam(request = request, response = response, param = "name")
			val details = mapOf(Pair("Name", playerName)).toJSON()
			response.body(details)
		}

		internal fun getAll(request: Request, response: Response) {
			val details = Config.players.toJSON()
			response.body(details)
		}

		@Suppress("UNCHECKED_CAST")
		internal fun add(request: Request, response: Response) {
			val temp: Map<String, String> = request.body().fromJSON() as Map<String, String>
			Config.players = Config.players.plus(temp)
		}
	}
}