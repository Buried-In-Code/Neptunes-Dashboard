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
public class IntegerTableColumn<T> extends TableColumn<T, Integer> {

	public IntegerTableColumn(@NotNull String title, @NotNull Callback<CellDataFeatures<T, Integer>, ObservableValue<Integer>> property) {
		this(title);
		setCellValueFactory(property);
	}

	public IntegerTableColumn(@NotNull String title) {
		super(title);
		setEditable(false);
		setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty) {
					if (item != null)
						setText(String.format("%,d", item));
					setAlignment(Pos.CENTER_LEFT);
				}
			}
		});
	}
}