package macro303.neptunes_pride.technology

import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import tornadofx.*

internal class TechnologyViewModel : ItemViewModel<Technology>() {
	//Used
	val level: IntegerProperty = bind {
		SimpleIntegerProperty(item?.level ?: -1)
	}
	//Unused
	val value: DoubleProperty = bind {
		SimpleDoubleProperty(item?.value ?: -1.0)
	}
	//Unknown
}