package macro303.neptunes.display.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import macro303.neptunes.Game;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GameModel {
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd HH:mm");

	private final StringProperty name = new SimpleStringProperty("Unknown");
	private final BooleanProperty started = new SimpleBooleanProperty(false);
	private final StringProperty startTime = new SimpleStringProperty("Unknown");
	private final BooleanProperty paused = new SimpleBooleanProperty(false);
	private final StringProperty victory = new SimpleStringProperty("0/0");
	private final BooleanProperty gameOver = new SimpleBooleanProperty(false);

	public void updateModel(Game game) {
		setName(game.getName());
		setStarted(game.isStarted());
		setStartTime(game.getStartTime());
		setPaused(game.isPaused());
		setVictory(game.getStarsForVictory(), game.getTotalStars());
		setGameOver(game.isGameOver());
	}

	public String getName() {
		return name.get();
	}

	public StringProperty nameProperty() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public boolean isStarted() {
		return started.get();
	}

	public BooleanProperty startedProperty() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started.set(started);
	}

	public String getStartTime() {
		return startTime.get();
	}

	public StringProperty startTimeProperty() {
		return startTime;
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime.set(startTime.format(formatter));
	}

	public boolean isPaused() {
		return paused.get();
	}

	public BooleanProperty pausedProperty() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused.set(paused);
	}

	public String getVictory() {
		return victory.get();
	}

	public StringProperty victoryProperty() {
		return victory;
	}

	public void setVictory(int victory, int total) {
		this.victory.set(victory + "/" + total);
	}

	public boolean isGameOver() {
		return gameOver.get();
	}

	public BooleanProperty gameOverProperty() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver.set(gameOver);
	}
}