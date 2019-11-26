package macro.dashboard.neptunes

import freemarker.cache.ClassTemplateLoader
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.freemarker.FreeMarker
import io.ktor.freemarker.FreeMarkerContent
import io.ktor.gson.gson
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.defaultResource
import io.ktor.http.content.resource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.put
import io.ktor.routing.route
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import macro.dashboard.neptunes.config.Config.Companion.CONFIG
import macro.dashboard.neptunes.tick.TickRouter
import macro.dashboard.neptunes.tick.TickTable
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameRouter
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.PlayerRouter
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.team.TeamRouter
import macro.dashboard.neptunes.team.TeamTable
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.exists

object Server {
	private val LOGGER = LogManager.getLogger(Server::class.java)

	init {
		LOGGER.info("Initializing Neptune's Dashboard")
		checkLogLevels()
		checkDatabase()
		checkConfigGames()
	}

	private fun checkLogLevels() {
		LOGGER.trace("TRACE | is Visible")
		LOGGER.debug("DEBUG | is Visible")
		LOGGER.info("INFO  | is Visible")
		LOGGER.warn("WARN  | is Visible")
		LOGGER.error("ERROR | is Visible")
	}

	private fun checkDatabase() {
		Util.query(description = "Check All Tables Exist") {
			GameTable.exists()
			PlayerTable.exists()
			TickTable.exists()
			TeamTable.exists()
		}
	}

	private fun checkConfigGames() {
		CONFIG.game.gameId ?: return
		CONFIG.game.code ?: return
		GameTable.select(gameId = CONFIG.game.gameId!!) ?: Triton.getGame(
			gameId = CONFIG.game.gameId!!,
			code = CONFIG.game.code!!
		)
	}

	@KtorExperimentalAPI
	@JvmStatic
	fun main(args: Array<String>) {
		embeddedServer(
			Netty,
			port = CONFIG.server.port ?: 5505,
			host = CONFIG.server.hostName ?: "localhost",
			module = Application::module
		).apply { start(wait = true) }
	}
}

