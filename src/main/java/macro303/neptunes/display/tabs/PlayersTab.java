package macro303.neptunes.display.tabs;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import macro303.neptunes.display.models.PlayersModel;
import macro303.neptunes.display.scene.PlayerTableColumn;
import macro303.neptunes.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-05-08.
 */
public class PlayersTab extends Tab {
	@NotNull
	private PlayersModel playersModel;

	public PlayersTab(@NotNull PlayersModel playersModel) {
		super("Players");
		this.playersModel = playersModel;
		setContent(getTab());
	}

	@NotNull
	private Node getTab() {
		var table = new TableView<>(playersModel.getPlayers());
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setRowFactory(param -> new TableRow<>() {
			@Override
			public void updateItem(Player item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty && item != null && (item.isAI() || item.isConceded())) {
					setStyle("-fx-background: tomato");
				} else
					setStyle("");
			}
		});

		var aliasColumn = new PlayerTableColumn<>("Alias", Pos.CENTER_LEFT, new PropertyValueFactory<Player, String>("alias"));
		var nameColumn = new PlayerTableColumn<>("Name", Pos.CENTER_LEFT, new PropertyValueFactory<Player, String>("name"));
		var teamColumn = new PlayerTableColumn<>("Team", Pos.CENTER_LEFT, new PropertyValueFactory<Player, String>("team"));
		var starsColumn = new PlayerTableColumn<>("Stars", Pos.CENTER, new PropertyValueFactory<Player, String>("totalStars"));
		var shipsColumn = new PlayerTableColumn<>("Ships", Pos.CENTER, new PropertyValueFactory<Player, String>("totalShips"));
		var fleetsColumn = new PlayerTableColumn<>("Fleets", Pos.CENTER, new PropertyValueFactory<Player, String>("totalFleets"));
		var statsColumn = new TableColumn("Total Stats");
		var economyColumn = new PlayerTableColumn<>("Economy", Pos.CENTER, new PropertyValueFactory<Player, String>("totalEconomy"));
		var industryColumn = new PlayerTableColumn<>("Industry", Pos.CENTER, new PropertyValueFactory<Player, String>("totalIndustry"));
		var scienceColumn = new PlayerTableColumn<>("Science", Pos.CENTER, new PropertyValueFactory<Player, String>("totalScience"));
		statsColumn.getColumns().addAll(economyColumn, industryColumn, scienceColumn);
		var technologyColumn = new TableColumn("Technology Level");
		var bankingColumn = new PlayerTableColumn<Integer>("Banking", Pos.CENTER);
		bankingColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("banking").levelProperty().asObject());
		var experimentationColumn = new PlayerTableColumn<Integer>("Experimentation", Pos.CENTER);
		experimentationColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("research").levelProperty().asObject());
		var hyperspaceColumn = new PlayerTableColumn<Integer>("Hyperspace", Pos.CENTER);
		hyperspaceColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("propulsion").levelProperty().asObject());
		var manufacturingColumn = new PlayerTableColumn<Integer>("Manufacturing", Pos.CENTER);
		manufacturingColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("manufacturing").levelProperty().asObject());
		var scanningColumn = new PlayerTableColumn<Integer>("Scanning", Pos.CENTER);
		scanningColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("scanning").levelProperty().asObject());
		var terraformingColumn = new PlayerTableColumn<Integer>("Terraforming", Pos.CENTER);
		terraformingColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("terraforming").levelProperty().asObject());
		var weaponsColumn = new PlayerTableColumn<Integer>("Weapons", Pos.CENTER);
		weaponsColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("weapons").levelProperty().asObject());
		technologyColumn.getColumns().addAll(bankingColumn, experimentationColumn, hyperspaceColumn, manufacturingColumn, scanningColumn, terraformingColumn, weaponsColumn);
		table.getColumns().addAll(aliasColumn, nameColumn, teamColumn, starsColumn, shipsColumn, fleetsColumn, statsColumn, technologyColumn);

		return table;
	}
}