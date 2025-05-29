package github.buriedincode.routers.html

import github.buriedincode.Utils.transaction
import github.buriedincode.models.Game
import github.buriedincode.models.Player
import io.github.oshai.kotlinlogging.KotlinLogging
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse

object PlayerHtmlRouter : BaseHtmlRouter<Player>(entity = Player) {
  @JvmStatic private val LOGGER = KotlinLogging.logger {}

  private fun Context.getGame(): Game =
    this.pathParam("game-id").toLongOrNull()?.let {
      Game.findById(id = it) ?: throw NotFoundResponse(message = "Game not found")
    } ?: throw BadRequestResponse(message = "Invalid Game Id")

  override fun listEndpoint(ctx: Context): Unit = transaction {
    val game = ctx.getGame()
    val maxStats =
      mapOf(
        "economy" to game.players.maxOf { it.turns.first().economy },
        "industry" to game.players.maxOf { it.turns.first().industry },
        "science" to game.players.maxOf { it.turns.first().science },
        "ships" to game.players.maxOf { it.turns.first().ships },
        "stars" to game.players.maxOf { it.turns.first().stars },
      )
    val maxTechnology =
      mapOf(
        "banking" to game.players.maxOf { it.turns.first().banking },
        "manufacturing" to game.players.maxOf { it.turns.first().manufacturing },
        "propulsion" to game.players.maxOf { it.turns.first().propulsion },
        "research" to game.players.maxOf { it.turns.first().research },
        "scanning" to game.players.maxOf { it.turns.first().scanning },
        "terraforming" to game.players.maxOf { it.turns.first().terraforming },
        "weapons" to game.players.maxOf { it.turns.first().weapons },
      )
    ctx.render(
      filePath = "templates/$name/list.kte",
      model =
        mapOf(
          "games" to Game.all().toList(),
          "game" to game,
          "resources" to game.players.toList(),
          "maxStats" to maxStats,
          "maxTechnology" to maxTechnology,
        ),
    )
  }

  override fun viewEndpoint(ctx: Context): Unit = transaction {
    ctx.render(
      filePath = "templates/$name/view.kte",
      model = mapOf("games" to Game.all().toList(), "resource" to ctx.getResource()),
    )
  }
}
