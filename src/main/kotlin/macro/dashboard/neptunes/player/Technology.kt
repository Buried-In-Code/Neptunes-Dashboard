package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.GeneralException
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2019-Mar-08.
 */
data class Technology(
	val ID: Int,
	val turnID: Int,
	val name: String,
	val value: Double,
	val level: Int
) {

	val turn: Turn by lazy {
		TurnTable.select(ID = turnID) ?: throw GeneralException()
	}

	fun toOutput(): Map<String, Any?> {
		val output = mapOf(
			"ID" to ID,
			"turn" to turnID,
			"name" to name,
			"value" to value,
			"level" to level
		).toMutableMap()
		return output.toSortedMap()
	}

	companion object {
		private val LOGGER = LoggerFactory.getLogger(this::class.java)
	}
}