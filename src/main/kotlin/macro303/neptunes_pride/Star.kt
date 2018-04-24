package macro303.neptunes_pride

import com.google.gson.annotations.SerializedName

internal class Star : Comparable<Star> {
	@SerializedName("uid")
	var starID: Int = 0
		private set
	@SerializedName("n")
	var name: String = ""
		private set
	@SerializedName("puid")
	var playerID: Int = 0
		private set
	var v: String? = null
		private set
	@SerializedName("y")
	var yCoordinate: Double = 0.toDouble()
		private set
	@SerializedName("x")
	var xCoordinate: Double = 0.toDouble()
		private set

	override fun compareTo(other: Star): Int {
		return 0
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Star) return false

		if (starID != other.starID) return false
		if (name != other.name) return false
		if (playerID != other.playerID) return false
		if (v != other.v) return false
		if (yCoordinate != other.yCoordinate) return false
		if (xCoordinate != other.xCoordinate) return false

		return true
	}

	override fun hashCode(): Int {
		var result = starID
		result = 31 * result + name.hashCode()
		result = 31 * result + playerID
		result = 31 * result + (v?.hashCode() ?: 0)
		result = 31 * result + yCoordinate.hashCode()
		result = 31 * result + xCoordinate.hashCode()
		return result
	}

	override fun toString(): String {
		return "Star(starID=$starID, name='$name', playerID=$playerID, v=$v, yCoordinate=$yCoordinate, xCoordinate=$xCoordinate)"
	}
}