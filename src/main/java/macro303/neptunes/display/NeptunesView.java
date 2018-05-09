package macro303.neptunes.display;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTabPane;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
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
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-05-08.
 */
public class NeptunesView {
	@NotNull
	private GameModel gameModel = new GameModel();
	@NotNull
	private PlayersModel playersModel = new PlayersModel();
	@NotNull
	private TeamsModel teamsModel = new TeamsModel();

	@NotNull
	public Parent getRoot() {
		var root = new BorderPane();
		root.setPadding(new Insets(10.0));
		root.setStyle("-fx-background-color: #EAEAEA");

		root.setCenter(getCenter());
		root.setBottom(getBottom());

		return root;
	}

	@NotNull
	private Node getCenter() {
		var tabPane = new JFXTabPane();
		tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		tabPane.setPadding(new Insets(5.0));
		var gameTab = new GameTab(gameModel);
		var playersTab = new PlayersTab(playersModel);
		var teamsTab = new TeamsTab(teamsModel);
		teamsTab.setDisable(true);
		tabPane.getTabs().addAll(gameTab, playersTab, teamsTab);
		return tabPane;
	}

	@NotNull
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
				teamsModel.updateModel(game);
			});
			new Thread(connection).start();
		});
		hbox.getChildren().add(refresh);

		return hbox;
	}
}