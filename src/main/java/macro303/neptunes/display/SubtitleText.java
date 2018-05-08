package macro303.neptunes.display;

import javafx.scene.text.Text;

public class SubtitleText extends Text {
	public SubtitleText(String title) {
		super(title);
		getStyleClass().add("card-subtitle");
	}
}