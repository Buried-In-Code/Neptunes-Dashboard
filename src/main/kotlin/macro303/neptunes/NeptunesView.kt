package macro303.neptunes

import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.Insets
import javafx.scene.control.TabPane
import javafx.scene.control.TableRow
import macro303.neptunes.game.GameModel
import macro303.neptunes.player.Player
import macro303.neptunes.player.PlayersModel
import macro303.neptunes.team.Team
import macro303.neptunes.team.TeamsModel
import tornadofx.*

/**
 * Created by Macro303 on 2018-05-08.
 */
class NeptunesView : View(title = "Neptune's Pride") {
	val game: GameModel by inject()
	val player: PlayersModel by inject()
	val team: TeamsModel by inject()

	override val root = borderpane {
		center {
			tabpane {
				padding = Insets(10.0)
				tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
				tab(text = "Game") {
					gridpane {
						padding = Insets(5.0)
						hgap = 5.0
						vgap = 5.0
						row {
							label(observable = game.nameProperty()) {
								gridpaneConstraints {
									columnSpan = 2
								}
							}
						}
						row {
							label(text = "Started: ")
							label(observable = game.startedProperty())
						}
						row {
							label(text = "Start Time: ")
							label(observable = game.startTimeProperty())
						}
						row {
							label(text = "Paused: ")
							label(observable = game.pausedProperty())
						}
						row {
							label(text = "Stars to Win: ")
							label(observable = game.victoryProperty())
						}
						row {
							label(text = "Game Over: ")
							label(observable = game.gameOverProperty())
						}
						row {
							label(text = "Next Turn: ")
							label(observable = game.turnTimeProperty())
						}
						constraintsForColumn(0).percentWidth = 20.0
					}
				}
				tab(text = "Players") {
					tableview(player.players) {
						readonlyColumn("Alias", Player::alias) {
							cellFormat {
								text = it.toString()
							}
						}.remainingWidth()
						readonlyColumn("Name", Player::name) {
							cellFormat {
								text = it.toString()
							}
						}.contentWidth(padding = 5.0, useAsMin = true)
						readonlyColumn("Team", Player::team) {
							cellFormat {
								text = it.toString()
							}
						}.contentWidth(padding = 5.0, useAsMin = true)
						readonlyColumn("Stars", Player::totalStars) {
							cellFormat {
								text = String.format("%,d", it)
							}
						}.contentWidth(padding = 5.0, useAsMin = true)
						column<Player, Double>(
							"Victory",
							{ SimpleDoubleProperty(((it.value.totalStars + 0.0) / (game.totalStarsProperty().value + 0.0)) * 100.0).asObject() })
							.contentWidth(padding = 5.0, useAsMin = true)
							.cellFormat {
								text = String.format("%.01f", item) + "%"
							}
						readonlyColumn("Ships", Player::totalShips) {
							cellFormat {
								text = String.format("%,d", it)
							}
						}.contentWidth(padding = 5.0, useAsMin = true)
						nestedColumn("Total Stats") {
							readonlyColumn("Economy", Player::totalEconomy) {
								cellFormat {
									text = String.format("%,d", it)
								}
							}.contentWidth(padding = 5.0, useAsMin = true)
							readonlyColumn("$/Turn", Player::economyTurn) {
								cellFormat {
									text = String.format("$%,d", it)
								}
							}.contentWidth(padding = 5.0, useAsMin = true)
							readonlyColumn("Industry", Player::totalIndustry) {
								cellFormat {
									text = String.format("%,d", it)
								}
							}.contentWidth(padding = 5.0, useAsMin = true)
							readonlyColumn("Ships/Turn", Player::industryTurn) {
								cellFormat {
									text = String.format("%,d", it)
								}
							}.contentWidth(padding = 5.0, useAsMin = true)
							readonlyColumn("Science", Player::totalScience) {
								cellFormat {
									text = String.format("%,d", it)
								}
							}.contentWidth(padding = 5.0, useAsMin = true)
						}
						nestedColumn("Technology") {
							column<Player, Int>(
								"Banking",
								{ it.value.technologies["banking"]!!.levelProperty.asObject() })
								.contentWidth(padding = 5.0, useAsMin = true)
							column<Player, Int>(
								"Experimentation",
								{ it.value.technologies["research"]!!.levelProperty.asObject() })
								.contentWidth(padding = 5.0, useAsMin = true)
							column<Player, Int>(
								"Hyperspace",
								{ it.value.technologies["propulsion"]!!.levelProperty.asObject() })
								.contentWidth(padding = 5.0, useAsMin = true)
							column<Player, Int>(
								"Manufacturing",
								{ it.value.technologies["manufacturing"]!!.levelProperty.asObject() })
								.contentWidth(padding = 5.0, useAsMin = true)
							column<Player, Int>(
								"Scanning",
								{ it.value.technologies["scanning"]!!.levelProperty.asObject() })
								.contentWidth(padding = 5.0, useAsMin = true)
							column<Player, Int>(
								"Terraforming",
								{ it.value.technologies["terraforming"]!!.levelProperty.asObject() })
								.contentWidth(padding = 5.0, useAsMin = true)
							column<Player, Int>(
								"Weapons",
								{ it.value.technologies["weapons"]!!.levelProperty.asObject() })
								.contentWidth(padding = 5.0, useAsMin = true)
						}

						columnResizePolicy = SmartResize.POLICY
						setRowFactory({
							object : TableRow<Player>() {
								public override fun updateItem(item: Player?, empty: Boolean) {
									super.updateItem(item, empty)
									style = if (!empty && item != null && (item.ai || item.conceded)) {
										"-fx-background: tomato"
									} else
										""
								}
							}
						})
					}
				}
				tab(text = "Teams") {
					tableview(team.teams) {
						readonlyColumn("Name", Team::name) {
						}.remainingWidth()
						readonlyColumn("Stars", Team::totalStars) {
							cellFormat {
								text = String.format("%,d", it)
							}
						}.contentWidth(padding = 5.0, useAsMin = true)
						column<Team, Double>(
							"Victory",
							{ SimpleDoubleProperty(((it.value.totalStars + 0.0) / (game.totalStarsProperty().value + 0.0)) * 100.0).asObject() })
							.contentWidth(padding = 5.0, useAsMin = true)
							.cellFormat {
								text = String.format("%.01f", item) + "%"
							}
						readonlyColumn("Ships", Team::totalShips) {
							cellFormat {
								text = String.format("%,d", it)
							}
						}.contentWidth(padding = 5.0, useAsMin = true)
						nestedColumn("Total Stats") {
							readonlyColumn("Economy", Team::totalEconomy) {
								cellFormat {
									text = String.format("%,d", it)
								}
							}.contentWidth(padding = 5.0, useAsMin = true)
							readonlyColumn("$/Turn", Team::economyTurn) {
								cellFormat {
									text = String.format("$%,d", it)
								}
							}.contentWidth(padding = 5.0, useAsMin = true)
							readonlyColumn("Industry", Team::totalIndustry) {
								cellFormat {
									text = String.format("%,d", it)
								}
							}.contentWidth(padding = 5.0, useAsMin = true)
							readonlyColumn("Ships/Turn", Team::industryTurn) {
								cellFormat {
									text = String.format("%,d", it)
								}
							}.contentWidth(padding = 5.0, useAsMin = true)
							readonlyColumn("Science", Team::totalScience) {
								cellFormat {
									text = String.format("%,d", it)
								}
							}.contentWidth(padding = 5.0, useAsMin = true)
						}

						columnResizePolicy = SmartResize.POLICY
						setRowFactory({
							object : TableRow<Team>() {
								public override fun updateItem(item: Team?, empty: Boolean) {
									super.updateItem(item, empty)
									style = if (!empty && item != null && item.isLost) {
										"-fx-background: tomato"
									} else
										""
								}
							}
						})
					}
				}
			}
		}
	}

/*val root: Parent
	get() {
		val root = BorderPane()
		root.padding = Insets(10.0)

		root.center = center
		root.bottom = bottom

		return root
	}

private val center: Node
	get() {
		val tabPane = TabPane()
		tabPane.tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
		tabPane.padding = Insets(5.0)
		val gameTab = GameTab(gameModel)
		val playersTab = PlayersTab(playersModel)
		val teamsTab = TeamsTab(teamsModel)
		tabPane.tabs.addAll(gameTab, playersTab, teamsTab)
		return tabPane
	}

private val bottom: Node
	get() {
		val hbox = HBox()
		hbox.alignment = Pos.CENTER

		val refresh = Button("Refresh")
		refresh.setOnAction { event ->
			val connection = Connection()
			connection.setOnSucceeded { success ->
				val game = success.source.value as Game
				gameModel.updateModel(game)
				playersModel.updateModel(game)
				teamsModel.updateModel(game)
			}
			Thread(connection).start()
		}
		hbox.children.add(refresh)

		return hbox
	}*/
}