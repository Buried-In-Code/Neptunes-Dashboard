package macro303.neptunes;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class NeptunesApp extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("NeptunesView.fxml"));

		Scene scene = new Scene(root);
		primaryStage.setTitle("Neptune's Pride");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
