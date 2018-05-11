package macro303.neptunes.display.tabs;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import macro303.neptunes.display.models.PlayersModel;
import macro303.neptunes.display.scene.IntegerTableColumn;
import macro303.neptunes.display.scene.MoneyTableColumn;
import macro303.neptunes.display.scene.StringTableColumn;
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

	@SuppressWarnings("unchecked")
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

		var aliasColumn = new StringTableColumn<Player>("Alias", new PropertyValueFactory<>("alias"));
		var nameColumn = new StringTableColumn<Player>("Name", new PropertyValueFactory<>("name"));
		var teamColumn = new StringTableColumn<Player>("Team", new PropertyValueFactory<>("team"));
		var starsColumn = new IntegerTableColumn<Player>("Stars", new PropertyValueFactory<>("totalStars"));
		var shipsColumn = new IntegerTableColumn<Player>("Ships", new PropertyValueFactory<>("totalShips"));
		var statsColumn = new TableColumn("Total Stats");
		var economyColumn = new IntegerTableColumn<Player>("Economy", new PropertyValueFactory<>("totalEconomy"));
		var economyTurnColumn = new MoneyTableColumn<Player>("Per Turn", new PropertyValueFactory<>("economyTurn"));
		var industryColumn = new IntegerTableColumn<Player>("Industry", new PropertyValueFactory<>("totalIndustry"));
		var industryTurnColumn = new IntegerTableColumn<Player>("Per Turn", new PropertyValueFactory<>("industryTurn"));
		var scienceColumn = new IntegerTableColumn<Player>("Science", new PropertyValueFactory<>("totalScience"));
		statsColumn.getColumns().addAll(economyColumn, economyTurnColumn, industryColumn, industryTurnColumn, scienceColumn);
		var technologyColumn = new TableColumn("Technology Level");
		var bankingColumn = new IntegerTableColumn<Player>("Banking");
		bankingColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("banking").levelProperty().asObject());
		var experimentationColumn = new IntegerTableColumn<Player>("Research");
		experimentationColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("research").levelProperty().asObject());
		var hyperspaceColumn = new IntegerTableColumn<Player>("Hyperspace");
		hyperspaceColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("propulsion").levelProperty().asObject());
		var manufacturingColumn = new IntegerTableColumn<Player>("Manufacturing");
		manufacturingColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("manufacturing").levelProperty().asObject());
		var scanningColumn = new IntegerTableColumn<Player>("Scanning");
		scanningColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("scanning").levelProperty().asObject());
		var terraformingColumn = new IntegerTableColumn<Player>("Terraforming");
		terraformingColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("terraforming").levelProperty().asObject());
		var weaponsColumn = new IntegerTableColumn<Player>("Weapons");
		weaponsColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("weapons").levelProperty().asObject());
		technologyColumn.getColumns().addAll(bankingColumn, experimentationColumn, hyperspaceColumn, manufacturingColumn, scanningColumn, terraformingColumn, weaponsColumn);
		table.getColumns().addAll(aliasColumn, nameColumn, teamColumn, starsColumn, shipsColumn, statsColumn, technologyColumn);

		return table;
	}
}