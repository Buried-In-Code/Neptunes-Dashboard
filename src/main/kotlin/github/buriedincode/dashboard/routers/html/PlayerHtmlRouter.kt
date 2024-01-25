package github.buriedincode.dashboard.routers.html

import github.buriedincode.dashboard.Utils
import github.buriedincode.dashboard.models.Player
import github.buriedincode.dashboard.models.Game
import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.NotFoundResponse
import org.apache.logging.log4j.kotlin.Logging

object PlayerHtmlRouter : BaseHtmlRouter<Player>(entity = Player), Logging {
    private fun Context.getGame(): Game {
        return this.pathParam("game-id").toLongOrNull()?.let {
            Game.findById(id = it) ?: throw NotFoundResponse(message = "Game not found")
        } ?: throw BadRequestResponse(message = "Invalid Game Id")
    }

    override fun listEndpoint(ctx: Context) {
        Utils.query {
            val game = ctx.getGame()
            val maxStats = mapOf(
                "economy" to game.players.maxOf { it.turns.first().economy },
                "industry" to game.players.maxOf { it.turns.first().industry },
                "science" to game.players.maxOf { it.turns.first().science },
                "ships" to game.players.maxOf { it.turns.first().ships },
                "stars" to game.players.maxOf { it.turns.first().stars },
            )
            val maxTechnology = mapOf(
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
                model = mapOf(
                    "games" to Game.all().toList(),
                    "game" to game,
                    "resources" to game.players.toList(),
                    "maxStats" to maxStats,
                    "maxTechnology" to maxTechnology,
                ),
            )
        }
    }
}
