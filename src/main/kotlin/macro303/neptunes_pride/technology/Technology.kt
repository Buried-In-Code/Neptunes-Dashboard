package macro303.neptunes_pride.technology

/**
 * Created by Macro303 on 2018-04-17.
 */
internal data class Technology(
	val value: Double,
	val level: Int
) {
	override fun toString(): String {
		return "Technology(value=$value, level=$level)"
	}
}