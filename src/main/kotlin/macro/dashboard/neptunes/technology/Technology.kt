package macro.dashboard.neptunes.technology

import macro.dashboard.neptunes.GeneralException
import macro.dashboard.neptunes.player.TurnTable

/**
 * Created by Macro303 on 2019-Mar-08.
 */
data class Technology(
	val ID: Int,
	val turnID: Int,
	val name: String,
	val value: Double,
	val level: Int
) : Comparable<Technology> {
	fun getTurn() = TurnTable.select(ID = turnID) ?: throw GeneralException()

	override fun compareTo(other: Technology): Int {
		return byTurn.then(byName).compare(this, other)
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
		internal val byTurn = compareBy(Technology::getTurn)
		internal val byName = compareBy(Technology::name)
	}
}