package macro303.neptunes.display.models;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import macro303.neptunes.Game;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
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
		setName(game.getName());
		setStarted(game.isStarted());
		setStartTime(game.getStartTime());
		setPaused(game.isPaused());
		setVictory(game.getStarsForVictory(), game.getTotalStars());
		setGameOver(game.isGameOver());
	}

	@NotNull
	public String getName() {
		return name.get();
	}

	private void setName(@NotNull String name) {
		this.name.set(name);
	}

	@NotNull
	public StringProperty nameProperty() {
		return name;
	}

	public boolean isStarted() {
		return started.get();
	}

	private void setStarted(boolean started) {
		this.started.set(started);
	}

	@NotNull
	public BooleanProperty startedProperty() {
		return started;
	}

	@NotNull
	public String getStartTime() {
		return startTime.get();
	}

	private void setStartTime(@NotNull LocalDateTime startTime) {
		this.startTime.set(startTime.format(formatter));
	}

	@NotNull
	public StringProperty startTimeProperty() {
		return startTime;
	}

	public boolean isPaused() {
		return paused.get();
	}

	private void setPaused(boolean paused) {
		this.paused.set(paused);
	}

	@NotNull
	public BooleanProperty pausedProperty() {
		return paused;
	}

	@NotNull
	public String getVictory() {
		return victory.get();
	}

	@NotNull
	public StringProperty victoryProperty() {
		return victory;
	}

	private void setVictory(int victory, int total) {
		this.victory.set(victory + "/" + total);
	}

	public boolean isGameOver() {
		return gameOver.get();
	}

	private void setGameOver(boolean gameOver) {
		this.gameOver.set(gameOver);
	}

	@NotNull
	public BooleanProperty gameOverProperty() {
		return gameOver;
	}
}