package macro303.neptunes_pride.javafx;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import macro303.neptunes_pride.Connection;
import macro303.neptunes_pride.javafx.tabs.FleetsTab;
import macro303.neptunes_pride.javafx.tabs.GameTab;
import macro303.neptunes_pride.javafx.tabs.PlayersTab;
import macro303.neptunes_pride.javafx.tabs.StarsTab;

public class NeptunesFX extends Application {
	private NeptunesModel model;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		model = new NeptunesModel();

		VBox root = setupUI();

		Scene scene = new Scene(root, 600, 800);
		primaryStage.setTitle("Neptunes Pride");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private VBox setupUI() {
		VBox root = new VBox();
		root.setPadding(new Insets(10));
		root.setSpacing(5.0);

		TabPane main = setupMain();
		HBox refreshButton = setupRefreshButton();

		VBox.setVgrow(main, Priority.ALWAYS);
		VBox.setVgrow(refreshButton, Priority.SOMETIMES);
		root.getChildren().addAll(main, refreshButton);

		return root;
	}

	private TabPane setupMain() {
		TabPane main = new TabPane();
		main.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

		main.getTabs().addAll(new GameTab(model), new PlayersTab(model), new StarsTab(model), new FleetsTab(model));

		return main;
	}

	private HBox setupRefreshButton() {
		HBox refreshBox = new HBox();
		refreshBox.setAlignment(Pos.CENTER);

		Button refreshButton = new Button("Refresh");
		refreshButton.disableProperty().bind(Connection.connectionUnavailableProperty);
		refreshButton.setOnAction(event -> model.refreshGame());
		refreshButton.setMaxWidth(Double.MAX_VALUE);

		HBox.setHgrow(refreshButton, Priority.ALWAYS);
		refreshBox.getChildren().add(refreshButton);

		return refreshBox;
	}
}
