package macro.neptunes.game

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.response.respond
import io.ktor.routing.*
import macro.neptunes.Server
import macro.neptunes.Router

/**
 * Created by Macro303 on 2018-Nov-16.
 */
object GameRouter : Router<Game>() {
	override fun getAll(): List<Game> = listOfNotNull(GameTable.select())
	override suspend fun get(call: ApplicationCall, useJson: Boolean): Game? = call.parseParam(useJson = useJson)

	override suspend fun ApplicationCall.parseParam(useJson: Boolean): Game? {
		notFound(useJson = useJson, type = "game", field = "", value = null)
		return null
	}

	override suspend fun ApplicationCall.parseBody(useJson: Boolean): Map<String, Any>? {
		badRequest(useJson = useJson, fields = emptyArray(), values = emptyArray())
		return null
	}

	fun Route.gameRoutes() {
		route(path = "/game") {
			contentType(contentType = ContentType.Application.Json) {
				get {
					call.respond(
						message = GameTable.select()?.toOutput(showParent = true) ?: emptyMap<String, Any>(),
						status = HttpStatusCode.OK
					)
				}
			}
		}
		route(path = "/refresh") {
			contentType(contentType = ContentType.Application.Json) {
				put {
					Server.refreshData()
					call.respond(
						message = emptyMap<String, String>(),
						status = HttpStatusCode.NoContent
					)
				}
			}
		}
	}
}