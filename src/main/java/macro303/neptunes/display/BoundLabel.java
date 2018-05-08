package macro303.neptunes.display;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;

public class BoundLabel extends Label {
	public BoundLabel(ObservableValue<? extends String> property) {
		super();
		textProperty().bind(property);
	}
}
