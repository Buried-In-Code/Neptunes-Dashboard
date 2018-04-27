package macro303.neptunes_pride.player

import com.google.gson.annotations.SerializedName
import macro303.neptunes_pride.Connection
import macro303.neptunes_pride.technology.Technology

/**
 * Created by Macro303 on 2018-04-17.
 */
internal data class Player(
	@SerializedName(value = "ai") private val ai: Int,
	val alias: String,
	val avatar: Int,
	private val conceded: Int,
	val huid: Int,
	@SerializedName(value = "karma_to_give") val karma: Int,
	@SerializedName(value = "missed_turns") val missedTurns: Int,
	@SerializedName(value = "uid") val playerID: Int,
	private val ready: Int,
	val regard: Int,
	@SerializedName(value = "tech") val technologyMap: HashMap<String, Technology>,
	@SerializedName(value = "total_economy") val totalEconomy: Int,
	@SerializedName(value = "total_fleets") val totalFleets: Int,
	@SerializedName(value = "total_industry") val totalIndustry: Int,
	@SerializedName(value = "total_science") val totalScience: Int,
	@SerializedName(value = "total_strength") val totalShips: Int,
	@SerializedName(value = "total_stars") val totalStars: Int
) : Comparable<Player> {
	val name: String?
		get() = Connection.config.players.entries.firstOrNull {
			it.value.equals(alias, ignoreCase = true)
		}?.key
	val isAI: Boolean
		get() = ai == 1
	val hasConceded: Boolean
		get() = conceded == 1
	val isReady: Boolean
		get() = ready == 1

	private fun countTotalStats(): Int {
		return totalEconomy + totalIndustry + totalScience
	}

	private fun countTechLevels(): Int {
		return technologyMap.values.sumBy { it.level }
	}

	override fun compareTo(other: Player): Int {
		return compareBy<Player>(
			{ it.isAI },
			{ it.hasConceded },
			{ it.totalStars },
			{ it.totalShips },
			{ it.countTotalStats() },
			{ it.countTechLevels() },
			{ it.totalFleets },
			{ it.alias }
		).reversed().compare(this, other)
	}

	override fun toString(): String {
		return "Player(ai=$ai, alias='$alias', avatar=$avatar, conceded=$conceded, huid=$huid, karma=$karma, missedTurns=$missedTurns, playerID=$playerID, ready=$ready, regard=$regard, technologyMap=$technologyMap, totalEconomy=$totalEconomy, totalFleets=$totalFleets, totalIndustry=$totalIndustry, totalScience=$totalScience, totalShips=$totalShips, totalStars=$totalStars)"
	}

}