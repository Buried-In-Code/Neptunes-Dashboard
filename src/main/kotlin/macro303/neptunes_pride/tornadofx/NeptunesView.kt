package macro303.neptunes_pride.tornadofx

import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleStringProperty
import javafx.geometry.HPos
import javafx.geometry.Insets
import javafx.geometry.Pos
import javafx.scene.control.TabPane
import javafx.scene.layout.GridPane
import macro303.neptunes_pride.player.Player
import macro303.neptunes_pride.player.PlayerViewModel
import tornadofx.*

internal class NeptunesView : View("Neptunes Pride") {
	private val model: NeptunesModel by inject()
	val playerModel = PlayerViewModel()

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
							label(observable = model.hasStartedProperty) {
								addClass(NeptunesStyles.informationLabel)
							}
						}
						row {
							label(text = "Started Time:") {
								addClass(NeptunesStyles.subHeaderLabel)
							}
							label(observable = model.startTimeProperty) {
								addClass(NeptunesStyles.informationLabel)
							}
						}
						row {
							label(text = "Paused:") {
								addClass(NeptunesStyles.subHeaderLabel)
							}
							label(observable = model.isPausedProperty) {
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
							label(text = "Game Over:") {
								addClass(NeptunesStyles.subHeaderLabel)
							}
							label(observable = model.finishedProperty) {
								addClass(NeptunesStyles.informationLabel)
							}
						}
						row {
							label(text = "Current Turn:") {
								addClass(NeptunesStyles.subHeaderLabel)
							}
							label(observable = model.currentTurnProperty) {
								addClass(NeptunesStyles.informationLabel)
							}
						}
						row {
							label(text = "Turn Rate:") {
								addClass(NeptunesStyles.subHeaderLabel)
							}
							label(observable = model.turnRateProperty) {
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
							label(text = "Payday Rate:") {
								addClass(NeptunesStyles.subHeaderLabel)
							}
							label(observable = model.payRateProperty) {
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
					tableview(items = model.playersProperty) {
						smartResize()
						column<Player, String>(title = "Alias") {
							SimpleStringProperty(it.value?.alias ?: "INVALID")
						}.remainingWidth()
							.cellFormat {
								alignment = Pos.CENTER_LEFT
								text = it
								if (rowItem.isAI || rowItem.hasConceded)
									addClass(NeptunesStyles.strikethroughCell)
							}
						column<Player, String>(title = "Name") {
							SimpleStringProperty(it.value?.name ?: "INVALID")
						}.remainingWidth()
							.cellFormat {
								alignment = Pos.CENTER_LEFT
								text = it
								if (rowItem.isAI || rowItem.hasConceded)
									addClass(NeptunesStyles.strikethroughCell)
							}
						column<Player, Int>(title = "Stars") {
							SimpleIntegerProperty(it.value?.totalStars ?: -1).asObject()
						}.contentWidth()
							.cellFormat {
								alignment = Pos.CENTER
								text = it.toString()
								if (rowItem.isAI || rowItem.hasConceded)
									addClass(NeptunesStyles.strikethroughCell)
							}
						column<Player, Int>(title = "Ships") {
							SimpleIntegerProperty(it.value?.totalShips ?: -1).asObject()
						}.contentWidth()
							.cellFormat {
								alignment = Pos.CENTER
								text = it.toString()
								if (rowItem.isAI || rowItem.hasConceded)
									addClass(NeptunesStyles.strikethroughCell)
							}
						column<Player, Int>(title = "Fleets") {
							SimpleIntegerProperty(it.value?.totalFleets ?: -1).asObject()
						}.contentWidth()
							.cellFormat {
								alignment = Pos.CENTER
								text = it.toString()
								if (rowItem.isAI || rowItem.hasConceded)
									addClass(NeptunesStyles.strikethroughCell)
							}
						nestedColumn(title = "Total Stats") {
							column<Player, Int>(title = "Economy") {
								SimpleIntegerProperty(it.value?.totalEconomy ?: -1).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
									if (rowItem.isAI || rowItem.hasConceded)
										addClass(NeptunesStyles.strikethroughCell)
								}
							column<Player, Int>(title = "Industry") {
								SimpleIntegerProperty(it.value?.totalIndustry ?: -1).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
									if (rowItem.isAI || rowItem.hasConceded)
										addClass(NeptunesStyles.strikethroughCell)
								}
							column<Player, Int>(title = "Science") {
								SimpleIntegerProperty(it.value?.totalScience ?: -1).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
									if (rowItem.isAI || rowItem.hasConceded)
										addClass(NeptunesStyles.strikethroughCell)
								}
						}
						nestedColumn(title = "Technology Level") {
							column<Player, Int>(title = "Banking") {
								SimpleIntegerProperty(it.value?.technologyMap?.get("banking")?.level ?: -1).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
									if (rowItem.isAI || rowItem.hasConceded)
										addClass(NeptunesStyles.strikethroughCell)
								}
							column<Player, Int>(title = "Experimentation") {
								SimpleIntegerProperty(it.value?.technologyMap?.get("research")?.level ?: -1).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
									if (rowItem.isAI || rowItem.hasConceded)
										addClass(NeptunesStyles.strikethroughCell)
								}
							column<Player, Int>(title = "Hyperspace") {
								SimpleIntegerProperty(
									it.value?.technologyMap?.get("propulsion")?.level ?: -1
								).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
									if (rowItem.isAI || rowItem.hasConceded)
										addClass(NeptunesStyles.strikethroughCell)
								}
							column<Player, Int>(title = "Manufacturing") {
								SimpleIntegerProperty(
									it.value?.technologyMap?.get("manufacturing")?.level ?: -1
								).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
									if (rowItem.isAI || rowItem.hasConceded)
										addClass(NeptunesStyles.strikethroughCell)
								}
							column<Player, Int>(title = "Scanning") {
								SimpleIntegerProperty(it.value?.technologyMap?.get("scanning")?.level ?: -1).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
									if (rowItem.isAI || rowItem.hasConceded)
										addClass(NeptunesStyles.strikethroughCell)
								}
							column<Player, Int>(title = "Terraforming") {
								SimpleIntegerProperty(
									it.value?.technologyMap?.get("terraforming")?.level ?: -1
								).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
									if (rowItem.isAI || rowItem.hasConceded)
										addClass(NeptunesStyles.strikethroughCell)
								}
							column<Player, Int>(title = "Weapons") {
								SimpleIntegerProperty(it.value?.technologyMap?.get("weapons")?.level ?: -1).asObject()
							}.contentWidth()
								.cellFormat {
									alignment = Pos.CENTER
									text = it.toString()
									if (rowItem.isAI || rowItem.hasConceded)
										addClass(NeptunesStyles.strikethroughCell)
								}
						}
						bindSelected(playerModel)
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
						setOnAction {
							model.refreshGame()
						}
					}
				}
			}
		}
	}
}