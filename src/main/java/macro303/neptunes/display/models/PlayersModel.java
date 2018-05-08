package macro303.neptunes.display.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import macro303.neptunes.Game;
import macro303.neptunes.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeSet;

public class PlayersModel {
	private final ObservableList<Player> players = FXCollections.observableList(new ArrayList<>());

	public void updateModel(Game game) {
		updatePlayers(game.getPlayers());
	}

	public ObservableList<Player> getPlayers() {
		return players;
	}

	private void updatePlayers(TreeSet<Player> players) {
		this.players.clear();
		this.players.addAll(players);
		this.players.sort(Comparator.reverseOrder());
	}
}
