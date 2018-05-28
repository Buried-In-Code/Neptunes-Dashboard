package macro303.neptunes.game

import javafx.beans.property.*
import javafx.util.Duration
import macro303.neptunes.Model
import tornadofx.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Created by Macro303 on 2018-05-08.
 */
class GameModel : ViewModel(), Model {
	private var name: StringProperty = SimpleStringProperty("Unknown")
	private var started: BooleanProperty = SimpleBooleanProperty(false)
	private val startTime: StringProperty = SimpleStringProperty(LocalDateTime.now().format(dateFormat))
	private var paused: BooleanProperty = SimpleBooleanProperty(false)
	private val victory: StringProperty = SimpleStringProperty("0/0")
	private val totalStars: IntegerProperty = SimpleIntegerProperty(0)
	private var gameOver: BooleanProperty = SimpleBooleanProperty(false)
	private var turnTime: StringProperty = SimpleStringProperty(LocalDateTime.now().format(dateFormat))

	init {
		val refreshService = GameRefreshService(this)
		refreshService.period = Duration.hours(1.0)
		refreshService.setOnFailed {
			println("Exception Loading API: ${it.source.exception}")
		}
		refreshService.setOnCancelled {
			println("Unable to connect to API")
		}
		refreshService.setOnSucceeded {
			val game = it.source.value
			if (game != null)
				updateModel(any = game)
			println("Game Update At: ${LocalDateTime.now().format(timeFormat)}")
		}
		refreshService.start()
	}

	override fun updateModel(any: Any) {
		val game = any as Game
		this.name.bind(game.nameProperty)
		this.started.bind(game.startedProperty)
		this.startTime.bind(SimpleStringProperty(game.startTime.format(dateFormat)))
		this.paused.bind(game.pausedProperty)
		this.victory.bind(SimpleStringProperty("${game.victory}/${game.totalStars}"))
		this.totalStars.bind(game.totalStarsProperty)
		this.gameOver.bind(game.gameOverProperty)
		this.turnTime.bind(SimpleStringProperty(game.turnTime.format(dateFormat)))
	}

	override fun getAddress(): String = "basic"

	fun nameProperty(): StringProperty {
		return name
	}

	fun startedProperty(): BooleanProperty {
		return started
	}

	fun startTimeProperty(): StringProperty {
		return startTime
	}

	fun pausedProperty(): BooleanProperty {
		return paused
	}

	fun victoryProperty(): StringProperty {
		return victory
	}

	fun totalStarsProperty(): IntegerProperty {
		return totalStars
	}

	fun gameOverProperty(): BooleanProperty {
		return gameOver
	}

	fun turnTimeProperty(): StringProperty {
		return turnTime
	}

	companion object {
		private val dateFormat = DateTimeFormatter.ofPattern("MMM-dd HH:mm")
		private val timeFormat = DateTimeFormatter.ofPattern("MMM-dd HH:mm:ss")
	}
}