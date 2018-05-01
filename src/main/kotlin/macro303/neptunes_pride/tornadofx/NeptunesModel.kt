package macro303.neptunes_pride.tornadofx

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import macro303.neptunes_pride.Connection
import macro303.neptunes_pride.game.Game
import macro303.neptunes_pride.player.Player
import tornadofx.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

internal class NeptunesModel : ViewModel() {
	private val dateTimeFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM HH:mm")
	private val gameProperty: SimpleObjectProperty<Game> = SimpleObjectProperty<Game>(null)
	val nameProperty: SimpleStringProperty = SimpleStringProperty("INVALID")
	val hasStartedProperty: SimpleBooleanProperty = SimpleBooleanProperty(false)
	val startTimeProperty: SimpleStringProperty = SimpleStringProperty("INVALID")
	val isPausedProperty: SimpleBooleanProperty = SimpleBooleanProperty(false)
	val victoryProperty: SimpleStringProperty = SimpleStringProperty("-1/-1")
	val finishedProperty: SimpleBooleanProperty = SimpleBooleanProperty(false)
	val playersProperty: ObservableList<Player> = FXCollections.observableList(ArrayList<Player>())
	val currentTurnProperty: SimpleIntegerProperty = SimpleIntegerProperty(-1)
	val turnRateProperty: SimpleStringProperty = SimpleStringProperty("INVALID")
	val nextTurnProperty: SimpleStringProperty = SimpleStringProperty("INVALID")
	val payRateProperty: SimpleStringProperty = SimpleStringProperty("INVALID")
	val nextPaydayProperty: SimpleStringProperty = SimpleStringProperty("INVALID")
	val lastUpdatedProperty: SimpleStringProperty = SimpleStringProperty(LocalDateTime.now().format(dateTimeFormat))

	init {
		refreshGame()
	}

	internal fun refreshGame() {
		val connection = Connection()
		connection.setOnSucceeded { setGame(it.source.value as Game?) }
		connection.setOnFailed { setGame(null) }
		connection.setOnCancelled { setGame(null) }
		Thread(connection).start()
	}

	private fun setGame(game: Game?) {
		playersProperty.clear()
		if (game != null) {
			nameProperty.value = game.name
			hasStartedProperty.value = game.hasStarted
			startTimeProperty.value = game.startDateTime.format(dateTimeFormat)
			isPausedProperty.value = game.isPaused
			victoryProperty.value = "${game.starVictory}/${game.totalStars}"
			finishedProperty.value = game.isGameOver
			playersProperty.addAll(game.playerSet)
			currentTurnProperty.value = game.tick.div(game.payRate.div(2))
			if (game.isPaused) {
				turnRateProperty.value = "Paused"
				nextTurnProperty.value = "Paused"
				payRateProperty.value = "Paused"
				nextPaydayProperty.value = "Paused"
			} else {
				turnRateProperty.value = "${game.payRate.div(2)} hrs"
				var noon = LocalTime.now().until(LocalTime.of(13, 0, 0, 0), ChronoUnit.MINUTES)
				if (noon <= 0)
					noon += (24 * 60)
				var midnight = LocalTime.now().until(LocalTime.of(1, 0, 0, 0), ChronoUnit.MINUTES)
				if (midnight <= 0)
					midnight += (24 * 60)
				nextTurnProperty.value =
						if (midnight in 1..(noon - 1)) "${midnight / 60}:${midnight % 60}" else "${noon / 60}:${noon % 60}"
				payRateProperty.value = "${game.payRate} hrs"
				nextPaydayProperty.value = "${noon / 60}:${noon % 60}"
			}
		} else {
			nameProperty.value = "INVALID"
			hasStartedProperty.value = false
			startTimeProperty.value = "INVALID"
			isPausedProperty.value = false
			victoryProperty.value = "-1/-1"
			finishedProperty.value = false
			currentTurnProperty.value = -1
			turnRateProperty.value = "INVALID"
			nextTurnProperty.value = "INVALID"
			payRateProperty.value = "INVALID"
			nextPaydayProperty.value = "INVALID"
		}
		lastUpdatedProperty.value = LocalDateTime.now().format(dateTimeFormat)
		gameProperty.value = game
	}
}