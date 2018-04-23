package macro303.neptunes_pride.javafx.tabs;

import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import macro303.neptunes_pride.javafx.NeptunesModel;
import org.jetbrains.annotations.NotNull;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class GameTab extends Tab {
	private static final int headerFontIncrease = 20;
	private static final int fontIncrease = 10;
	private NeptunesModel model;

	public GameTab(NeptunesModel model) {
		super("Game");
		this.model = model;
		setContent(setupContent());
	}

	private Node setupContent() {
		GridPane grid = new GridPane();
		grid.setPadding(new Insets(5));
		grid.setHgap(5.0);

		ColumnConstraints column1 = new ColumnConstraints();
		column1.setPercentWidth(20);
		ColumnConstraints column2 = new ColumnConstraints();
		column2.setPercentWidth(80);
		grid.getColumnConstraints().addAll(column1, column2);

		setupName(grid, 0);
		setupStarted(grid, 1);
		setupPaused(grid, 2);
		setupVictory(grid, 3);
		setupTurn(grid, 4);
		setupNextTurn(grid, 5);
		setupNextPayday(grid, 6);

		grid.setMaxWidth(Double.MAX_VALUE);

		return grid;
	}

	private void setupName(@NotNull GridPane grid, int row) {
		Text nameText = new Text();
		nameText.textProperty().bind(model.nameProperty);
		nameText.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize() + headerFontIncrease));

		grid.add(nameText, 0, row, 2, 1);

		GridPane.setHalignment(nameText, HPos.CENTER);
	}

	private void setupStarted(@NotNull GridPane grid, int row) {
		Text startedText = new Text();
		startedText.textProperty().bind(model.startedProperty.asString());
		startedText.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), Font.getDefault().getSize() + fontIncrease));
		grid.add(startedText, 1, row);

		Label startedLabel = new Label("Started:");
		startedLabel.setLabelFor(startedText);
		startedLabel.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize() + fontIncrease));
		grid.add(startedLabel, 0, row);
	}

	private void setupPaused(@NotNull GridPane grid, int row) {
		Text pausedText = new Text();
		pausedText.textProperty().bind(model.pausedProperty.asString());
		pausedText.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), Font.getDefault().getSize() + fontIncrease));
		grid.add(pausedText, 1, row);

		Label pausedLabel = new Label("Paused:");
		pausedLabel.setLabelFor(pausedText);
		pausedLabel.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize() + fontIncrease));
		grid.add(pausedLabel, 0, row);
	}

	private void setupVictory(@NotNull GridPane grid, int row) {
		Text victoryText = new Text();
		victoryText.textProperty().bind(model.victoryProperty);
		victoryText.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), Font.getDefault().getSize() + fontIncrease));
		grid.add(victoryText, 1, row);

		Label victoryLabel = new Label("Stars to Win:");
		victoryLabel.setLabelFor(victoryText);
		victoryLabel.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize() + fontIncrease));
		grid.add(victoryLabel, 0, row);
	}

	private void setupTurn(@NotNull GridPane grid, int row) {
		Text turnText = new Text();
		turnText.textProperty().bind(model.turnProperty.asString());
		turnText.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), Font.getDefault().getSize() + fontIncrease));
		grid.add(turnText, 1, row);

		Label turnLabel = new Label("Turn:");
		turnLabel.setLabelFor(turnText);
		turnLabel.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize() + fontIncrease));
		grid.add(turnLabel, 0, row);
	}

	private void setupNextTurn(@NotNull GridPane grid, int row) {
		Text nextTurnText = new Text();
		long midnight = LocalTime.now().until(LocalTime.MIDNIGHT.plusHours(1), ChronoUnit.HOURS);
		if (midnight < 0)
			midnight += 24;
		long noon = LocalTime.now().until(LocalTime.NOON.plusHours(1), ChronoUnit.HOURS);
		if (noon < 0)
			noon += 24;
		if (midnight > noon)
			nextTurnText.textProperty().bind(new SimpleStringProperty(noon + " hrs"));
		else
			nextTurnText.textProperty().bind(new SimpleStringProperty(midnight + " hrs"));
		nextTurnText.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), Font.getDefault().getSize() + fontIncrease));
		grid.add(nextTurnText, 1, row);

		Label nextTurnLabel = new Label("Next Turn:");
		nextTurnLabel.setLabelFor(nextTurnText);
		nextTurnLabel.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize() + fontIncrease));
		grid.add(nextTurnLabel, 0, row);
	}

	private void setupNextPayday(@NotNull GridPane grid, int row) {
		Text nextPaydayText = new Text();
		long noon = LocalTime.now().until(LocalTime.NOON.plusHours(1), ChronoUnit.HOURS);
		if (noon < 0)
			noon += 24;
		nextPaydayText.textProperty().bind(new SimpleStringProperty(noon + " hrs"));
		nextPaydayText.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), Font.getDefault().getSize() + fontIncrease));
		grid.add(nextPaydayText, 1, row);

		Label nextPaydayLabel = new Label("Next Payday:");
		nextPaydayLabel.setLabelFor(nextPaydayText);
		nextPaydayLabel.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize() + fontIncrease));
		grid.add(nextPaydayLabel, 0, row);
	}
}