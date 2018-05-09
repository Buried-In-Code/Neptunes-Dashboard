package macro303.neptunes.display.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import macro303.neptunes.Game;
import macro303.neptunes.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Created by Macro303 on 2018-05-08.
 */
public class PlayersModel implements Model {
	@NotNull
	private final ObservableList<Player> players = FXCollections.observableList(new ArrayList<>());

	@Override
	public void updateModel(@NotNull Game game) {
		updatePlayers(game.getPlayers());
	}

	@NotNull
	public ObservableList<Player> getPlayers() {
		return players;
	}

	private void updatePlayers(@NotNull TreeSet<Player> players) {
		this.players.clear();
		this.players.addAll(players);
		this.players.sort(Comparator.reverseOrder());
	}
}