package macro303.neptunes.display.tabs;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import macro303.neptunes.Player;
import macro303.neptunes.display.models.TeamsModel;

public class TeamsTab extends Tab {
	private TeamsModel teamsModel;

	public TeamsTab(TeamsModel teamsModel) {
		super("Teams");
		this.teamsModel = teamsModel;
		setContent(getTab());
	}

	private Node getTab() {
		var table = new TableView<Player>();

		return table;
	}
}
