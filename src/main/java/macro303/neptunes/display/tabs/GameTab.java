package macro303.neptunes.display.tabs;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import macro303.neptunes.display.BoundLabel;
import macro303.neptunes.display.Clock;
import macro303.neptunes.display.SubtitleText;
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

		var nameLabel = new BoundLabel(gameModel.nameProperty());
		nameLabel.getStyleClass().add("card-title");
		GridPane.setHalignment(nameLabel, HPos.CENTER);
		gridPane.addRow(0, nameLabel);

		var startedText = new SubtitleText("Started:");
		var startedLabel = new BoundLabel(gameModel.startedProperty().asString());
		gridPane.addRow(1, startedText, startedLabel);

		var startText = new SubtitleText("Start Time:");
		var startLabel = new BoundLabel(gameModel.startTimeProperty());
		gridPane.addRow(2, startText, startLabel);

		var pausedText = new SubtitleText("Paused:");
		var pausedLabel = new BoundLabel(gameModel.pausedProperty().asString());
		gridPane.addRow(3, pausedText, pausedLabel);

		var victoryText = new SubtitleText("Stars to Win:");
		var victoryLabel = new BoundLabel(gameModel.victoryProperty());
		gridPane.addRow(4, victoryText, victoryLabel);

		var gameOverText = new SubtitleText("Game Over:");
		var gameOverLabel = new BoundLabel(gameModel.gameOverProperty().asString());
		gridPane.addRow(5, gameOverText, gameOverLabel);

		var nextTurnText = new SubtitleText("Next Turn:");
		var nextTurnLabel = new Clock();
		gridPane.addRow(6, nextTurnText, nextTurnLabel);

		var column1 = new ColumnConstraints();
		column1.setPercentWidth(20.0);
		gridPane.getColumnConstraints().addAll(column1);

		return gridPane;
	}
}
