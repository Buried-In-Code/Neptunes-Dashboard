package macro303.neptunes.display.tabs;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import macro303.neptunes.display.models.GameModel;
import macro303.neptunes.display.scene.TitledBoundLabel;
import macro303.neptunes.display.scene.TitledClockLabel;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

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

		var nameLabel = new Label();
		nameLabel.textProperty().bind(gameModel.nameProperty());
		GridPane.setHalignment(nameLabel, HPos.CENTER);
		gridPane.addRow(0, nameLabel);

		var started = new TitledBoundLabel("Started:", gameModel.startedProperty().asString());
		gridPane.addRow(1, started.getTitleLabel(), started.getInfoLabel());

		var start = new TitledBoundLabel("Start Time: ", gameModel.startTimeProperty());
		gridPane.addRow(2, start.getTitleLabel(), start.getInfoLabel());

		var paused = new TitledBoundLabel("Paused: ", gameModel.pausedProperty().asString());
		gridPane.addRow(3, paused.getTitleLabel(), paused.getInfoLabel());

		var victory = new TitledBoundLabel("Stars to Win: ", gameModel.victoryProperty());
		gridPane.addRow(4, victory.getTitleLabel(), victory.getInfoLabel());

		var gameOver = new TitledBoundLabel("Game Over: ", gameModel.gameOverProperty().asString());
		gridPane.addRow(5, gameOver.getTitleLabel(), gameOver.getInfoLabel());

		var nextTurn = new TitledClockLabel("Next Turn: ");
		gridPane.addRow(6, nextTurn.getTitleLabel(), nextTurn.getClockLabel());

		var column1 = new ColumnConstraints();
		column1.setPercentWidth(20.0);
		gridPane.getColumnConstraints().addAll(column1);

		return gridPane;
	}
}