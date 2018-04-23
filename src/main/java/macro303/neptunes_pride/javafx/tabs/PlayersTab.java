package macro303.neptunes_pride.javafx.tabs;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import macro303.neptunes_pride.Player;
import macro303.neptunes_pride.javafx.NeptunesModel;

public class PlayersTab extends Tab {
	private NeptunesModel model;

	public PlayersTab(NeptunesModel model) {
		super("Players");
		this.model = model;
		setContent(setupContent());
	}

	private Node setupContent() {
		TableView<Player> playersTable = new TableView<>(model.playerProperty);

		TableColumn<Player, String> aliasColumn = new TableColumn<>("Alias");
		aliasColumn.setCellValueFactory(new PropertyValueFactory<>("alias"));
		aliasColumn.setSortable(true);
		aliasColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		aliasColumn.prefWidthProperty().bind(playersTable.widthProperty().divide(playersTable.getColumns().size() + 2).multiply(2));
		TableColumn<Player, String> nameColumn = new TableColumn<>("Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		nameColumn.setSortable(true);
		nameColumn.setStyle("-fx-alignment: CENTER-LEFT;");
		nameColumn.prefWidthProperty().bind(playersTable.widthProperty().divide(playersTable.getColumns().size() + 2).multiply(2));
		TableColumn<Player, Integer> strengthColumn = new TableColumn<>("Strength");
		strengthColumn.setCellValueFactory(new PropertyValueFactory<>("totalStrength"));
		strengthColumn.setSortable(true);
		strengthColumn.setStyle("-fx-alignment: CENTER;");
		strengthColumn.prefWidthProperty().bind(playersTable.widthProperty().divide(playersTable.getColumns().size() + 2));
		TableColumn<Player, Integer> starsColumn = new TableColumn<>("Stars");
		starsColumn.setCellValueFactory(new PropertyValueFactory<>("totalStars"));
		starsColumn.setSortable(true);
		starsColumn.setStyle("-fx-alignment: CENTER;");
		starsColumn.prefWidthProperty().bind(playersTable.widthProperty().divide(playersTable.getColumns().size() + 2));
		TableColumn<Player, Integer> fleetsColumn = new TableColumn<>("Fleets");
		fleetsColumn.setCellValueFactory(new PropertyValueFactory<>("totalFleets"));
		fleetsColumn.setSortable(true);
		fleetsColumn.setStyle("-fx-alignment: CENTER;");
		fleetsColumn.prefWidthProperty().bind(playersTable.widthProperty().divide(playersTable.getColumns().size() + 2));

		TableColumn<Player, Integer> statsColumn = new TableColumn<>("Total Stats");
		TableColumn<Player, Integer> economyColumn = new TableColumn<>("Economy");
		economyColumn.setCellValueFactory(new PropertyValueFactory<>("totalEconomy"));
		economyColumn.setSortable(true);
		economyColumn.setStyle("-fx-alignment: CENTER;");
		economyColumn.prefWidthProperty().bind(playersTable.widthProperty().divide(playersTable.getColumns().size() + 2));
		TableColumn<Player, Integer> industryColumn = new TableColumn<>("Industry");
		industryColumn.setCellValueFactory(new PropertyValueFactory<>("totalIndustry"));
		industryColumn.setSortable(true);
		industryColumn.setStyle("-fx-alignment: CENTER;");
		industryColumn.prefWidthProperty().bind(playersTable.widthProperty().divide(playersTable.getColumns().size() + 2));
		TableColumn<Player, Integer> scienceColumn = new TableColumn<>("Science");
		scienceColumn.setCellValueFactory(new PropertyValueFactory<>("totalScience"));
		scienceColumn.setSortable(true);
		scienceColumn.setStyle("-fx-alignment: CENTER;");
		scienceColumn.prefWidthProperty().bind(playersTable.widthProperty().divide(playersTable.getColumns().size() + 2));
		statsColumn.getColumns().addAll(economyColumn, industryColumn, scienceColumn);

		TableColumn<Player, Integer> technologyColumn = new TableColumn<>("Technology Levels");
		TableColumn<Player, Integer> scanningColumn = new TableColumn<>("Scanning");
		scanningColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getTechnologyMap().get("scanning").getLevel()).asObject());
		scanningColumn.setSortable(true);
		scanningColumn.setStyle("-fx-alignment: CENTER;");
		scanningColumn.prefWidthProperty().bind(playersTable.widthProperty().divide(playersTable.getColumns().size() + 2));
		TableColumn<Player, Integer> propulsionColumn = new TableColumn<>("Propulsion");
		propulsionColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getTechnologyMap().get("propulsion").getLevel()).asObject());
		propulsionColumn.setSortable(true);
		propulsionColumn.setStyle("-fx-alignment: CENTER;");
		propulsionColumn.prefWidthProperty().bind(playersTable.widthProperty().divide(playersTable.getColumns().size() + 2));
		TableColumn<Player, Integer> terraformingColumn = new TableColumn<>("Terraforming");
		terraformingColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getTechnologyMap().get("terraforming").getLevel()).asObject());
		terraformingColumn.setSortable(true);
		terraformingColumn.setStyle("-fx-alignment: CENTER;");
		terraformingColumn.prefWidthProperty().bind(playersTable.widthProperty().divide(playersTable.getColumns().size() + 2));
		TableColumn<Player, Integer> researchColumn = new TableColumn<>("Research");
		researchColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getTechnologyMap().get("research").getLevel()).asObject());
		researchColumn.setSortable(true);
		researchColumn.setStyle("-fx-alignment: CENTER;");
		researchColumn.prefWidthProperty().bind(playersTable.widthProperty().divide(playersTable.getColumns().size() + 2));
		TableColumn<Player, Integer> weaponsColumn = new TableColumn<>("Weapons");
		weaponsColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getTechnologyMap().get("weapons").getLevel()).asObject());
		weaponsColumn.setSortable(true);
		weaponsColumn.setStyle("-fx-alignment: CENTER;");
		weaponsColumn.prefWidthProperty().bind(playersTable.widthProperty().divide(playersTable.getColumns().size() + 2));
		TableColumn<Player, Integer> bankingColumn = new TableColumn<>("Banking");
		bankingColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getTechnologyMap().get("banking").getLevel()).asObject());
		bankingColumn.setSortable(true);
		bankingColumn.setStyle("-fx-alignment: CENTER;");
		bankingColumn.prefWidthProperty().bind(playersTable.widthProperty().divide(playersTable.getColumns().size() + 2));
		TableColumn<Player, Integer> manufacturingColumn = new TableColumn<>("Manufacturing");
		manufacturingColumn.setCellValueFactory(p -> new SimpleIntegerProperty(p.getValue().getTechnologyMap().get("manufacturing").getLevel()).asObject());
		manufacturingColumn.setSortable(true);
		manufacturingColumn.setStyle("-fx-alignment: CENTER;");
		manufacturingColumn.prefWidthProperty().bind(playersTable.widthProperty().divide(playersTable.getColumns().size() + 2));
		technologyColumn.getColumns().addAll(bankingColumn, manufacturingColumn, propulsionColumn, researchColumn, scanningColumn, terraformingColumn, weaponsColumn);

		playersTable.getColumns().addAll(aliasColumn, nameColumn, strengthColumn, starsColumn, fleetsColumn, statsColumn, technologyColumn);
		playersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		return playersTable;
	}
}