@KtorExperimentalAPI
fun Application.module() {
	install(ContentNegotiation) {
		gson {
			setPrettyPrinting()
			disableHtmlEscaping()
			serializeNulls()
		}
	}
	install(DefaultHeaders) {
		header(name = "Developer", value = "Macro303")
		header(name = HttpHeaders.Server, value = "Neptunes-Dashboard")
		header(name = HttpHeaders.AcceptLanguage, value = "en-NZ")
		header(name = HttpHeaders.ContentLanguage, value = "en-NZ")
	}
	install(ConditionalHeaders)
	install(AutoHeadResponse)
	install(FreeMarker) {
		templateLoader = ClassTemplateLoader(this::class.java, "/templates")
	}
	install(StatusPages) {
		exception<Throwable> {
			val error = ErrorMessage(
				code = HttpStatusCode.InternalServerError,
				request = "${call.request.httpMethod.value} ${call.request.local.uri}",
				message = it.message ?: "",
				cause = it
			)
			call.respond(error = error)
		}
	}
	install(Routing) {
		intercept(ApplicationCallPipeline.Setup) {
			application.log.debug("==> ${call.request.httpVersion} ${call.request.httpMethod.value} ${call.request.uri}, Accept: ${call.request.accept()}, Content-Type: ${call.request.contentType()}, User-Agent: ${call.request.userAgent()}")
		}
		trace {
			application.log.trace(it.buildText())
		}
		route(path = "/api") {
			@KtorExperimentalAPI
			fun getGame(): Game {
				CONFIG.game.gameId ?: throw NotFoundException()
				return GameTable.select(gameId = CONFIG.game.gameId!!) ?: throw NotFoundException()
			}

			route(path = "/game") {
				GameRouter.routing(this)
			}
			route(path = "/players") {
				get {
					call.respond(
						message = PlayerRouter.getPlayers(game = getGame()).map { it.toJson() },
						status = HttpStatusCode.OK
					)
				}
				route(path = "/{alias}") {
					@KtorExperimentalAPI
					fun ApplicationCall.getAlias() = parameters["alias"] ?: throw BadRequestException("Invalid Alias")

					get {
						call.respond(
							message = PlayerRouter.getPlayer(game = getGame(), alias = call.getAlias()).toJson(),
							status = HttpStatusCode.OK
						)
					}
					put {
						call.respond(
							message = PlayerRouter.updatePlayer(
								game = getGame(),
								alias = call.getAlias(),
								update = call.receiveOrNull()
							).toJson(),
							status = HttpStatusCode.OK
						)
					}
					route(path = "/ticks") {
						get {
							call.respond(
								message = TickRouter.getTicks(
									game = getGame(),
									alias = call.getAlias()
								).map { it.toJson() },
								status = HttpStatusCode.OK
							)
						}
						route(path = "/{tick}") {
							@KtorExperimentalAPI
							fun ApplicationCall.getTick() =
								parameters["tick"]?.toIntOrNull() ?: throw BadRequestException("Invalid Tick")

							get {
								call.respond(
									message = TickRouter.getTick(
										game = getGame(),
										alias = call.getAlias(),
										tick = call.getTick()
									).toJson(),
									status = HttpStatusCode.OK
								)
							}
						}
					}
				}
			}
			route(path = "/teams") {
				get {
					call.respond(
						message = TeamRouter.getTeams(game = getGame()).map { it.toJson() },
						status = HttpStatusCode.OK
					)
				}
				route(path = "/{name}") {
					@KtorExperimentalAPI
					fun ApplicationCall.getName() = parameters["name"] ?: throw BadRequestException("Invalid Name")

					get {
						call.respond(
							message = TeamRouter.getTeam(game = getGame(), name = call.getName()).toJson(),
							status = HttpStatusCode.OK
						)
					}
					put {
						call.respond(
							message = TeamRouter.updateTeam(
								game = getGame(),
								name = call.getName(),
								update = call.receiveOrNull()
							).toJson(),
							status = HttpStatusCode.OK
						)
					}
				}
			}
			route("/contributors") {
				get {
					val contributors = listOf(
						mapOf<String, Any?>(
							"Title" to "Macro303",
							"Image" to "macro303.png",
							"Role" to "Creator and Maintainer"
						).toSortedMap(),
						mapOf<String, Any?>(
							"Title" to "Miss. T",
							"Image" to "misst.jpg",
							"Role" to "Supporter and Loving Fiance"
						).toSortedMap(),
						mapOf<String, Any?>(
							"Title" to "Rocky",
							"Image" to "rocky.png",
							"Role" to "Quality Tester"
						).toSortedMap(),
						mapOf<String, Any?>(
							"Title" to "Img Bot App",
							"Image" to "imgbotapp.png",
							"Role" to "Image Processor"
						).toSortedMap()
					)
					call.respond(contributors)
				}
			}
		}
		static {
			defaultResource(resource = "/static/index.html")
			resources(resourcePackage = "static/images")
			resources(resourcePackage = "static/css")
			resources(resourcePackage = "static/js")
			resource(remotePath = "/navbar.html", resource = "static/navbar.html")
			resource(remotePath = "/players", resource = "static/players.html")
			resource(remotePath = "/players/{alias}", resource = "static/player.html")
			resource(remotePath = "/teams", resource = "static/teams.html")
			resource(remotePath = "/teams/{name}", resource = "static/team.html")
			resource(remotePath = "/about", resource = "static/about.html")
			resource(remotePath = "/Neptunes-Dashboard.yaml", resource = "static/Neptunes-Dashboard.yaml")
		}
		intercept(ApplicationCallPipeline.Fallback) {
			if (call.response.status() != null)
				application.log.info("${call.request.httpMethod.value.padEnd(4)}: ${call.response.status()} - ${call.request.uri}")
		}
	}
}

suspend fun ApplicationCall.respond(error: ErrorMessage) {
	if (request.local.uri.startsWith("/api"))
		respond(message = error, status = error.code)
	else
		respond(
			message = FreeMarkerContent(template = "exception.ftl", model = error),
			status = error.code
		)
	when {
		error.code.value < 100 -> application.log.error("${request.httpMethod.value.padEnd(6)}: ${error.code} - ${request.uri} => ${error.message}")
		error.code.value in (100 until 200) -> application.log.info("${request.httpMethod.value.padEnd(6)}: ${error.code} - ${request.uri} => ${error.message}")
		error.code.value in (200 until 300) -> application.log.info("${request.httpMethod.value.padEnd(6)}: ${error.code} - ${request.uri}")
		error.code.value in (300 until 400) -> application.log.debug("${request.httpMethod.value.padEnd(6)}: ${error.code} - ${request.uri} => ${error.message}")
		error.code.value in (400 until 500) -> application.log.warn("${request.httpMethod.value.padEnd(6)}: ${error.code} - ${request.uri} => ${error.message}")
		error.code.value >= 500 -> application.log.error("${request.httpMethod.value.padEnd(6)}: ${error.code} - ${request.uri} => ${error.message}")
	}
}