package macro303.neptunes_pride

/**
 * Created by Macro303 on 2018-04-17.
 */
internal data class Technology_kotlin(@JvmField val value: Double, @JvmField val level: Int) {
	override fun toString(): String {
		return "Technology(value=$value, level=$level)"
	}
}