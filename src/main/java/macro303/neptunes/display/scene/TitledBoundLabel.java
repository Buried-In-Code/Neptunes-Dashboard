package macro303.neptunes.display.scene;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-05-09.
 */
public class TitledBoundLabel {
	private Label titleLabel;
	private Label infoLabel;

	public TitledBoundLabel(@NotNull String title, @NotNull ObservableValue<? extends String> property) {
		titleLabel = new Label(title);
		infoLabel = new Label();
		infoLabel.textProperty().bind(property);
	}

	public Label getTitleLabel() {
		return titleLabel;
	}

	public Label getInfoLabel() {
		return infoLabel;
	}
}