package macro.dashboard.neptunes.player

import macro.dashboard.neptunes.GeneralException
import org.slf4j.LoggerFactory

/**
 * Created by Macro303 on 2019-Mar-08.
 */
data class Technology(
	val ID: Int,
	val cycleID: Int,
	val name: String,
	val value: Double,
	val level: Int
) {

	val cycle by lazy {
		CycleTable.select(ID = cycleID) ?: throw GeneralException()
	}

	fun toOutput(): Map<String, Any?> {
		val output = mapOf(
			"ID" to ID,
			"cycle" to cycleID,
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