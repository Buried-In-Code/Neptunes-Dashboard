package macro303.neptunes_pride

import com.google.gson.annotations.SerializedName

/**
 * Created by Macro303 on 2018-04-17.
 */
class Player : Comparable<Player> {
	@SerializedName("total_industry")
	var totalIndustry: Int = 0
		private set
	var regard: Int = 0
		private set
	@SerializedName("total_science")
	var totalScience: Int = 0
		private set
	@SerializedName("uid")
	var playerID: Int = 0
		private set
	@SerializedName("ai")
	var ai: Int = 0
		private set
	@SerializedName("huid")
	var huid: Int = 0
		private set
	@SerializedName("total_stars")
	var totalStars: Int = 0
		private set
	@SerializedName("total_fleets")
	var totalFleets: Int = 0
		private set
	@SerializedName("total_strength")
	var totalStrength: Int = 0
		private set
	var alias: String = ""
		private set
	@SerializedName("tech")
	var technologyMap: HashMap<String, Technology> = HashMap()
		private set
	var avatar: Int = 0
		private set
	var conceded: Int = 0
		private set
	var ready: Int = 0
		private set
	@SerializedName("total_economy")
	var totalEconomy: Int = 0
		private set
	@SerializedName("missed_turns")
	var missedTurns: Int = 0
		private set
	@SerializedName("karma_to_give")
	var karma: Int = 0
		private set

	val name: String?
		get() = Connection.configProperty.value.players.entries.firstOrNull {
			it.value.equals(alias, ignoreCase = true)
		}?.value

	private fun countTotalStats(): Int {
		return totalEconomy + totalIndustry + totalScience
	}

	private fun countTechLevels(): Int {
		return technologyMap.values.sumBy { it.level }
	}

	override fun compareTo(other: Player): Int {
		return compareBy<Player>(
			{ it.totalStrength },
			{ it.countTotalStats() },
			{ it.totalStars },
			{ it.totalFleets },
			{ it.countTechLevels() },
			{ it.alias }
		).reversed().compare(this, other)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Player) return false

		if (totalIndustry != other.totalIndustry) return false
		if (regard != other.regard) return false
		if (totalScience != other.totalScience) return false
		if (playerID != other.playerID) return false
		if (ai != other.ai) return false
		if (huid != other.huid) return false
		if (totalStars != other.totalStars) return false
		if (totalFleets != other.totalFleets) return false
		if (totalStrength != other.totalStrength) return false
		if (alias != other.alias) return false
		if (technologyMap != other.technologyMap) return false
		if (avatar != other.avatar) return false
		if (conceded != other.conceded) return false
		if (ready != other.ready) return false
		if (totalEconomy != other.totalEconomy) return false
		if (missedTurns != other.missedTurns) return false
		if (karma != other.karma) return false

		return true
	}

	override fun hashCode(): Int {
		var result = totalIndustry
		result = 31 * result + regard
		result = 31 * result + totalScience
		result = 31 * result + playerID
		result = 31 * result + ai
		result = 31 * result + huid
		result = 31 * result + totalStars
		result = 31 * result + totalFleets
		result = 31 * result + totalStrength
		result = 31 * result + alias.hashCode()
		result = 31 * result + technologyMap.hashCode()
		result = 31 * result + avatar
		result = 31 * result + conceded
		result = 31 * result + ready
		result = 31 * result + totalEconomy
		result = 31 * result + missedTurns
		result = 31 * result + karma
		return result
	}

	override fun toString(): String {
		return "Player(totalIndustry=$totalIndustry, regard=$regard, totalScience=$totalScience, playerID=$playerID, ai=$ai, huid=$huid, totalStars=$totalStars, totalFleets=$totalFleets, totalStrength=$totalStrength, alias='$alias', technologyMap=$technologyMap, avatar=$avatar, conceded=$conceded, ready=$ready, totalEconomy=$totalEconomy, missedTurns=$missedTurns, karma=$karma)"
	}

}