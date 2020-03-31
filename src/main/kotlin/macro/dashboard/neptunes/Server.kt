package macro.dashboard.neptunes

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.gson.GsonConverter
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.respond
import io.ktor.routing.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.toMap
import macro.dashboard.neptunes.config.Config
import macro.dashboard.neptunes.game.Game
import macro.dashboard.neptunes.game.GameTable
import macro.dashboard.neptunes.player.Player
import macro.dashboard.neptunes.player.PlayerTable
import macro.dashboard.neptunes.tick.Tick
import macro.dashboard.neptunes.tick.TickTable
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

/**
 * Created by Macro303 on 2018-Nov-08.
 */
object Server {
	private val LOGGER = LogManager.getLogger(Server::class.java)

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
		register(contentType = ContentType.Application.Json, converter = GsonConverter(gson = Util.GSON))
	}
	install(DefaultHeaders) {
		header(name = "Developer", value = "Macro303")
		header(name = HttpHeaders.Server, value = "Neptunes-Dashboard")
		header(name = HttpHeaders.AcceptLanguage, value = "en-NZ")
		header(name = HttpHeaders.ContentLanguage, value = "en-NZ")
	}
	install(ConditionalHeaders)
	install(AutoHeadResponse)
	install(StatusPages) {
		exception<Throwable> {
			application.log.error(it.message)
			call.respond(
				TextContent(
					text = "Message: ${it.message}",
					contentType = ContentType.Text.Plain.withCharset(Charsets.UTF_8),
					status = HttpStatusCode.BadRequest
				)
			)
		}
		status(HttpStatusCode.NotFound) {
			call.respond(
				TextContent(
					text = "${it.value} ${it.description}",
					contentType = ContentType.Text.Plain.withCharset(Charsets.UTF_8),
					status = HttpStatusCode.NotFound
				)
			)
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
			route(path = "/games") {
				get {
					newSuspendedTransaction(db = Util.database) {
						call.respond(Game.all().sorted().map {
							it.toJson(full = true)
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
						newSuspendedTransaction(db = Util.database) {
							call.respond(call.getGame().toJson(full = true))
						}
					}
					post {
						val gameId = call.parameters["gameID"]?.toLongOrNull()
							?: throw BadRequestException("Invalid Game ID")
						val gameCode = call.request.queryParameters["code"]
							?: throw BadRequestException("Invalid Game Code")
						newSuspendedTransaction(db = Util.database) {
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
									message = game.toJson(full = true),
									status = HttpStatusCode.Created
								)
							}
						}
					}
					put {
						newSuspendedTransaction(db = Util.database) {
							var game = call.getGame()
							Triton.getGame(gameId = game.id.value, code = game.code)
							game = Game.findById(game.id) ?: throw NotFoundException("Unable to find Game")
							call.respond(
								message = game.toJson(full = true),
								status = HttpStatusCode.Accepted
							)
						}
					}
					route(path = "/players") {
						get {
							newSuspendedTransaction(db = Util.database) {
								call.respond(call.getGame().players.map { it.toJson(full = true) })
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
								newSuspendedTransaction(db = Util.database) {
									call.respond(call.getPlayer().toJson(full = true))
								}
							}
							put {
								newSuspendedTransaction(db = Util.database) {
									val player = call.getPlayer()
									val request = call.receiveOrNull<PlayerRequest>()
										?: throw BadRequestException("Invalid Player Data")
									player.name = request.name
									player.team = request.team
									call.respond(
										message = player.toJson(full = true),
										status = HttpStatusCode.Accepted
									)
								}
							}
							route(path = "/ticks") {
								get {
									newSuspendedTransaction(db = Util.database) {
										call.respond(call.getPlayer().ticks.map { it.toJson(full = true) })
									}
								}

								route(path = "/{tickID}") {
									fun ApplicationCall.getTick(): Tick {
										val gameId = getGame().id
										val playerId = getPlayer().id
										val tickId = parameters["tickID"]?.toLongOrNull()
											?: throw BadRequestException("Invalid Tick ID")
										return Tick.find {
											TickTable.id eq tickId and (TickTable.playerCol eq playerId) and (TickTable.gameCol eq gameId)
										}.limit(1).firstOrNull() ?: throw NotFoundException("Unable to find Tick")
									}

									get {
										newSuspendedTransaction(db = Util.database) {
											call.respond(call.getTick().toJson(full = true))
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
		}
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
			if (call.response.status() != null) {
				when (call.response.status()) {
					HttpStatusCode.OK -> application.log.info("${call.request.httpMethod.value.padEnd(4)}: ${call.response.status()} - ${call.request.uri}")
					HttpStatusCode.Accepted -> application.log.info("${call.request.httpMethod.value.padEnd(4)}: ${call.response.status()} - ${call.request.uri}")
					HttpStatusCode.Created -> application.log.info("${call.request.httpMethod.value.padEnd(4)}: ${call.response.status()} - ${call.request.uri}")
					HttpStatusCode.Conflict -> application.log.warn("${call.request.httpMethod.value.padEnd(4)}: ${call.response.status()} - ${call.request.uri}")
				}
			}
		}
	}
}

fun Parameters.lowerCase(): Map<String, String?> = this.toMap()
	.mapKeys { it.key.toLowerCase() }
	.mapValues { it.value.firstOrNull() }

data class PlayerRequest(val name: String?, val team: String?)