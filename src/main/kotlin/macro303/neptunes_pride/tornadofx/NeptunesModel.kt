package macro303.neptunes_pride.tornadofx

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
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
	val gameProperty = SimpleObjectProperty<Game>(null)
	val nameProperty = SimpleStringProperty(null)
	val startedProperty = SimpleBooleanProperty(false)
	val pausedProperty = SimpleBooleanProperty(true)
	val victoryProperty = SimpleStringProperty("0/0")
	val playerProperty = FXCollections.observableList(ArrayList<Player>())
	val turnProperty = SimpleIntegerProperty(0)
	val turnEveryProperty = SimpleStringProperty(null)
	val nextTurnProperty = SimpleStringProperty(null)
	val paydayEveryProperty = SimpleStringProperty(null)
	val nextPaydayProperty = SimpleStringProperty(null)
	val lastUpdatedProperty = SimpleStringProperty(null)

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
		nameProperty.value = game?.name
		startedProperty.value = game?.isStarted
		pausedProperty.value = game?.isPaused
		victoryProperty.value = "${game?.starVictory ?: 0}/${game?.totalStars ?: 0}"
		if (game != null)
			playerProperty.addAll(game.players)
		turnProperty.value = game?.turn?.div(12) ?: 0
		turnEveryProperty.value = "${game?.productionRate?.div(2) ?: 0} hrs"
		var noon = LocalTime.now().until(LocalTime.of(13, 0, 0, 0), ChronoUnit.MINUTES)
		if (noon <= 0)
			noon += (24 * 60)
		var midnight = LocalTime.now().until(LocalTime.of(1, 0, 0, 0), ChronoUnit.MINUTES)
		if (midnight <= 0)
			midnight += (24 * 60)
		nextTurnProperty.value =
				if (midnight in 1..(noon - 1)) "${midnight / 60}:${midnight % 60}" else "${noon / 60}:${noon % 60}"
		paydayEveryProperty.value = "${game?.productionRate ?: 0} hrs"
		nextPaydayProperty.value = "${noon / 60}:${noon % 60}"
		lastUpdatedProperty.value =
				if (game == null) null else LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy-MM-dd HH:mm"))
	}
}