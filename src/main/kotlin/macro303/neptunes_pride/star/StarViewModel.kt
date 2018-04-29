package macro303.neptunes_pride.star

import javafx.beans.property.*
import tornadofx.*

internal class StarViewModel : ItemViewModel<Star>() {
	//Used
	//Unused
	val name: StringProperty = bind {
		SimpleStringProperty(item?.name ?: "INVALID")
	}
	val playerID: IntegerProperty = bind {
		SimpleIntegerProperty(item?.playerID ?: -1)
	}
	val starID: IntegerProperty = bind {
		SimpleIntegerProperty(item?.starID ?: -1)
	}
	val visible: BooleanProperty = bind {
		SimpleBooleanProperty(item?.visible ?: false)
	}
	val xCoordinate: DoubleProperty = bind {
		SimpleDoubleProperty(item?.xCoordinate ?: -1.0)
	}
	val yCoordinate: DoubleProperty = bind {
		SimpleDoubleProperty(item?.yCoordinate ?: -1.0)
	}
	//Unknown
}