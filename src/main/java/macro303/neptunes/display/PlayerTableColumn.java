package macro303.neptunes.display;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import macro303.neptunes.Player;

public class PlayerTableColumn<T> extends TableColumn<Player, T> {

	public PlayerTableColumn(String title, Pos alignment, Callback<CellDataFeatures<Player, T>, ObservableValue<T>> property) {
		this(title, alignment);
		setCellValueFactory(property);
	}

	public PlayerTableColumn(String title, Pos alignment) {
		super(title);
		setEditable(false);
		setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(T item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty) {
					if (item != null)
						setText(item.toString());
					setAlignment(alignment);
				}
			}
		});
	}
}