package macro.neptunes.game

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.Route
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import macro.neptunes.DataNotFoundException
import macro.neptunes.IRequest
import macro.neptunes.IRouter
import macro.neptunes.Server
import macro.neptunes.backend.Neptunes

/**
 * Created by Macro303 on 2018-Nov-16.
 */
internal object GameRouter : IRouter<Game> {
	override fun getAll(): List<Game> = listOfNotNull(GameTable.select())
	override suspend fun get(call: ApplicationCall): Game = call.parseParam()

	override suspend fun ApplicationCall.parseParam(): Game {
		throw DataNotFoundException(type = "Game", field = "", value = null)
	}

	fun Route.gameRoutes() {
		route(path = "/game") {
			get {
				call.respond(
					message = GameTable.select()?.toOutput(showParent = true) ?: emptyMap<String, Any>(),
					status = HttpStatusCode.OK
				)
			}
			put {
				Neptunes.updateGame()
				call.respond(
					message = emptyMap<String, String>(),
					status = HttpStatusCode.NoContent
				)
			}
		}
	}
}