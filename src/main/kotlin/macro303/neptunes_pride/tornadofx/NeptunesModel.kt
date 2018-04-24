package macro303.neptunes_pride.tornadofx

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import macro303.console.Console
import macro303.neptunes_pride.Config
import macro303.neptunes_pride.Connection
import macro303.neptunes_pride.Game
import macro303.neptunes_pride.Player
import tornadofx.*
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.*

internal class NeptunesModel : ViewModel() {
	private val gameProperty: SimpleObjectProperty<Game> = SimpleObjectProperty<Game>(null)
	val nameProperty: SimpleStringProperty = SimpleStringProperty(null)
	val startedProperty: SimpleBooleanProperty = SimpleBooleanProperty(false)
	val pausedProperty: SimpleBooleanProperty = SimpleBooleanProperty(true)
	val victoryProperty: SimpleStringProperty = SimpleStringProperty("0/0")
	val playerProperty: ObservableList<Player> = FXCollections.observableList(ArrayList<Player>())
	val turnProperty: SimpleIntegerProperty = SimpleIntegerProperty(0)
	val turnEveryProperty: SimpleStringProperty = SimpleStringProperty(null)
	val nextTurnProperty: SimpleStringProperty = SimpleStringProperty(null)
	val paydayEveryProperty: SimpleStringProperty = SimpleStringProperty(null)
	val nextPaydayProperty: SimpleStringProperty = SimpleStringProperty(null)
	val lastUpdatedProperty: SimpleStringProperty = SimpleStringProperty(null)

	init {
		loadCustomConfig()
		refreshGame()
	}

	private fun loadCustomConfig() {
		Connection.refreshConfig()
		if (Connection.configProperty.isNull.value!!) {
			Config.saveConfig(Config(0.toLong()))
			Console.displayError("Config couldn't be loaded new config has been created. Please add your details and reload")
		}
	}

	internal fun refreshGame() {
		val connection = Connection()
		connection.setOnSucceeded { setGame(it.source.value as Game) }
		connection.setOnFailed { setGame(null) }
		connection.setOnCancelled { setGame(null) }
		Thread(connection).start()
	}

	private fun setGame(game: Game?) {
		playerProperty.clear()
		gameProperty.value = game
		if(game != null){
			nameProperty.value = game.name
			startedProperty.value = game.isStarted
			pausedProperty.value = game.isPaused
			victoryProperty.value = "${game.starVictory}/${game.totalStars}"
			playerProperty.addAll(game.getPlayers())
			turnProperty.value = game.tick.div(game.payday.div(2))
			if(game.isPaused) {
				turnEveryProperty.value = "Paused"
				nextTurnProperty.value = "Paused"
				paydayEveryProperty.value = "Paused"
				nextPaydayProperty.value = "Paused"
			}else {
				turnEveryProperty.value = "${game.payday.div(2)} hrs"
				var noon = LocalTime.now().until(LocalTime.of(13, 0, 0, 0), ChronoUnit.MINUTES)
				if (noon <= 0)
					noon += (24 * 60)
				var midnight = LocalTime.now().until(LocalTime.of(1, 0, 0, 0), ChronoUnit.MINUTES)
				if (midnight <= 0)
					midnight += (24 * 60)
				nextTurnProperty.value = if (midnight in 1..(noon - 1)) "${midnight / 60}:${midnight % 60}" else "${noon / 60}:${noon % 60}"
				paydayEveryProperty.value = "${game.payday} hrs"
				nextPaydayProperty.value = "${noon / 60}:${noon % 60}"
			}
		}else{
			nameProperty.value = null
			startedProperty.value = null
			pausedProperty.value = null
			victoryProperty.value = null
			turnProperty.value = null
			turnEveryProperty.value = null
			nextTurnProperty.value = null
			paydayEveryProperty.value = null
			nextPaydayProperty.value = null
		}
		lastUpdatedProperty.value = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm"))
	}
}