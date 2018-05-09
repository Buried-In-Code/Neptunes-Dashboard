package macro303.neptunes.display.tabs;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TableView;
import macro303.neptunes.Player;
import macro303.neptunes.display.models.TeamsModel;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-05-08.
 */
public class TeamsTab extends Tab {
	@NotNull
	private TeamsModel teamsModel;

	public TeamsTab(@NotNull TeamsModel teamsModel) {
		super("Teams");
		this.teamsModel = teamsModel;
		setContent(getTab());
	}

	@NotNull
	private Node getTab() {
		var table = new TableView<Player>();

		return table;
	}
}