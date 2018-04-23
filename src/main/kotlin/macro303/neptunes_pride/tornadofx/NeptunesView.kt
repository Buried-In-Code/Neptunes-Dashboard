package macro303.neptunes_pride.tornadofx

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.TabPane
import javafx.scene.layout.GridPane
import macro303.neptunes_pride.Connection
import macro303.neptunes_pride.Player
import tornadofx.*

internal class NeptunesView : View("Neptunes Pride") {
	private val model: NeptunesModel by inject()

	override val root = borderpane {
		minHeight = 400.toDouble()
		minWidth = 700.toDouble()
		padding = Insets(10.toDouble())
		center {
			tabpane {
				tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
				tab(text = "Game") {
					gridpane {
						padding = Insets(5.toDouble())
						hgap = 5.toDouble()
						constraintsForColumn(columnIndex = 0).percentWidth = 20.toDouble()
						row {
							label(observable = model.nameProperty) {
								gridpaneConstraints {
									columnSpan = 2
								}
								addClass(NeptunesStyles.headerLabel)
								GridPane.setHalignment(this, HPos.CENTER)
							}
						}
						row {
							label(text = "Started:") {
								addClass(NeptunesStyles.subHeaderLabel)
							}
							label(observable = model.startedProperty) {
								addClass(NeptunesStyles.informationLabel)
							}
						}
						row {
							label(text = "Paused:") {
								addClass(NeptunesStyles.subHeaderLabel)
							}
							label(observable = model.pausedProperty) {
								addClass(NeptunesStyles.informationLabel)
							}
						}
						row {
							label(text = "Stars to Win:") {
								addClass(NeptunesStyles.subHeaderLabel)
							}
							label(observable = model.victoryProperty) {
								addClass(NeptunesStyles.informationLabel)
							}
						}
						row {
							label(text = "Total Turns:") {
								addClass(NeptunesStyles.subHeaderLabel)
							}
							label(observable = model.turnProperty) {
								addClass(NeptunesStyles.informationLabel)
							}
						}
						row {
							label(text = "Turn Every:") {
								addClass(NeptunesStyles.subHeaderLabel)
							}
							label(observable = model.turnEveryProperty) {
								addClass(NeptunesStyles.informationLabel)
							}
						}
						row {
							label(text = "Next Turn:") {
								addClass(NeptunesStyles.subHeaderLabel)
							}
							label(observable = model.nextTurnProperty) {
								addClass(NeptunesStyles.informationLabel)
							}
						}
						row {
							label(text = "Payday Every:") {
								addClass(NeptunesStyles.subHeaderLabel)
							}
							label(observable = model.paydayEveryProperty) {
								addClass(NeptunesStyles.informationLabel)
							}
						}
						row {
							label(text = "Next Payday:") {
								addClass(NeptunesStyles.subHeaderLabel)
							}
							label(observable = model.nextPaydayProperty) {
								addClass(NeptunesStyles.informationLabel)
							}
						}
					}
				}
				tab(text = "Players") {
					tableview(items = model.playerProperty) {
						smartResize()
						column<Player, String>(title = "Alias") {
							SimpleStringProperty(it.value.alias)
						}.remainingWidth()
							.cellFormat {
								alignment = Pos.CENTER_LEFT
								text = it
							}
						column<Player, String>(title = "Name") {
							SimpleStringProperty(it.value.name)
						}.remainingWidth()
							.cellFormat {
								alignment = Pos.CENTER_LEFT
								text = it
							}
						column<Player, Int>(title = "Strength") {
							SimpleIntegerProperty(it.value.totalStrength).asObject()
						}.contentWidth()
							.cellFormat {
								alignment = Pos.CENTER
								text = it.toString()
							}
						column<Player, Int>(title = "Stars") {
							SimpleIntegerProperty(it.value.totalStars).asObject()
						}.contentWidth()
							.cellFormat {
								alignment = Pos.CENTER
								text = it.toString()
							}
						column<Player, Int>(title = "Fleets") {
							SimpleIntegerProperty(it.value.totalFleets).asObject()
						}.contentWidth()
							.cellFormat {
								alignment = Pos.CENTER
								text = it.toString()
							}
						nestedColumn(title = "Total Stats") {
							column<Player, Int>(title = "Economy") {
								SimpleIntegerProperty(it.value.totalEconomy).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
								}
							column<Player, Int>(title = "Industry") {
								SimpleIntegerProperty(it.value.totalIndustry).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
								}
							column<Player, Int>(title = "Science") {
								SimpleIntegerProperty(it.value.totalScience).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
								}
						}
						nestedColumn(title = "Technology Level") {
							column<Player, Int>(title = "Banking") {
								SimpleIntegerProperty(it.value.technologyMap["banking"]?.level ?: 0).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
								}
							column<Player, Int>(title = "Manufacturing") {
								SimpleIntegerProperty(it.value.technologyMap["manufacturing"]?.level ?: 0).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
								}
							column<Player, Int>(title = "Propulsion") {
								SimpleIntegerProperty(it.value.technologyMap["propulsion"]?.level ?: 0).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
								}
							column<Player, Int>(title = "Research") {
								SimpleIntegerProperty(it.value.technologyMap["research"]?.level ?: 0).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
								}
							column<Player, Int>(title = "Scanning") {
								SimpleIntegerProperty(it.value.technologyMap["scanning"]?.level ?: 0).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
								}
							column<Player, Int>(title = "Terraforming") {
								SimpleIntegerProperty(it.value.technologyMap["terraforming"]?.level ?: 0).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
								}
							column<Player, Int>(title = "Weapons") {
								SimpleIntegerProperty(it.value.technologyMap["weapons"]?.level ?: 0).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
								}
						}
					}
				}
				tab(text = "Stars") {
					isDisable = true
				}
				tab(text = "Fleets") {
					isDisable = true
				}
			}
		}
		bottom {
			vbox(spacing = 5) {
				hbox(spacing = 5) {
					alignment = Pos.CENTER
					label(text = "Last Updated:") {
						addClass(NeptunesStyles.subHeaderLabel)
					}
					label(observable = model.lastUpdatedProperty) {
						addClass(NeptunesStyles.informationLabel)
					}
				}
				hbox(spacing = 5) {
					alignment = Pos.CENTER
					button(text = "Refresh") {
						disableWhen {
							Connection.connectionUnavailableProperty
						}
						setOnAction {
							model.refreshGame()
						}
					}
				}
			}
		}
	}
}