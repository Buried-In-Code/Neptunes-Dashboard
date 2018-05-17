package macro303.neptunes.display.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import macro303.neptunes.game.Game;
import macro303.neptunes.player.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Macro303 on 2018-05-08.
 */
public class PlayersModel implements Model {
	@NotNull
	private final ObservableList<Player> players = FXCollections.observableList(new ArrayList<>());
	@Nullable
	private Game game;

	@Override
	public void updateModel(@NotNull Game game) {
		this.game = game;
		players.clear();
		players.setAll(game.getPlayers());
		players.sort(Comparator.reverseOrder());
	}

	@NotNull
	public ObservableList<Player> getPlayers() {
		return players;
	}

	@Nullable
	@Override
	public Game getGame() {
		return game;
	}
}