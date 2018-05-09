package macro303.neptunes.display.scene;

import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-05-09.
 */
public class SubtitleText extends Text {
	public SubtitleText(@NotNull String title) {
		super(title);
		getStyleClass().add("card-subtitle");
	}
}