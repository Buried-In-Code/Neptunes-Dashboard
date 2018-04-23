package macro303.neptunes_pride.javafx;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import macro303.console.Console;
import macro303.neptunes_pride.Config;
import macro303.neptunes_pride.Connection;
import macro303.neptunes_pride.Game;
import macro303.neptunes_pride.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class NeptunesModel {
	public final SimpleObjectProperty<Game> gameProperty = new SimpleObjectProperty<>(null);
	public final SimpleStringProperty nameProperty = new SimpleStringProperty(null);
	public final SimpleBooleanProperty startedProperty = new SimpleBooleanProperty(false);
	public final SimpleBooleanProperty pausedProperty = new SimpleBooleanProperty(true);
	public final SimpleStringProperty victoryProperty = new SimpleStringProperty("0/0");
	public final ObservableList<Player> playerProperty = FXCollections.observableList(new ArrayList<>());
	public final SimpleIntegerProperty turnProperty = new SimpleIntegerProperty(0);

	NeptunesModel() {
		loadConfig();
		refreshGame();
	}

	private void loadConfig() {
		Connection.refreshConfig();
		if (Connection.configProperty.isNull().getValue()) {
			Config.saveConfig(new Config(-1L));
			Console.displayError("Config couldn't be loaded new config has been created. Please add your details and reload");
		}
	}

	void refreshGame() {
		Connection connection = new Connection();
		connection.setOnSucceeded(event -> setGame((Game) event.getSource().getValue()));
		connection.setOnFailed(event -> setGame(null));
		connection.setOnCancelled(event -> setGame(null));
		new Thread(connection).start();
	}

	private void setGame(@Nullable Game game) {
		playerProperty.clear();
		gameProperty.setValue(game);
		nameProperty.setValue(game == null ? null : game.getName());
		startedProperty.setValue(game == null ? null : game.isStarted());
		pausedProperty.setValue(game == null ? null : game.isPaused());
		victoryProperty.setValue(game == null ? "0/0" : game.getStarVictory() + "/" + game.getTotalStars());
		if (game != null)
			playerProperty.addAll(game.getPlayers());
		turnProperty.setValue(game == null ? 0 : game.getTurn());
	}
}