package macro303.neptunes.display.scene;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import macro303.neptunes.Team;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-05-08.
 */
public class TeamTableColumn<T> extends TableColumn<Team, T> {

	public TeamTableColumn(@NotNull String title, @NotNull Callback<CellDataFeatures<Team, T>, ObservableValue<T>> property) {
		this(title);
		setCellValueFactory(property);
	}

	public TeamTableColumn(@NotNull String title) {
		super(title);
		setEditable(false);
		setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(T item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty) {
					if (item != null)
						setText(item.toString());
					setAlignment(Pos.CENTER_LEFT);
				}
			}
		});
	}
}