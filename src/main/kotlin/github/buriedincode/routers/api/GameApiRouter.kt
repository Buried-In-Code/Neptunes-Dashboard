package github.buriedincode.routers.api

import github.buriedincode.Utils.transaction
import github.buriedincode.models.Game
import github.buriedincode.models.GameInput
import github.buriedincode.models.GameType
import github.buriedincode.models.Player
import github.buriedincode.models.PlayerTable
import github.buriedincode.models.Turn
import github.buriedincode.models.TurnTable
import github.buriedincode.services.Triton
import github.buriedincode.services.triton.ScanData
import io.github.oshai.kotlinlogging.KotlinLogging
import io.javalin.http.BadRequestResponse
import io.javalin.http.ConflictResponse
import io.javalin.http.Context
import io.javalin.http.HttpStatus
import io.javalin.http.bodyAsClass
import org.jetbrains.exposed.sql.and

object GameApiRouter : BaseApiRouter<Game>(entity = Game) {
  @JvmStatic private val LOGGER = KotlinLogging.logger {}

  override fun listEndpoint(ctx: Context): Unit = transaction {
    val resources = Game.all().toList()
    ctx.json(resources.sorted().map { it.toJson() })
  }

  override fun createEndpoint(ctx: Context): Unit = transaction {
    val body = ctx.bodyAsClass<GameInput>()
    val exists = Game.findById(body.gameId)
    if (exists != null) {
      throw ConflictResponse(message = "Game already exists.")
    }
    val response: ScanData =
      if (body.typeEnum == GameType.TRITON) {
        Triton.getGame(gameId = body.gameId, apiKey = body.apiKey)
          ?: throw BadRequestResponse(message = "Unable to load Game.")
      } else {
        TODO("Type not yet supported")
      }
    val resource =
      Game.new(id = body.gameId) {
        this.apiKey = body.apiKey
        this.tickRate = response.tickRate
        this.name = response.name
        this.type = body.typeEnum
        this.starsForVictory = response.starsForVictory
        this.totalStars = response.totalStars
        this.tradeCost = response.tradeCost
        this.isTradeScanOnly = response.isTradeScanOnly
        this.isTurnBased = response.isTurnBased
        this.isPaused = response.isPaused
        this.isGameOver = response.isGameOver
        this.isStarted = response.isStarted
        this.startTime = response.startTime
        this.turn = response.turn
        this.nextTurn = response.nextTurn
      }
    response.players
      .filterNot { it.value.username.isBlank() }
      .forEach { (_, data) ->
        val player =
          Player.new {
            this.game = resource
            this.playerId = data.uid
            this.isActive = data.isActive
            this.username = data.username
          }
        Turn.new {
          this.player = player
          this.turn = resource.turn
          this.industry = data.totalIndustry
          this.economy = data.totalEconomy
          this.science = data.totalScience
          this.stars = data.totalStars
          this.ships = data.totalStrength
          this.scanning = data.tech["scanning"]!!.level
          this.propulsion = data.tech["propulsion"]!!.level
          this.terraforming = data.tech["terraforming"]!!.level
          this.research = data.tech["research"]!!.level
          this.weapons = data.tech["weapons"]!!.level
          this.banking = data.tech["banking"]!!.level
          this.manufacturing = data.tech["manufacturing"]!!.level
        }
      }

    ctx.status(HttpStatus.CREATED).json(resource.toJson(showAll = true))
  }

  override fun updateEndpoint(ctx: Context): Unit = transaction {
    val resource = ctx.getResource()
    val response: ScanData =
      if (resource.type == GameType.TRITON) {
        Triton.getGame(gameId = resource.id.value, apiKey = resource.apiKey)
          ?: throw BadRequestResponse(message = "Unable to load Game.")
      } else {
        TODO("Type not yet supported")
      }
    resource.isPaused = response.isPaused
    resource.isGameOver = response.isGameOver
    resource.isStarted = response.isStarted
    resource.startTime = response.startTime
    resource.turn = response.turn
    resource.nextTurn = response.nextTurn
    response.players
      .filterNot { it.value.username.isBlank() }
      .forEach { (_, data) ->
        val player =
          Player.find { (PlayerTable.gameCol eq resource.id) and (PlayerTable.playerIdCol eq data.uid) }.firstOrNull()
            ?: Player.new {
              this.game = resource
              this.playerId = data.uid
            }
        player.isActive = data.isActive
        player.username = data.username
        Turn.find { (TurnTable.playerCol eq player.id) and (TurnTable.turnCol eq resource.turn) }.firstOrNull()
          ?: Turn.new {
            this.player = player
            this.turn = resource.turn
            this.industry = data.totalIndustry
            this.economy = data.totalEconomy
            this.science = data.totalScience
            this.stars = data.totalStars
            this.ships = data.totalStrength
            this.scanning = data.tech["scanning"]!!.level
            this.propulsion = data.tech["propulsion"]!!.level
            this.terraforming = data.tech["terraforming"]!!.level
            this.research = data.tech["research"]!!.level
            this.weapons = data.tech["weapons"]!!.level
            this.banking = data.tech["banking"]!!.level
            this.manufacturing = data.tech["manufacturing"]!!.level
          }
      }

    ctx.json(resource.toJson(showAll = true))
  }
}
