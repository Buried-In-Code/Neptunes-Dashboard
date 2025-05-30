package github.buriedincode.models

import github.buriedincode.Utils.asEnumOrNull

data class GameInput(val gameId: Long, val apiKey: String, val type: String) {
  val typeEnum: GameType
    get() = this.type.asEnumOrNull<GameType>() ?: GameType.TRITON
}
