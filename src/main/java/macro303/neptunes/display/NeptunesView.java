package macro303.neptunes.display;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import macro303.neptunes.Connection;
import macro303.neptunes.Game;
import macro303.neptunes.display.models.GameModel;
import macro303.neptunes.display.models.PlayersModel;
import macro303.neptunes.display.models.TeamsModel;
import macro303.neptunes.display.tabs.GameTab;
import macro303.neptunes.display.tabs.PlayersTab;
import macro303.neptunes.display.tabs.TeamsTab;

public class NeptunesView {
	private GameModel gameModel;
	private PlayersModel playersModel;
	private TeamsModel teamsModel;

	public Parent getRoot() {
		var root = new BorderPane();
		root.setPadding(new Insets(10.0));
		root.setStyle("-fx-background-color: #EAEAEA");

		root.setCenter(getCenter());
		root.setBottom(getBottom());

		return root;
	}

	private Node getCenter() {
		var tabPane = new JFXTabPane();
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		tabPane.setPadding(new Insets(5.0));
		gameModel = new GameModel();
		var gameTab = new GameTab(gameModel);
		playersModel = new PlayersModel();
		var playersTab = new PlayersTab(playersModel);
		teamsModel = new TeamsModel();
		var teamsTab = new TeamsTab(teamsModel);
		teamsTab.setDisable(true);
		tabPane.getTabs().addAll(gameTab, playersTab, teamsTab);
		return tabPane;
	}

	private Node getBottom() {
		var hbox = new HBox();
		hbox.setAlignment(Pos.CENTER);

		var refresh = new JFXButton("Refresh");
		refresh.setOnAction(event -> {
			Connection connection = new Connection();
			connection.setOnSucceeded(success -> {
				var game = (Game) success.getSource().getValue();
				gameModel.updateModel(game);
				playersModel.updateModel(game);
			});
			new Thread(connection).start();
		});
		hbox.getChildren().add(refresh);

		return hbox;
	}
}