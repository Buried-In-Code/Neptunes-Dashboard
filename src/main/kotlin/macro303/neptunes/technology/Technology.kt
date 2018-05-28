package macro303.neptunes.technology

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*

/**
 * Created by Macro303 on 2018-05-07.
 */
class Technology(level: Int, value: Double) {
	val levelProperty = SimpleIntegerProperty(level)
	var level by levelProperty

	val valueProperty = SimpleDoubleProperty(value)
	var value by valueProperty

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Technology) return false

		if (levelProperty != other.levelProperty) return false
		if (valueProperty != other.valueProperty) return false

		return true
	}

	override fun hashCode(): Int {
		var result = levelProperty.hashCode()
		result = 31 * result + valueProperty.hashCode()
		return result
	}

	override fun toString(): String {
		return "Technology(levelProperty=$levelProperty, valueProperty=$valueProperty)"
	}
}