package macro303.neptunes.display.models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import macro303.neptunes.Team;
import macro303.neptunes.game.Game;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Macro303 on 2018-05-08.
 */
public class TeamsModel implements Model {
	@NotNull
	private final ObservableList<Team> teams = FXCollections.observableList(new ArrayList<>());

	@Override
	public void updateModel(@NotNull Game game) {
		teams.setAll(game.getTeams());
		teams.sort(Comparator.reverseOrder());
	}

	@NotNull
	public ObservableList<Team> getTeams() {
		return teams;
	}
}