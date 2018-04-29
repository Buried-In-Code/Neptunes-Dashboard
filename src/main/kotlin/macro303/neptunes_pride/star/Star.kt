package macro303.neptunes_pride.star

import com.google.gson.annotations.SerializedName

internal data class Star(
	@SerializedName(value = "uid") val starID: Int,
	@SerializedName(value = "n") val name: String,
	@SerializedName(value = "puid") val playerID: Int,
	private val v: String,
	@SerializedName(value = "y") val yCoordinate: Double,
	@SerializedName(value = "x") val xCoordinate: Double
) : Comparable<Star> {
	val visible: Boolean
		get() = v == "1"

	override fun compareTo(other: Star): Int {
		return compareBy<Star>(
			{ it.playerID },
			{ it.name }
		).reversed().compare(this, other)
	}

	override fun toString(): String {
		return "Star(starID=$starID, name='$name', playerID=$playerID, v='$v', yCoordinate=$yCoordinate, xCoordinate=$xCoordinate)"
	}
}