package macro303.neptunes.display.models;

import macro303.neptunes.Game;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-05-09.
 */
public interface Model {

	void updateModel(@NotNull Game game);
}