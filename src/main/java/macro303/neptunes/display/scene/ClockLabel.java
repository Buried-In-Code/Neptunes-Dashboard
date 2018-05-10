package macro303.neptunes.display.scene;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

/**
 * Created by Macro303 on 2018-05-09.
 */
public class ClockLabel extends Label {
	public ClockLabel() {
		super();
		bindToTime();
	}

	private void bindToTime() {
		var clock = new Timeline(new KeyFrame(Duration.minutes(0), event -> {
			var noon = LocalTime.now().until(LocalTime.of(12, 55), ChronoUnit.MINUTES);
			if (noon <= 0)
				noon += (12 * 60);
			if (noon > (12 * 60))
				noon -= (12 * 60);
			setText(String.format("%02d", (noon / 60)) + ":" + String.format("%02d", (noon % 60)));
		}), new KeyFrame(Duration.minutes(1)));
		clock.setCycleCount(Animation.INDEFINITE);
		clock.play();
	}
}