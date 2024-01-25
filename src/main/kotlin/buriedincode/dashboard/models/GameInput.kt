package github.buriedincode.dashboard.models

import github.buriedincode.dashboard.Utils.asEnumOrNull

data class GameInput(
    val gameId: Long,
    val apiKey: String,
    val type: String,
) {
    val typeEnum: GameType
        get() = this.type.asEnumOrNull<GameType>() ?: GameType.TRITON
}
