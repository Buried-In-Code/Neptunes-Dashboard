package macro303.neptunes_pride;

import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

class AllPlayers {
	@SerializedName(value = "0")
	private Player player0 = null;
	@SerializedName(value = "1")
	private Player player1 = null;
	@SerializedName(value = "2")
	private Player player2 = null;
	@SerializedName(value = "3")
	private Player player3 = null;
	@SerializedName(value = "4")
	private Player player4 = null;
	@SerializedName(value = "5")
	private Player player5 = null;
	@SerializedName(value = "6")
	private Player player6 = null;
	@SerializedName(value = "7")
	private Player player7 = null;
	@SerializedName(value = "8")
	private Player player8 = null;
	@SerializedName(value = "9")
	private Player player9 = null;
	@SerializedName(value = "10")
	private Player player10 = null;
	@SerializedName(value = "11")
	private Player player11 = null;
	@SerializedName(value = "12")
	private Player player12 = null;
	@SerializedName(value = "13")
	private Player player13 = null;
	@SerializedName(value = "14")
	private Player player14 = null;
	@SerializedName(value = "15")
	private Player player15 = null;
	@SerializedName(value = "16")
	private Player player16 = null;
	@SerializedName(value = "17")
	private Player player17 = null;
	@SerializedName(value = "18")
	private Player player18 = null;
	@SerializedName(value = "19")
	private Player player19 = null;
	@SerializedName(value = "20")
	private Player player20 = null;
	@SerializedName(value = "21")
	private Player player21 = null;
	@SerializedName(value = "22")
	private Player player22 = null;
	@SerializedName(value = "23")
	private Player player23 = null;

	TreeSet<Player> getPlayers() {
		List<Player> temp = Arrays.asList(
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
		);
		return new TreeSet<>(temp);
	}
}