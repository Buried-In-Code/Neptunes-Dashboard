package macro.neptunes

import javafx.beans.property.BooleanProperty

/**
 * Created by Macro303 on 2018-05-09.
 */
interface Model {
	val loading: BooleanProperty
	fun updateModel()
	fun getAddress(): String
}