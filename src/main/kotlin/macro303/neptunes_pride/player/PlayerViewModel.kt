package macro303.neptunes_pride.player

import javafx.beans.property.*
import tornadofx.*

internal class PlayerViewModel : ItemViewModel<Player>() {
	//Used
	val alias: StringProperty = bind {
		SimpleStringProperty(item?.alias ?: "INVALID")
	}
	val banking: IntegerProperty = bind {
		SimpleIntegerProperty(item?.technologyMap?.get("banking")?.level ?: -1)
	}
	val experimentation: IntegerProperty = bind {
		SimpleIntegerProperty(item?.technologyMap?.get("research")?.level ?: -1)
	}
	val hyperspaceRange: IntegerProperty = bind {
		SimpleIntegerProperty(item?.technologyMap?.get("propulsion")?.level ?: -1)
	}
	val manufacturing: IntegerProperty = bind {
		SimpleIntegerProperty(item?.technologyMap?.get("manufacturing")?.level ?: -1)
	}
	val scanning: IntegerProperty = bind {
		SimpleIntegerProperty(item?.technologyMap?.get("scanning")?.level ?: -1)
	}
	val terraforming: IntegerProperty = bind {
		SimpleIntegerProperty(item?.technologyMap?.get("terraforming")?.level ?: -1)
	}
	val weapons: IntegerProperty = bind {
		SimpleIntegerProperty(item?.technologyMap?.get("weapons")?.level ?: -1)
	}
	val economy: IntegerProperty = bind {
		SimpleIntegerProperty(item?.totalEconomy ?: -1)
	}
	val fleets: IntegerProperty = bind {
		SimpleIntegerProperty(item?.totalFleets ?: -1)
	}
	val industry: IntegerProperty = bind {
		SimpleIntegerProperty(item?.totalIndustry ?: -1)
	}
	val science: IntegerProperty = bind {
		SimpleIntegerProperty(item?.totalScience ?: -1)
	}
	val ships: IntegerProperty = bind {
		SimpleIntegerProperty(item?.totalShips ?: -1)
	}
	val stars: IntegerProperty = bind {
		SimpleIntegerProperty(item?.totalStars ?: -1)
	}
	val name: StringProperty = bind {
		SimpleStringProperty(item?.name ?: "INVALID")
	}
	//Unused
	val ai: BooleanProperty = bind {
		SimpleBooleanProperty(item?.isAI ?: false)
	}
	val avatar: IntegerProperty = bind {
		SimpleIntegerProperty(item?.avatar ?: -1)
	}
	val conceded: BooleanProperty = bind {
		SimpleBooleanProperty(item?.hasConceded ?: false)
	}
	val missedTurns: IntegerProperty = bind {
		SimpleIntegerProperty(item?.missedTurns ?: -1)
	}
	val playerID: IntegerProperty = bind {
		SimpleIntegerProperty(item?.playerID ?: -1)
	}
	val ready: BooleanProperty = bind {
		SimpleBooleanProperty(item?.isReady ?: false)
	}
	val regard: IntegerProperty = bind {
		SimpleIntegerProperty(item?.regard ?: -1)
	}
	//Unknown
	val huid: IntegerProperty = bind {
		SimpleIntegerProperty(item?.huid ?: -1)
	}
	val karma: IntegerProperty = bind {
		SimpleIntegerProperty(item?.karma ?: -1)
	}
}