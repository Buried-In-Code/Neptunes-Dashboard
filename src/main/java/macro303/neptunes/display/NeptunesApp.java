package macro303.neptunes.display;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NeptunesApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		var view = new NeptunesView();

		var scene = new Scene(view.getRoot());
		scene.getStylesheets().add("css/MaterialFX-v0.3.css");
//		scene.getStylesheets().add("css/JFXButton.css");
//		scene.getStylesheets().add("css/JFXScrollPane.css");
//		scene.getStylesheets().add("css/JFXTableView.css");
		primaryStage.setTitle("Neptune's Pride");
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}
}
