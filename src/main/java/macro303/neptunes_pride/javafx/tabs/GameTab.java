package macro303.neptunes_pride.javafx.tabs;

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

public class GameTab extends Tab {
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

		setupName(grid);
		setupPaused(grid);
		setupVictory(grid);
		setupTurn(grid);

		grid.setMaxWidth(Double.MAX_VALUE);

		return grid;
	}

	private void setupName(@NotNull GridPane grid) {
		Text nameText = new Text();
		nameText.textProperty().bind(model.nameProperty);
		nameText.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize() + 10));

		grid.add(nameText, 0, 0, 2, 1);

		GridPane.setHalignment(nameText, HPos.CENTER);
	}

	private void setupPaused(@NotNull GridPane grid) {
		Text pausedText = new Text();
		pausedText.textProperty().bind(model.pausedProperty.asString());
		grid.add(pausedText, 1, 1);

		Label pausedLabel = new Label("Paused:");
		pausedLabel.setLabelFor(pausedText);
		pausedLabel.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize()));
		grid.add(pausedLabel, 0, 1);
	}

	private void setupVictory(@NotNull GridPane grid) {
		Text victoryText = new Text();
		victoryText.textProperty().bind(model.victoryProperty);
		grid.add(victoryText, 1, 2);

		Label victoryLabel = new Label("Stars to Win:");
		victoryLabel.setLabelFor(victoryText);
		victoryLabel.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize()));
		grid.add(victoryLabel, 0, 2);
	}

	private void setupTurn(@NotNull GridPane grid) {
		Text turnText = new Text();
		turnText.textProperty().bind(model.turnProperty.asString());
		grid.add(turnText, 1, 3);

		Label turnLabel = new Label("Turn:");
		turnLabel.setLabelFor(turnText);
		turnLabel.fontProperty().setValue(Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, Font.getDefault().getSize()));
		grid.add(turnLabel, 0, 3);
	}
}