package macro.neptunes.game

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.time.LocalDateTime

/**
 * Created by Macro303 on 2018-05-07.
 */
class Game(
	gameOver: Boolean,
	name: String,
	paused: Boolean,
	started: Boolean,
	startTime: LocalDateTime,
	totalStars: Int,
	turnTime: LocalDateTime,
	victory: Int
) {
	val gameOverProperty = SimpleBooleanProperty(gameOver)
	var gameOver by gameOverProperty

	val nameProperty = SimpleStringProperty(name)
	var name by nameProperty

	val pausedProperty = SimpleBooleanProperty(paused)
	var paused by pausedProperty

	val startedProperty = SimpleBooleanProperty(started)
	var started by startedProperty

	val startTimeProperty = SimpleObjectProperty<LocalDateTime>(startTime)
	var startTime by startTimeProperty

	val totalStarsProperty = SimpleIntegerProperty(totalStars)
	var totalStars by totalStarsProperty

	val turnTimeProperty = SimpleObjectProperty<LocalDateTime>(turnTime)
	var turnTime by turnTimeProperty

	val victoryProperty = SimpleIntegerProperty(victory)
	var victory by victoryProperty

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other !is Game) return false

		if (gameOverProperty != other.gameOverProperty) return false
		if (nameProperty != other.nameProperty) return false
		if (pausedProperty != other.pausedProperty) return false
		if (startedProperty != other.startedProperty) return false
		if (startTimeProperty != other.startTimeProperty) return false
		if (totalStarsProperty != other.totalStarsProperty) return false
		if (turnTimeProperty != other.turnTimeProperty) return false
		if (victoryProperty != other.victoryProperty) return false

		return true
	}

	override fun hashCode(): Int {
		var result = gameOverProperty.hashCode()
		result = 31 * result + nameProperty.hashCode()
		result = 31 * result + pausedProperty.hashCode()
		result = 31 * result + startedProperty.hashCode()
		result = 31 * result + startTimeProperty.hashCode()
		result = 31 * result + totalStarsProperty.hashCode()
		result = 31 * result + turnTimeProperty.hashCode()
		result = 31 * result + victoryProperty.hashCode()
		return result
	}

	override fun toString(): String {
		return "Game(gameOverProperty=$gameOverProperty, nameProperty=$nameProperty, pausedProperty=$pausedProperty, startedProperty=$startedProperty, startTimeProperty=$startTimeProperty, totalStarsProperty=$totalStarsProperty, turnTimeProperty=$turnTimeProperty, victoryProperty=$victoryProperty)"
	}
}