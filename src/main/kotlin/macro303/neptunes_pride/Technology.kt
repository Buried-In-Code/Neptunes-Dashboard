package macro303.neptunes_pride

/**
 * Created by Macro303 on 2018-04-17.
 */
internal class Technology {
	var value: Double = 0.0
		private set
	var level: Int = 0
		private set

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Technology) return false

		if (value != other.value) return false
		if (level != other.level) return false

		return true
	}

	override fun hashCode(): Int {
		var result = value.hashCode()
		result = 31 * result + level
		return result
	}

	override fun toString(): String {
		return "Technology(value=$value, level=$level)"
	}
}