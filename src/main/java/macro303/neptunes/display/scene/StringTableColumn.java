package macro303.neptunes.display.scene;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-05-11.
 */
public class StringTableColumn<T> extends TableColumn<T, String> {

	public StringTableColumn(@NotNull String title, @NotNull Callback<CellDataFeatures<T, String>, ObservableValue<String>> property) {
		this(title);
		setCellValueFactory(property);
	}

	public StringTableColumn(@NotNull String title) {
		super(title);
		setEditable(false);
		setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty) {
					if (item != null)
						setText(item);
					setAlignment(Pos.CENTER_LEFT);
				}
			}
		});
	}
}