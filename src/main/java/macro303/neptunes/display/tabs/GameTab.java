package macro303.neptunes.display.tabs;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import macro303.neptunes.display.scene.BoundLabel;
import macro303.neptunes.display.scene.ClockLabel;
import macro303.neptunes.display.scene.SubtitleText;
import macro303.neptunes.display.models.GameModel;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-05-08.
 */
public class GameTab extends Tab {
	@NotNull
	private GameModel gameModel;

	public GameTab(@NotNull GameModel gameModel) {
		super("Game");
		this.gameModel = gameModel;
		setContent(getTab());
	}

	@NotNull
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
		var nextTurnLabel = new ClockLabel();
		gridPane.addRow(6, nextTurnText, nextTurnLabel);

		var column1 = new ColumnConstraints();
		column1.setPercentWidth(20.0);
		gridPane.getColumnConstraints().addAll(column1);

		return gridPane;
	}
}