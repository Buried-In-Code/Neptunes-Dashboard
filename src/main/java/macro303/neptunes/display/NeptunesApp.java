package macro303.neptunes.display;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Created by Macro303 on 2018-05-08.
 */
public class NeptunesApp extends Application {

	public static void main(@Nullable String[] args) {
		launch(args);
	}

	@Override
	public void start(@NotNull Stage primaryStage) {
		var view = new NeptunesView();

		var scene = new Scene(view.getRoot());
		scene.getStylesheets().add("css/MaterialFX-v0.3.css");
		primaryStage.setTitle("Neptune's Pride");
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}
}