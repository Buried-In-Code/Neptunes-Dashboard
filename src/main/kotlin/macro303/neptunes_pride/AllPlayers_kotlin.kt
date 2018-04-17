package macro303.neptunes_pride

import com.google.gson.annotations.SerializedName
import java.util.*

internal class AllPlayers_kotlin {
	@JvmField
	@SerializedName(value = "0")
	internal var player0: Player_kotlin? = null
	@SerializedName(value = "1")
	internal var player1: Player_kotlin? = null
	@SerializedName(value = "2")
	internal var player2: Player_kotlin? = null
	@SerializedName(value = "3")
	internal var player3: Player_kotlin? = null
	@SerializedName(value = "4")
	internal var player4: Player_kotlin? = null
	@SerializedName(value = "5")
	internal var player5: Player_kotlin? = null
	@SerializedName(value = "6")
	internal var player6: Player_kotlin? = null
	@SerializedName(value = "7")
	internal var player7: Player_kotlin? = null
	@SerializedName(value = "8")
	internal var player8: Player_kotlin? = null
	@SerializedName(value = "9")
	internal var player9: Player_kotlin? = null
	@SerializedName(value = "10")
	internal var player10: Player_kotlin? = null
	@SerializedName(value = "11")
	internal var player11: Player_kotlin? = null
	@SerializedName(value = "12")
	internal var player12: Player_kotlin? = null
	@SerializedName(value = "13")
	internal var player13: Player_kotlin? = null
	@SerializedName(value = "14")
	internal var player14: Player_kotlin? = null
	@SerializedName(value = "15")
	internal var player15: Player_kotlin? = null
	@SerializedName(value = "16")
	internal var player16: Player_kotlin? = null
	@SerializedName(value = "17")
	internal var player17: Player_kotlin? = null
	@SerializedName(value = "18")
	internal var player18: Player_kotlin? = null
	@SerializedName(value = "19")
	internal var player19: Player_kotlin? = null
	@SerializedName(value = "20")
	internal var player20: Player_kotlin? = null
	@SerializedName(value = "21")
	internal var player21: Player_kotlin? = null
	@SerializedName(value = "22")
	internal var player22: Player_kotlin? = null
	@SerializedName(value = "23")
	internal var player23: Player_kotlin? = null

	internal val players: TreeSet<Player_kotlin>
		get() {
			val players = listOf(
				player0,
				player1,
				player2,
				player3,
				player4,
				player5,
				player6,
				player7,
				player8,
				player9,
				player10,
				player11,
				player12,
				player13,
				player14,
				player15,
				player16,
				player17,
				player18,
				player19,
				player20,
				player21,
				player22,
				player23
			)
			return TreeSet(players.filterNotNull())
		}
}