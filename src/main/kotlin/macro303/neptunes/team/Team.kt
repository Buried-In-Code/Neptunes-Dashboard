package macro303.neptunes.team

import macro303.neptunes.player.Player
import java.util.*

/**
 * Created by Macro303 on 2018-05-10.
 */
class Team : Comparable<Team> {
	private val members = ArrayList<Player>()

	val isLost: Boolean
		get() {
			return members.stream().allMatch({ it.ai || it.conceded })
		}

	val name: String
		get() = if (members.size > 0) members[0].team else "Unknown"

	val totalShips: Int
		get() = members.stream().filter { member -> !member.ai || !member.conceded }.mapToInt({ it.totalShips }).sum()

	val totalStars: Int
		get() = members.stream().filter { member -> !member.ai || !member.conceded }.mapToInt({ it.totalStars }).sum()

	val totalEconomy: Int
		get() = members.stream().filter { member -> !member.ai || !member.conceded }.mapToInt({ it.totalEconomy }).sum()

	val economyTurn: Int
		get() = members.stream().filter { member -> !member.ai || !member.conceded }.mapToInt({ it.economyTurn }).sum()

	val totalIndustry: Int
		get() = members.stream().filter { member -> !member.ai || !member.conceded }.mapToInt({ it.totalIndustry }).sum()

	val industryTurn: Int
		get() = members.stream().filter { member -> !member.ai || !member.conceded }.mapToInt({ it.industryTurn }).sum()

	val totalScience: Int
		get() = members.stream().filter { member -> !member.ai || !member.conceded }.mapToInt({ it.totalScience }).sum()

	fun addMember(player: Player) {
		members.add(player)
	}

	override fun compareTo(other: Team): Int {
		if (totalStars.compareTo(other.totalStars) != 0)
			return totalStars.compareTo(other.totalStars)
		if (totalShips.compareTo(other.totalShips) != 0)
			return totalShips.compareTo(other.totalShips)
		if ((totalEconomy + totalIndustry + totalScience).compareTo(other.totalEconomy + totalIndustry + totalScience) != 0)
			return (totalEconomy + totalIndustry + totalScience).compareTo(other.totalEconomy + totalIndustry + totalScience)
		return name.compareTo(other.name, ignoreCase = true)
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Team) return false

		if (members != other.members) return false

		return true
	}

	override fun hashCode(): Int {
		return members.hashCode()
	}

	override fun toString(): String {
		return "Team(members=$members)"
	}
}