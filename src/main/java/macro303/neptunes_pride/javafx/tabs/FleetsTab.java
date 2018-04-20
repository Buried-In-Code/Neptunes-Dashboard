package macro303.neptunes_pride.javafx.tabs;

import javafx.scene.Node;
import javafx.scene.control.Tab;
import macro303.neptunes_pride.javafx.NeptunesModel;

public class FleetsTab extends Tab {
	private NeptunesModel model;

	public FleetsTab(NeptunesModel model) {
		super("Fleets");
		this.model = model;
		setContent(setupContent());
	}

	private Node setupContent(){
		return null;
	}
}
