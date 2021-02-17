package macro.dashboard

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.GsonConverter
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.defaultResource
import io.ktor.http.content.resource
import io.ktor.http.content.resources
import io.ktor.http.content.static
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.accept
import io.ktor.routing.route
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import macro.dashboard.Server.LOGGER
import macro.dashboard.config.Config
import macro.dashboard.v2.Endpoints.v2Routes
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager

/**
 * Created by Macro303 on 2018-Nov-08.
 */
object Server {
	val LOGGER = LogManager.getLogger(Server::class.java)

	init {
		checkLogLevels()
		LOGGER.info("Welcome to Neptune's Dashboard")
	}

	private fun checkLogLevels() {
		Level.values().sorted().forEach {
			LOGGER.log(it, "{} is Visible", it.name())
		}
	}

	@KtorExperimentalAPI
	@JvmStatic
	fun main(args: Array<String>) {
		embeddedServer(
			Netty,
			port = Config.INSTANCE.server.port ?: 6790,
			host = Config.INSTANCE.server.hostName ?: "localhost",
			module = Application::module
		).apply { start(wait = true) }
	}
}

@KtorExperimentalAPI
fun Application.module() {
	install(ContentNegotiation) {
		register(contentType = ContentType.Application.Json, converter = GsonConverter(gson = Utils.GSON))
	}
	install(DefaultHeaders) {
		header(name = "Developer", value = "Macro303")
		header(name = HttpHeaders.Server, value = "Neptunes-Dashboard")
		header(name = HttpHeaders.AcceptLanguage, value = "en-NZ")
		header(name = HttpHeaders.ContentLanguage, value = "en-NZ")
	}
	install(ConditionalHeaders)
	install(AutoHeadResponse)
	install(Compression)
	install(XForwardedHeaderSupport)
	install(StatusPages) {
		exception<Throwable> {
			it.printStackTrace()
			call.respond(
				message = sortedMapOf<String, Any?>(
					"value" to it.localizedMessage,
					"error" to HttpStatusCode.InternalServerError.description
				),
				status = HttpStatusCode.InternalServerError
			)

			callLog(ctx = call, status = HttpStatusCode.InternalServerError)
		}
		exception<UnsupportedMediaTypeException> {
			call.respond(
				message = sortedMapOf<String, Any?>(
					"value" to it.localizedMessage,
					"error" to HttpStatusCode.UnsupportedMediaType.description
				),
				status = HttpStatusCode.UnsupportedMediaType
			)
			callLog(ctx = call, status = HttpStatusCode.UnsupportedMediaType)
		}
		exception<NotFoundException> {
			call.respond(
				message = sortedMapOf<String, Any?>(
					"value" to it.localizedMessage,
					"error" to HttpStatusCode.NotFound.description
				),
				status = HttpStatusCode.NotFound
			)
			callLog(ctx = call, status = HttpStatusCode.NotFound)
		}
		exception<BadRequestException> {
			call.respond(
				message = sortedMapOf<String, Any?>(
					"value" to it.localizedMessage,
					"error" to HttpStatusCode.BadRequest.description
				),
				status = HttpStatusCode.BadRequest
			)
			callLog(ctx = call, status = HttpStatusCode.BadRequest)
		}
		exception<NotImplementedException> {
			call.respond(
				message = sortedMapOf<String, Any?>(
					"value" to it.localizedMessage,
					"error" to HttpStatusCode.NotImplemented.description
				),
				status = HttpStatusCode.NotImplemented
			)
			callLog(ctx = call, status = HttpStatusCode.NotImplemented)
		}
		exception<ConflictException> {
			call.respond(
				message = sortedMapOf<String, Any?>(
					"value" to it.localizedMessage,
					"error" to HttpStatusCode.Conflict.description
				),
				status = HttpStatusCode.Conflict
			)
			callLog(ctx = call, status = HttpStatusCode.Conflict)
		}
		status(HttpStatusCode.NotFound) {
			call.respond(
				message = sortedMapOf<String, Any?>(
					"value" to "Unable to find resource",
					"error" to HttpStatusCode.NotFound.description
				),
				status = HttpStatusCode.NotFound
			)
			callLog(ctx = call, status = HttpStatusCode.NotFound)
		}
	}
	intercept(ApplicationCallPipeline.Monitoring) {
		application.log.debug(">> ${call.request.httpVersion} ${call.request.httpMethod.value} ${call.request.uri}, Accept: ${call.request.accept()}, Content-Type: ${call.request.contentType()}, User-Agent: ${call.request.userAgent()}, Host: ${call.request.origin.remoteHost}:${call.request.port()}")
	}
	install(Routing) {
		intercept(ApplicationCallPipeline.Setup) {
			application.log.debug("==> ${call.request.httpVersion} ${call.request.httpMethod.value} ${call.request.uri}, Accept: ${call.request.accept()}, Content-Type: ${call.request.contentType()}, User-Agent: ${call.request.userAgent()}")
		}
		trace {
			application.log.trace(it.buildText())
		}
		accept(ContentType.Application.Json) {
			route(path = "/api") {
				v2Routes()
			}
		}
		/*route(path = "/api") {
			route(path = "/games") {
				get {
					newSuspendedTransaction(db = Utils.database) {
						call.respond(Game.all().sorted().map {
							it.toJson(filterKeys = true)
						})
					}
				}
				route(path = "/{gameID}") {
					fun ApplicationCall.getGame(): Game {
						if (parameters["gameID"].equals("latest"))
							return Game.all().orderBy(GameTable.startTimeCol to SortOrder.DESC).limit(1).firstOrNull()
								?: throw NotFoundException("Unable to find Game")
						val gameId = parameters["gameID"]?.toLongOrNull()
							?: throw BadRequestException("Invalid Game ID")
						return Game.findById(gameId) ?: throw NotFoundException("Unable to find Game")
					}

					get {
						newSuspendedTransaction(db = Utils.database) {
							call.respond(call.getGame().toJson(filterKeys = true))
						}
					}
					post {
						val gameId = call.parameters["gameID"]?.toLongOrNull()
							?: throw BadRequestException("Invalid Game ID")
						val gameCode = call.request.queryParameters["code"]
							?: throw BadRequestException("Invalid Game Code")
						newSuspendedTransaction(db = Utils.database) {
							var game = Game.findById(gameId)
							if (game != null) {
								call.respond(
									message = "Game with ID already Exists",
									status = HttpStatusCode.Conflict
								)
							} else {
								Triton.getGame(gameId = gameId, code = gameCode)
								game = Game.findById(gameId) ?: throw NotFoundException("Unable to find Game")
								call.respond(
									message = game.toJson(filterKeys = true),
									status = HttpStatusCode.Created
								)
							}
						}
					}
					put {
						newSuspendedTransaction(db = Utils.database) {
							var game = call.getGame()
							Triton.getGame(gameId = game.id.value, code = game.code)
							game = Game.findById(game.id) ?: throw NotFoundException("Unable to find Game")
							call.respond(
								message = game.toJson(filterKeys = true),
								status = HttpStatusCode.Accepted
							)
						}
					}
					route(path = "/players") {
						get {
							newSuspendedTransaction(db = Utils.database) {
								call.respond(call.getGame().players.map { it.toJson(filterKeys = true) })
							}
						}
						route(path = "/{alias}") {
							fun ApplicationCall.getPlayer(): Player {
								val gameId = getGame().id
								val playerAlias = parameters["alias"]
									?: throw BadRequestException("Invalid Player Alias")
								return Player.find {
									PlayerTable.aliasCol eq playerAlias and (PlayerTable.gameCol eq gameId)
								}.limit(1).firstOrNull() ?: throw NotFoundException("Unable to find Player")
							}

							get {
								newSuspendedTransaction(db = Utils.database) {
									call.respond(call.getPlayer().toJson(filterKeys = true))
								}
							}
							put {
								newSuspendedTransaction(db = Utils.database) {
									val player = call.getPlayer()
									val request = call.receiveOrNull<PlayerRequest>()
										?: throw BadRequestException("Invalid Player Data")
									player.name = request.name
									player.team = request.team
									call.respond(
										message = player.toJson(filterKeys = true),
										status = HttpStatusCode.Accepted
									)
								}
							}
							route(path = "/ticks") {
								get {
									newSuspendedTransaction(db = Utils.database) {
										call.respond(call.getPlayer().ticks.map { it.toJson(filterKeys = true) })
									}
								}

								route(path = "/{tickID}") {
									fun ApplicationCall.getTick(): Turn {
										val gameId = getGame().id
										val playerId = getPlayer().id
										val tickId = parameters["tickID"]?.toLongOrNull()
											?: throw BadRequestException("Invalid Turn ID")
										return Turn.find {
											TurnTable.id eq tickId and (TurnTable.playerCol eq playerId) and (TurnTable.gameCol eq gameId)
										}.limit(1).firstOrNull() ?: throw NotFoundException("Unable to find Turn")
									}

									get {
										newSuspendedTransaction(db = Utils.database) {
											call.respond(call.getTick().toJson(filterKeys = true))
										}
									}
								}
							}
						}
					}
				}
			}
			route("/contributors") {
				get {
					val contributors = listOf(
						mapOf<String, Any?>(
							"Title" to "Macro303",
							"Role" to "Creator and Maintainer"
						).toSortedMap(),
						mapOf<String, Any?>(
							"Title" to "Miss. T",
							"Role" to "Supporter and Loving Wife"
						).toSortedMap(),
						mapOf<String, Any?>(
							"Title" to "Rocky",
							"Role" to "Quality Tester"
						).toSortedMap(),
						mapOf<String, Any?>(
							"Title" to "Img Bot App",
							"Role" to "Image Processor"
						).toSortedMap(),
						mapOf<String, Any?>(
							"Title" to "Dependabot App",
							"Role" to "Automated dependency updates"
						).toSortedMap()
					)
					call.respond(contributors)
				}
			}
		}*/
		static {
			defaultResource(resource = "/static/index.html")
			resources(resourcePackage = "static/images")
			resources(resourcePackage = "static/css")
			resources(resourcePackage = "static/js")
			resource(remotePath = "/navbar.html", resource = "static/navbar.html")
			resource(remotePath = "/players", resource = "static/players.html")
			resource(remotePath = "/players/{alias}", resource = "static/player.html")
			resource(remotePath = "/about", resource = "static/about.html")
			resource(remotePath = "/Neptunes-Dashboard.yaml", resource = "static/Neptunes-Dashboard.yaml")
		}
		intercept(ApplicationCallPipeline.Fallback) {
			callLog(ctx = call, status = call.response.status())
		}
	}
}

fun callLog(ctx: ApplicationCall, status: HttpStatusCode? = null) {
	when (status?.value) {
		in 100..199 -> LOGGER.warn("${ctx.request.httpMethod.value.padEnd(4)}: $status - ${ctx.request.uri}")
		in 200..299 -> LOGGER.info("${ctx.request.httpMethod.value.padEnd(4)}: $status - ${ctx.request.uri}")
		in 300..399 -> LOGGER.info("${ctx.request.httpMethod.value.padEnd(4)}: $status - ${ctx.request.uri}")
		in 400..499 -> LOGGER.warn("${ctx.request.httpMethod.value.padEnd(4)}: $status - ${ctx.request.uri}")
		in 500..599 -> LOGGER.error("${ctx.request.httpMethod.value.padEnd(4)}: $status - ${ctx.request.uri}")
		else -> LOGGER.error("${ctx.request.httpMethod.value.padEnd(4)}: $status - ${ctx.request.uri}")
	}
}

data class PlayerRequest(val name: String?, val team: String?)