package macro.neptunes

import javafx.beans.property.SimpleDoubleProperty
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.TabPane
import javafx.scene.control.TableRow
import javafx.scene.layout.GridPane
import javafx.scene.layout.Priority
import macro.neptunes.game.GameModel
import macro.neptunes.player.Player
import macro.neptunes.player.PlayersModel
import macro.neptunes.team.Team
import macro.neptunes.team.TeamsModel
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
					borderpane {
						padding = Insets(5.0)
						center {
							gridpane {
								padding = Insets(5.0)
								hgap = 5.0
								vgap = 5.0
								row {
									label(observable = game.nameProperty()) {
										addClass(NeptunesStylesheet.headerLabel)
										gridpaneConstraints {
											columnSpan = 2
										}
										GridPane.setHalignment(this, HPos.CENTER)
									}
								}
								row {
									label(text = "Started: ") {
										addClass(NeptunesStylesheet.subHeaderLabel)
									}
									label(observable = game.startedProperty())
								}
								row {
									label(text = "Start Time: ") {
										addClass(NeptunesStylesheet.subHeaderLabel)
									}
									label(observable = game.startTimeProperty())
								}
								row {
									label(text = "Paused: ") {
										addClass(NeptunesStylesheet.subHeaderLabel)
									}
									label(observable = game.pausedProperty())
								}
								row {
									label(text = "Stars to Win: ") {
										addClass(NeptunesStylesheet.subHeaderLabel)
									}
									label(observable = game.victoryProperty())
								}
								row {
									label(text = "Game Over: ") {
										addClass(NeptunesStylesheet.subHeaderLabel)
									}
									label(observable = game.gameOverProperty())
								}
								row {
									label(text = "Next Turn: ") {
										addClass(NeptunesStylesheet.subHeaderLabel)
									}
									label(observable = game.turnTimeProperty())
								}
								constraintsForColumn(0).percentWidth = 20.0
							}
						}
						bottom {
							vbox(spacing = 5) {
								padding = Insets(5.0)
								alignment = Pos.TOP_CENTER
								progressbar {
									useMaxWidth = true
									visibleProperty().bind(game.loading)
									vgrow = Priority.SOMETIMES
								}
							}
						}
					}
				}
				tab(text = "Players") {
					borderpane {
						padding = Insets(5.0)
						center {
							tableview(player.players) {
								readonlyColumn("Alias", Player::alias) {
								}.remainingWidth()
								readonlyColumn("Name", Player::name) {
								}.contentWidth(padding = 2.0, useAsMin = true)
								readonlyColumn("Team", Player::team) {
								}.contentWidth(padding = 2.0, useAsMin = true)
								readonlyColumn("Stars", Player::totalStars) {
									cellFormat {
										text = String.format("%,d", it)
										alignment = Pos.CENTER_RIGHT
									}
								}.contentWidth(padding = 2.0, useAsMin = true)
								column<Player, Double>(
									"Victory",
									{ SimpleDoubleProperty(((it.value.totalStars + 0.0) / (game.totalStarsProperty().value + 0.0)) * 100.0).asObject() })
									.contentWidth(padding = 2.0, useAsMin = true)
									.cellFormat {
										text = String.format("%.01f", item) + "%"
										alignment = Pos.CENTER_RIGHT
									}
								readonlyColumn("Ships", Player::totalShips) {
									cellFormat {
										text = String.format("%,d", it)
										alignment = Pos.CENTER_RIGHT
									}
								}.contentWidth(padding = 2.0, useAsMin = true)
								nestedColumn("Total Stats") {
									readonlyColumn("Economy", Player::totalEconomy) {
										cellFormat {
											text = String.format("%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									}.contentWidth(padding = 2.0, useAsMin = true)
									readonlyColumn("$/Turn", Player::economyTurn) {
										cellFormat {
											text = String.format("$%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									}.contentWidth(padding = 2.0, useAsMin = true)
									readonlyColumn("Industry", Player::totalIndustry) {
										cellFormat {
											text = String.format("%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									}.contentWidth(padding = 2.0, useAsMin = true)
									readonlyColumn("Ships/Turn", Player::industryTurn) {
										cellFormat {
											text = String.format("%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									}.contentWidth(padding = 2.0, useAsMin = true)
									readonlyColumn("Science", Player::totalScience) {
										cellFormat {
											text = String.format("%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									}.contentWidth(padding = 2.0, useAsMin = true)
								}
								nestedColumn("Technology") {
									column<Player, Int>(
										"Banking",
										{ it.value.technologies["banking"]!!.levelProperty.asObject() })
										.contentWidth(padding = 2.0, useAsMin = true)
										.cellFormat {
											text = String.format("%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									column<Player, Int>(
										"Experimentation",
										{ it.value.technologies["research"]!!.levelProperty.asObject() })
										.contentWidth(padding = 2.0, useAsMin = true)
										.cellFormat {
											text = String.format("%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									column<Player, Int>(
										"Hyperspace",
										{ it.value.technologies["propulsion"]!!.levelProperty.asObject() })
										.contentWidth(padding = 2.0, useAsMin = true)
										.cellFormat {
											text = String.format("%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									column<Player, Int>(
										"Manufacturing",
										{ it.value.technologies["manufacturing"]!!.levelProperty.asObject() })
										.contentWidth(padding = 2.0, useAsMin = true)
										.cellFormat {
											text = String.format("%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									column<Player, Int>(
										"Scanning",
										{ it.value.technologies["scanning"]!!.levelProperty.asObject() })
										.contentWidth(padding = 2.0, useAsMin = true)
										.cellFormat {
											text = String.format("%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									column<Player, Int>(
										"Terraforming",
										{ it.value.technologies["terraforming"]!!.levelProperty.asObject() })
										.contentWidth(padding = 2.0, useAsMin = true)
										.cellFormat {
											text = String.format("%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									column<Player, Int>(
										"Weapons",
										{ it.value.technologies["weapons"]!!.levelProperty.asObject() })
										.contentWidth(padding = 2.0, useAsMin = true)
										.cellFormat {
											text = String.format("%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
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
						bottom {
							vbox(spacing = 5) {
								padding = Insets(5.0)
								alignment = Pos.TOP_CENTER
								progressbar {
									useMaxWidth = true
									visibleProperty().bind(player.loading)
									vgrow = Priority.SOMETIMES
								}
							}
						}
					}
				}
				tab(text = "Teams") {
					borderpane {
						padding = Insets(5.0)
						center {
							tableview(team.teams) {
								readonlyColumn("Name", Team::name) {
								}.remainingWidth()
								readonlyColumn("Stars", Team::totalStars) {
									cellFormat {
										text = String.format("%,d", it)
										alignment = Pos.CENTER_RIGHT
									}
								}.contentWidth(padding = 2.0, useAsMin = true)
								column<Team, Double>(
									"Victory",
									{ SimpleDoubleProperty(((it.value.totalStars + 0.0) / (game.totalStarsProperty().value + 0.0)) * 100.0).asObject() })
									.contentWidth(padding = 2.0, useAsMin = true)
									.cellFormat {
										text = String.format("%.01f", item) + "%"
										alignment = Pos.CENTER_RIGHT
									}
								readonlyColumn("Ships", Team::totalShips) {
									cellFormat {
										text = String.format("%,d", it)
										alignment = Pos.CENTER_RIGHT
									}
								}.contentWidth(padding = 2.0, useAsMin = true)
								nestedColumn("Total Stats") {
									readonlyColumn("Economy", Team::totalEconomy) {
										cellFormat {
											text = String.format("%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									}.contentWidth(padding = 2.0, useAsMin = true)
									readonlyColumn("$/Turn", Team::economyTurn) {
										cellFormat {
											text = String.format("$%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									}.contentWidth(padding = 2.0, useAsMin = true)
									readonlyColumn("Industry", Team::totalIndustry) {
										cellFormat {
											text = String.format("%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									}.contentWidth(padding = 2.0, useAsMin = true)
									readonlyColumn("Ships/Turn", Team::industryTurn) {
										cellFormat {
											text = String.format("%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									}.contentWidth(padding = 2.0, useAsMin = true)
									readonlyColumn("Science", Team::totalScience) {
										cellFormat {
											text = String.format("%,d", it)
											alignment = Pos.CENTER_RIGHT
										}
									}.contentWidth(padding = 2.0, useAsMin = true)
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
						bottom {
							vbox(spacing = 5) {
								padding = Insets(5.0)
								alignment = Pos.TOP_CENTER
								progressbar {
									useMaxWidth = true
									visibleProperty().bind(team.loading)
									vgrow = Priority.SOMETIMES
								}
							}
						}
					}
				}
			}
		}
		bottom {
			hbox(spacing = 5.0) {
				padding = Insets(5.0)
				alignment = Pos.CENTER
				button("Refresh") {
					setOnAction {
						game.updateModel()
						player.updateModel()
						team.updateModel()
					}
				}
			}
		}
	}
}