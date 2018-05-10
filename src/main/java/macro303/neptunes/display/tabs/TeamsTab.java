package macro303.neptunes.display.tabs;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import macro303.neptunes.Team;
import macro303.neptunes.display.models.TeamsModel;
import macro303.neptunes.display.scene.TeamTableColumn;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

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

		var nameColumn = new TeamTableColumn<>("Name", new PropertyValueFactory<Team, String>("name"));
		var starsColumn = new TeamTableColumn<>("Stars", new PropertyValueFactory<Team, Integer>("totalStars"));
		var shipsColumn = new TeamTableColumn<>("Ships", new PropertyValueFactory<Team, Integer>("totalShips"));
		var statsColumn = new TableColumn("Total Stats");
		var economyColumn = new TeamTableColumn<>("Economy", new PropertyValueFactory<Team, Integer>("totalEconomy"));
		var economyTurnColumn = new TeamTableColumn<>("$Per Turn", new PropertyValueFactory<Team, Integer>("economyTurn"));
		var industryColumn = new TeamTableColumn<>("Industry", new PropertyValueFactory<Team, Integer>("totalIndustry"));
		var industryTurnColumn = new TeamTableColumn<>("Per Turn");
		industryTurnColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(new DecimalFormat("00").format(param.getValue().getIndustryTurn())));
		var scienceColumn = new TeamTableColumn<>("Science", new PropertyValueFactory<Team, Integer>("totalScience"));
		statsColumn.getColumns().addAll(economyColumn, economyTurnColumn, industryColumn, industryTurnColumn, scienceColumn);
		table.getColumns().addAll(nameColumn, starsColumn, shipsColumn, statsColumn);

		return table;
	}
}