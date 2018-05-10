package macro303.neptunes.display.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import macro303.neptunes.game.Game;
import org.jetbrains.annotations.NotNull;

import java.time.format.DateTimeFormatter;

/**
 * Created by Macro303 on 2018-05-08.
 */
public class GameModel implements Model {
	@NotNull
	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM-dd HH:mm");

	@NotNull
	private final StringProperty name = new SimpleStringProperty("Unknown");
	@NotNull
	private final BooleanProperty started = new SimpleBooleanProperty(false);
	@NotNull
	private final StringProperty startTime = new SimpleStringProperty("Unknown");
	@NotNull
	private final BooleanProperty paused = new SimpleBooleanProperty(false);
	@NotNull
	private final StringProperty victory = new SimpleStringProperty("0/0");
	@NotNull
	private final BooleanProperty gameOver = new SimpleBooleanProperty(false);

	@Override
	public void updateModel(@NotNull Game game) {
		name.bind(game.nameProperty());
		started.bind(game.startedProperty());
		startTime.bind(new SimpleStringProperty(game.getStartTime().format(formatter)));
		paused.bind(game.pausedProperty());
		victory.bind(new SimpleStringProperty(game.getVictory() + "/" + game.getTotalStars()));
		gameOver.bind(game.gameOverProperty());
	}

	public String getName() {
		return name.get();
	}

	public @NotNull StringProperty nameProperty() {
		return name;
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public boolean isStarted() {
		return started.get();
	}

	public @NotNull BooleanProperty startedProperty() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started.set(started);
	}

	public String getStartTime() {
		return startTime.get();
	}

	public @NotNull StringProperty startTimeProperty() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime.set(startTime);
	}

	public boolean isPaused() {
		return paused.get();
	}

	public @NotNull BooleanProperty pausedProperty() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused.set(paused);
	}

	public String getVictory() {
		return victory.get();
	}

	public @NotNull StringProperty victoryProperty() {
		return victory;
	}

	public void setVictory(String victory) {
		this.victory.set(victory);
	}

	public boolean isGameOver() {
		return gameOver.get();
	}

	public @NotNull BooleanProperty gameOverProperty() {
		return gameOver;
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver.set(gameOver);
	}
}