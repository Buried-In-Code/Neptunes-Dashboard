package macro303.neptunes.game;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import macro303.neptunes.Team;
import macro303.neptunes.player.Player;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Macro303 on 2018-05-07.
 */
public class Game {
	@NotNull
	private final BooleanProperty gameOver;
	@NotNull
	private final StringProperty name;
	@NotNull
	private final BooleanProperty paused;
	@NotNull
	private final ObservableList<Player> players;
	@NotNull
	private final BooleanProperty started;
	@NotNull
	private final ObjectProperty<LocalDateTime> startTime;
	@NotNull
	private final ObservableList<Team> teams;
	@NotNull
	private final IntegerProperty totalStars;
	@NotNull
	private final IntegerProperty victory;

	public Game(boolean gameOver, @NotNull String name, boolean paused, @NotNull List<Player> players, boolean started, @NotNull LocalDateTime startTime, @NotNull List<Team> teams, int totalStars, int victory) {
		this.gameOver = new SimpleBooleanProperty(gameOver);
		this.name = new SimpleStringProperty(name);
		this.paused = new SimpleBooleanProperty(paused);
		this.players = FXCollections.observableList(players);
		this.started = new SimpleBooleanProperty(started);
		this.startTime = new SimpleObjectProperty<>(startTime);
		this.teams = FXCollections.observableList(teams);
		this.totalStars = new SimpleIntegerProperty(totalStars);
		this.victory = new SimpleIntegerProperty(victory);
	}

	public boolean isGameOver() {
		return gameOver.get();
	}

	public void setGameOver(boolean gameOver) {
		this.gameOver.set(gameOver);
	}

	public @NotNull BooleanProperty gameOverProperty() {
		return gameOver;
	}

	public String getName() {
		return name.get();
	}

	public void setName(String name) {
		this.name.set(name);
	}

	public @NotNull StringProperty nameProperty() {
		return name;
	}

	public boolean isPaused() {
		return paused.get();
	}

	public void setPaused(boolean paused) {
		this.paused.set(paused);
	}

	public @NotNull BooleanProperty pausedProperty() {
		return paused;
	}

	public ObservableList<Player> getPlayers() {
		return players;
	}

	public boolean isStarted() {
		return started.get();
	}

	public void setStarted(boolean started) {
		this.started.set(started);
	}

	public @NotNull BooleanProperty startedProperty() {
		return started;
	}

	public LocalDateTime getStartTime() {
		return startTime.get();
	}

	public void setStartTime(LocalDateTime startTime) {
		this.startTime.set(startTime);
	}

	public @NotNull ObjectProperty<LocalDateTime> startTimeProperty() {
		return startTime;
	}

	public ObservableList<Team> getTeams() {
		return teams;
	}

	public int getTotalStars() {
		return totalStars.get();
	}

	public void setTotalStars(int totalStars) {
		this.totalStars.set(totalStars);
	}

	public @NotNull IntegerProperty totalStarsProperty() {
		return totalStars;
	}

	public int getVictory() {
		return victory.get();
	}

	public void setVictory(int victory) {
		this.victory.set(victory);
	}

	public @NotNull IntegerProperty victoryProperty() {
		return victory;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Game)) return false;

		Game game = (Game) o;

		if (!gameOver.equals(game.gameOver)) return false;
		if (!name.equals(game.name)) return false;
		if (!paused.equals(game.paused)) return false;
		if (!players.equals(game.players)) return false;
		if (!started.equals(game.started)) return false;
		if (!startTime.equals(game.startTime)) return false;
		if (!teams.equals(game.teams)) return false;
		if (!totalStars.equals(game.totalStars)) return false;
		return victory.equals(game.victory);
	}

	@Override
	public int hashCode() {
		int result = gameOver.hashCode();
		result = 31 * result + name.hashCode();
		result = 31 * result + paused.hashCode();
		result = 31 * result + players.hashCode();
		result = 31 * result + started.hashCode();
		result = 31 * result + startTime.hashCode();
		result = 31 * result + teams.hashCode();
		result = 31 * result + totalStars.hashCode();
		result = 31 * result + victory.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Game{" +
				"gameOver=" + gameOver +
				", name=" + name +
				", paused=" + paused +
				", players=" + players +
				", started=" + started +
				", startTime=" + startTime +
				", teams=" + teams +
				", totalStars=" + totalStars +
				", victory=" + victory +
				'}';
	}
}