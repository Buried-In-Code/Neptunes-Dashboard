package macro303.neptunes.display.tabs;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import macro303.neptunes.Player;
import macro303.neptunes.display.PlayerTableColumn;
import macro303.neptunes.display.models.PlayersModel;

public class PlayersTab extends Tab {
	private PlayersModel playersModel;

	public PlayersTab(PlayersModel playersModel) {
		super("Players");
		this.playersModel = playersModel;
		setContent(getTab());
	}

	private Node getTab() {
		var table = new TableView<>(playersModel.getPlayers());
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setRowFactory(param -> new TableRow<>() {
			@Override
			public void updateItem(Player item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty && item != null && (item.isAi() || item.isConceded())) {
					setStyle("-fx-background: tomato");
				} else
					setStyle("");
			}
		});

		var aliasColumn = new PlayerTableColumn<>("Alias", Pos.CENTER_LEFT, new PropertyValueFactory<Player, String>("alias"));
		var nameColumn = new PlayerTableColumn<>("Name", Pos.CENTER_LEFT, new PropertyValueFactory<Player, String>("name"));
		var teamColumn = new PlayerTableColumn<>("Team", Pos.CENTER_LEFT, new PropertyValueFactory<Player, String>("team"));
		var starsColumn = new PlayerTableColumn<>("Stars", Pos.CENTER, new PropertyValueFactory<Player, String>("totalStars"));
		var shipsColumn = new PlayerTableColumn<>("Ships", Pos.CENTER, new PropertyValueFactory<Player, String>("totalStrength"));
		var fleetsColumn = new PlayerTableColumn<>("Fleets", Pos.CENTER, new PropertyValueFactory<Player, String>("totalFleets"));
		var statsColumn = new TableColumn("Total Stats");
		var economyColumn = new PlayerTableColumn<>("Economy", Pos.CENTER, new PropertyValueFactory<Player, String>("totalEconomy"));
		var industryColumn = new PlayerTableColumn<>("Industry", Pos.CENTER, new PropertyValueFactory<Player, String>("totalIndustry"));
		var scienceColumn = new PlayerTableColumn<>("Science", Pos.CENTER, new PropertyValueFactory<Player, String>("totalScience"));
		statsColumn.getColumns().addAll(economyColumn, industryColumn, scienceColumn);
		var technologyColumn = new TableColumn("Technology Level");
		var bankingColumn = new PlayerTableColumn<Integer>("Banking", Pos.CENTER);
		bankingColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getTech().get("banking").getLevel()).asObject());
		var experimentationColumn = new PlayerTableColumn<Integer>("Experimentation", Pos.CENTER);
		experimentationColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getTech().get("research").getLevel()).asObject());
		var hyperspaceColumn = new PlayerTableColumn<Integer>("Hyperspace", Pos.CENTER);
		hyperspaceColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getTech().get("propulsion").getLevel()).asObject());
		var manufacturingColumn = new PlayerTableColumn<Integer>("Manufacturing", Pos.CENTER);
		manufacturingColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getTech().get("manufacturing").getLevel()).asObject());
		var scanningColumn = new PlayerTableColumn<Integer>("Scanning", Pos.CENTER);
		scanningColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getTech().get("scanning").getLevel()).asObject());
		var terraformingColumn = new PlayerTableColumn<Integer>("Terraforming", Pos.CENTER);
		terraformingColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getTech().get("terraforming").getLevel()).asObject());
		var weaponsColumn = new PlayerTableColumn<Integer>("Weapons", Pos.CENTER);
		weaponsColumn.setCellValueFactory(param -> new SimpleIntegerProperty(param.getValue().getTech().get("weapons").getLevel()).asObject());
		technologyColumn.getColumns().addAll(bankingColumn, experimentationColumn, hyperspaceColumn, manufacturingColumn, scanningColumn, terraformingColumn, weaponsColumn);
		table.getColumns().addAll(aliasColumn, nameColumn, teamColumn, starsColumn, shipsColumn, fleetsColumn, statsColumn, technologyColumn);

		return table;
	}
}
