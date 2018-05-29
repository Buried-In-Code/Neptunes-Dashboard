package macro303.neptunes.game

import javafx.beans.property.*
import macro303.neptunes.Model
import tornadofx.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.Executor
import java.util.concurrent.Executors

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
	override val loading = SimpleBooleanProperty(false)
	private val exec: Executor

	init {
		exec = Executors.newCachedThreadPool { runnable ->
			val t = Thread(runnable)
			t.isDaemon = true
			t
		}
		updateModel()
	}

	override fun updateModel() {
		val refreshTask = GameRefreshTask(this)
		refreshTask.setOnFailed {
			println("Unable to connect to Game API: ${refreshTask.exception.message}")
			loading.value = false
		}
		refreshTask.setOnSucceeded {
			val result = it.source.value as Game?
			if (result != null) {
				this.name.bind(result.nameProperty)
				this.started.bind(result.startedProperty)
				this.startTime.bind(SimpleStringProperty(result.startTime.format(dateFormat)))
				this.paused.bind(result.pausedProperty)
				this.victory.bind(SimpleStringProperty("${result.victory}/${result.totalStars}"))
				this.totalStars.bind(result.totalStarsProperty)
				this.gameOver.bind(result.gameOverProperty)
				this.turnTime.bind(SimpleStringProperty(result.turnTime.format(dateFormat)))
			}
			println("Game API loaded")
			loading.value = false
		}
		refreshTask.setOnCancelled {
			println("Unable to connect to Game API")
			loading.value = false
		}
		exec.execute(refreshTask)
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