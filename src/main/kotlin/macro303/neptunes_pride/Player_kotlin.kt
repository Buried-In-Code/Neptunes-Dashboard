package macro303.neptunes_pride

import com.google.gson.annotations.SerializedName

import java.io.Serializable

/**
 * Created by Macro303 on 2018-04-17.
 */
internal class Player_kotlin : Serializable, Comparable<Player_kotlin> {
	@SerializedName("total_industry")
	internal var totalIndustry: Int = 0
	internal var regard: Int = 0
	@SerializedName("total_science")
	internal var totalScience: Int = 0
	internal var uid: Int = 0
	internal var ai: Int = 0
	internal var huid: Int = 0
	@SerializedName("total_stars")
	internal var totalStars: Int = 0
	@SerializedName("total_fleets")
	internal var totalFleets: Int = 0
	@SerializedName("total_strength")
	internal var totalStrength: Int = 0
	internal var alias: String? = null
	@SerializedName("tech")
	internal var playerTechnology: PlayerTechnology_kotlin? = null
	internal var avatar: Int = 0
	internal var conceded: Int = 0
	internal var ready: Int = 0
	@SerializedName("total_economy")
	internal var totalEconomy: Int = 0
	@SerializedName("missed_turns")
	internal var missedTurns: Int = 0
	@SerializedName("karma_to_give")
	internal var karma: Int = 0

	override fun compareTo(other: Player_kotlin): Int {
		return compareBy<Player_kotlin>(
			{ totalStrength },
			{ playerTechnology },
			{ totalStars },
			{ totalFleets },
			{ totalEconomy.plus(totalIndustry).plus(totalScience) },
			{ alias }).compare(this, other)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Player_kotlin) return false

		if (totalIndustry != other.totalIndustry) return false
		if (regard != other.regard) return false
		if (totalScience != other.totalScience) return false
		if (uid != other.uid) return false
		if (ai != other.ai) return false
		if (huid != other.huid) return false
		if (totalStars != other.totalStars) return false
		if (totalFleets != other.totalFleets) return false
		if (totalStrength != other.totalStrength) return false
		if (alias != other.alias) return false
		if (playerTechnology != other.playerTechnology) return false
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
		result = 31 * result + uid
		result = 31 * result + ai
		result = 31 * result + huid
		result = 31 * result + totalStars
		result = 31 * result + totalFleets
		result = 31 * result + totalStrength
		result = 31 * result + (alias?.hashCode() ?: 0)
		result = 31 * result + (playerTechnology?.hashCode() ?: 0)
		result = 31 * result + avatar
		result = 31 * result + conceded
		result = 31 * result + ready
		result = 31 * result + totalEconomy
		result = 31 * result + missedTurns
		result = 31 * result + karma
		return result
	}

	override fun toString(): String {
		return "Player(totalIndustry=$totalIndustry, regard=$regard, totalScience=$totalScience, uid=$uid, ai=$ai, huid=$huid, totalStars=$totalStars, totalFleets=$totalFleets, totalStrength=$totalStrength, alias=$alias, playerTechnology=$playerTechnology, avatar=$avatar, conceded=$conceded, ready=$ready, totalEconomy=$totalEconomy, missedTurns=$missedTurns, karma=$karma)"
	}
}