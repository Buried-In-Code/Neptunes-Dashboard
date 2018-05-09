package macro303.neptunes.display.scene;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-05-09.
 */
public class BoundLabel extends Label {
	public BoundLabel(@NotNull ObservableValue<? extends String> property) {
		super();
		textProperty().bind(property);
	}
}