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
public class PercentageTableColumn<T> extends TableColumn<T, Double> {

	public PercentageTableColumn(@NotNull String title, @NotNull Callback<CellDataFeatures<T, Double>, ObservableValue<Double>> property) {
		this(title);
		setCellValueFactory(property);
	}

	public PercentageTableColumn(@NotNull String title) {
		super(title);
		setEditable(false);
		setCellFactory(param -> new TableCell<>() {
			@Override
			protected void updateItem(Double item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty) {
					if (item != null)
						setText(String.format("%.01f", item) + "%");
					setAlignment(Pos.CENTER_LEFT);
				}
			}
		});
	}
}