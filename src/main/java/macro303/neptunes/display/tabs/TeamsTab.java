package macro303.neptunes.display.tabs;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import macro303.neptunes.Team;
import macro303.neptunes.display.models.TeamsModel;
import macro303.neptunes.display.scene.IntegerTableColumn;
import macro303.neptunes.display.scene.MoneyTableColumn;
import macro303.neptunes.display.scene.StringTableColumn;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-05-08.
 */
public class TeamsTab extends Tab {
	@NotNull
	private TeamsModel teamsModel;

	public TeamsTab(@NotNull TeamsModel teamsModel) {
		super("Teams");
		this.teamsModel = teamsModel;
		setContent(getTab());
	}

	@SuppressWarnings("unchecked")
	@NotNull
	private Node getTab() {
		var table = new TableView<>(teamsModel.getTeams());
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		table.setRowFactory(param -> new TableRow<>() {
			@Override
			public void updateItem(Team item, boolean empty) {
				super.updateItem(item, empty);
				if (!empty && item != null && item.isLost()) {
					setStyle("-fx-background: tomato");
				} else
					setStyle("");
			}
		});

		var nameColumn = new StringTableColumn<Team>("Name", new PropertyValueFactory<>("name"));
		var starsColumn = new IntegerTableColumn<Team>("Stars", new PropertyValueFactory<>("totalStars"));
		var shipsColumn = new IntegerTableColumn<Team>("Ships", new PropertyValueFactory<>("totalShips"));
		var statsColumn = new TableColumn("Total Stats");
		var economyColumn = new IntegerTableColumn<Team>("Economy", new PropertyValueFactory<>("totalEconomy"));
		var economyTurnColumn = new MoneyTableColumn<Team>("Per Turn", new PropertyValueFactory<>("economyTurn"));
		var industryColumn = new IntegerTableColumn<Team>("Industry", new PropertyValueFactory<>("totalIndustry"));
		var industryTurnColumn = new IntegerTableColumn<Team>("Per Turn", new PropertyValueFactory<>("industryTurn"));
		var scienceColumn = new IntegerTableColumn<Team>("Science", new PropertyValueFactory<>("totalScience"));
		statsColumn.getColumns().addAll(economyColumn, economyTurnColumn, industryColumn, industryTurnColumn, scienceColumn);
		table.getColumns().addAll(nameColumn, starsColumn, shipsColumn, statsColumn);

		return table;
	}
}