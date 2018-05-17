package macro303.neptunes.display.models;

import javafx.beans.property.*;
import macro303.neptunes.game.Game;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	@NotNull
	private final ObjectProperty<LocalDateTime> turnTime = new SimpleObjectProperty<>(LocalDateTime.now());
	@Nullable
	private Game game;

	@Override
	public void updateModel(@NotNull Game game) {
		this.game = game;
		name.bind(game.nameProperty());
		started.bind(game.startedProperty());
		startTime.bind(new SimpleStringProperty(game.getStartTime().format(formatter)));
		paused.bind(game.pausedProperty());
		victory.bind(new SimpleStringProperty(game.getVictory() + "/" + game.getTotalStars()));
		gameOver.bind(game.gameOverProperty());
		turnTime.bind(game.turnTimeProperty());
	}

	@Nullable
	@Override
	public Game getGame() {
		return game;
	}

	public @NotNull StringProperty nameProperty() {
		return name;
	}

	public @NotNull BooleanProperty startedProperty() {
		return started;
	}

	public @NotNull StringProperty startTimeProperty() {
		return startTime;
	}

	public @NotNull BooleanProperty pausedProperty() {
		return paused;
	}

	public @NotNull StringProperty victoryProperty() {
		return victory;
	}

	public @NotNull BooleanProperty gameOverProperty() {
		return gameOver;
	}

	public @NotNull ObjectProperty<LocalDateTime> turnTimeProperty() {
		return turnTime;
	}
}