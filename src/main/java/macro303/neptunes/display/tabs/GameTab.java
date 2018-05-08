package macro303.neptunes.display.tabs;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import macro303.neptunes.display.models.GameModel;

public class GameTab extends Tab {
	private GameModel gameModel;

	public GameTab(GameModel gameModel) {
		super("Game");
		this.gameModel = gameModel;
		setContent(getTab());
	}

	private Node getTab() {
		var gridPane = new GridPane();
		gridPane.setHgap(5.0);
		gridPane.setVgap(5.0);
		gridPane.setPadding(new Insets(5.0));

		var nameLabel = new Label("NAME");
		nameLabel.getStyleClass().add("card-title");
		GridPane.setHalignment(nameLabel, HPos.CENTER);
		gridPane.addRow(0, nameLabel);
		var startedText = new Text("Started:");
		startedText.getStyleClass().add("card-subtitle");
		var startedLabel = new Label("NOW");
		gridPane.addRow(1, startedText, startedLabel);

		var column1 = new ColumnConstraints();
		column1.setPercentWidth(20.0);
		gridPane.getColumnConstraints().addAll(column1);

		return gridPane;
	}
}
