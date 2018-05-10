package macro303.neptunes.display.tabs;

import javafx.beans.property.SimpleObjectProperty;
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

import java.text.DecimalFormat;

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

		var aliasColumn = new PlayerTableColumn<>("Alias", new PropertyValueFactory<Player, String>("alias"));
		var nameColumn = new PlayerTableColumn<>("Name", new PropertyValueFactory<Player, String>("name"));
		var teamColumn = new PlayerTableColumn<>("Team", new PropertyValueFactory<Player, String>("team"));
		var starsColumn = new PlayerTableColumn<>("Stars", new PropertyValueFactory<Player, Integer>("totalStars"));
		var shipsColumn = new PlayerTableColumn<>("Ships", new PropertyValueFactory<Player, Integer>("totalShips"));
		var statsColumn = new TableColumn("Total Stats");
		var economyColumn = new PlayerTableColumn<>("Economy", new PropertyValueFactory<Player, Integer>("totalEconomy"));
		var economyTurnColumn = new PlayerTableColumn<>("$Per Turn", new PropertyValueFactory<Player, Integer>("economyTurn"));
		var industryColumn = new PlayerTableColumn<>("Industry", new PropertyValueFactory<Player, Integer>("totalIndustry"));
		var industryTurnColumn = new PlayerTableColumn<>("Per Turn");
		industryTurnColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(new DecimalFormat("00").format(param.getValue().getIndustryTurn())));
		var scienceColumn = new PlayerTableColumn<>("Science", new PropertyValueFactory<Player, Integer>("totalScience"));
		statsColumn.getColumns().addAll(economyColumn, economyTurnColumn, industryColumn, industryTurnColumn, scienceColumn);
		var technologyColumn = new TableColumn("Technology Level");
		var bankingColumn = new PlayerTableColumn<Integer>("Banking");
		bankingColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("banking").levelProperty().asObject());
		var experimentationColumn = new PlayerTableColumn<Integer>("Research");
		experimentationColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("research").levelProperty().asObject());
		var hyperspaceColumn = new PlayerTableColumn<Integer>("Hyperspace");
		hyperspaceColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("propulsion").levelProperty().asObject());
		var manufacturingColumn = new PlayerTableColumn<Integer>("Manufacturing");
		manufacturingColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("manufacturing").levelProperty().asObject());
		var scanningColumn = new PlayerTableColumn<Integer>("Scanning");
		scanningColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("scanning").levelProperty().asObject());
		var terraformingColumn = new PlayerTableColumn<Integer>("Terraforming");
		terraformingColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("terraforming").levelProperty().asObject());
		var weaponsColumn = new PlayerTableColumn<Integer>("Weapons");
		weaponsColumn.setCellValueFactory(param -> param.getValue().getTechnologies().get("weapons").levelProperty().asObject());
		technologyColumn.getColumns().addAll(bankingColumn, experimentationColumn, hyperspaceColumn, manufacturingColumn, scanningColumn, terraformingColumn, weaponsColumn);
		table.getColumns().addAll(aliasColumn, nameColumn, teamColumn, starsColumn, shipsColumn, statsColumn, technologyColumn);

		return table;
	}
}