package macro303.neptunes_pride.game

import javafx.beans.property.*
import macro303.neptunes_pride.player.Player
import macro303.neptunes_pride.star.Star
import tornadofx.*
import java.time.LocalDateTime
import java.util.*

internal class GameViewModel : ItemViewModel<Game>() {
	//Used
	val hasStarted: BooleanProperty = bind {
		SimpleBooleanProperty(item?.hasStarted ?: false)
	}
	val isGameOver: BooleanProperty = bind {
		SimpleBooleanProperty(item?.isGameOver ?: false)
	}
	val isPaused: BooleanProperty = bind {
		SimpleBooleanProperty(item?.isPaused ?: false)
	}
	val name: StringProperty = bind {
		SimpleStringProperty(item?.name ?: "INVALID")
	}
	val payRate: IntegerProperty = bind {
		SimpleIntegerProperty(item?.payRate ?: -1)
	}
	val players: ObjectProperty<TreeSet<Player>> = bind {
		SimpleObjectProperty<TreeSet<Player>>(item?.playerSet ?: TreeSet())
	}
	val startTime: ObjectProperty<LocalDateTime> = bind {
		SimpleObjectProperty<LocalDateTime>(item?.startDateTime ?: LocalDateTime.now())
	}
	val starVictory: IntegerProperty = bind {
		SimpleIntegerProperty(item?.starVictory ?: -1)
	}
	val tick: IntegerProperty = bind {
		SimpleIntegerProperty(item?.tick ?: -1)
	}
	val totalStars: IntegerProperty = bind {
		SimpleIntegerProperty(item?.totalStars ?: -1)
	}
	val turn: IntegerProperty = bind {
		SimpleIntegerProperty(item?.turn ?: -1)
	}
	//Unused
	val admin: IntegerProperty = bind {
		SimpleIntegerProperty(item?.admin ?: -1)
	}
	val fleetSpeed: DoubleProperty = bind {
		SimpleDoubleProperty(item?.fleetSpeed ?: -1.0)
	}
	val now: ObjectProperty<LocalDateTime> = bind {
		SimpleObjectProperty<LocalDateTime>(item?.nowDateTime ?: LocalDateTime.now())
	}
	val stars: ObjectProperty<TreeSet<Star>> = bind {
		SimpleObjectProperty<TreeSet<Star>>(item?.starSet ?: TreeSet())
	}
	val tickFragment: IntegerProperty = bind {
		SimpleIntegerProperty(item?.tickFragment ?: -1)
	}
	val tickRate: IntegerProperty = bind {
		SimpleIntegerProperty(item?.tickRate ?: -1)
	}
	val timeOut: LongProperty = bind {
		SimpleLongProperty(item?.timeOut ?: -1)
	}
	val tradeCost: IntegerProperty = bind {
		SimpleIntegerProperty(item?.tradeCost ?: -1)
	}
	val tradeScanned: IntegerProperty = bind {
		SimpleIntegerProperty(item?.tradeScanned ?: -1)
	}
	//Unknown
	val fleets: ObjectProperty<Any> = bind {
		SimpleObjectProperty<Any>(item?.fleets)
	}
	val playerUID: IntegerProperty = bind {
		SimpleIntegerProperty(item?.playerUID ?: -1)
	}
	val productionCounter: IntegerProperty = bind {
		SimpleIntegerProperty(item?.productionCounter ?: -1)
	}
	val turnBased: IntegerProperty = bind {
		SimpleIntegerProperty(item?.turnBased ?: -1)
	}
	val war: IntegerProperty = bind {
		SimpleIntegerProperty(item?.war ?: -1)
	}
}