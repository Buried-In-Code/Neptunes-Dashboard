package macro.dashboard.neptunes.config

/**
 * Created by Macro303 on 2019-Sep-09
 */
internal class Game {
	var id: Long = 1L
	var code: String = ""
	var cycle: Int = 1

	override fun toString(): String {
		return "Game(id=$id, code=$code, cycle=$cycle)"
	}

}