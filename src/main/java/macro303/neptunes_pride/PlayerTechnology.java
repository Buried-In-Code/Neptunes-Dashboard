package macro303.neptunes_pride;

import org.jetbrains.annotations.NotNull;

class PlayerTechnology implements Comparable<PlayerTechnology> {
	private Technology scanning = null;
	private Technology propulsion = null;
	private Technology terraforming = null;
	private Technology research = null;
	private Technology weapons = null;
	private Technology banking = null;
	private Technology manufacturing = null;

	Technology getScanning() {
		return scanning;
	}

	Technology getPropulsion() {
		return propulsion;
	}

	Technology getTerraforming() {
		return terraforming;
	}

	Technology getResearch() {
		return research;
	}

	Technology getWeapons() {
		return weapons;
	}

	Technology getBanking() {
		return banking;
	}

	Technology getManufacturing() {
		return manufacturing;
	}

	@Override
	public int compareTo(@NotNull PlayerTechnology other) {
		int p1 = scanning.getLevel() + propulsion.getLevel() + terraforming.getLevel() + research.getLevel() + weapons.getLevel() + banking.getLevel() + manufacturing.getLevel();
		int p2 = other.scanning.getLevel() + other.propulsion.getLevel() + other.terraforming.getLevel() + other.research.getLevel() + other.weapons.getLevel() + other.banking.getLevel() + other.manufacturing.getLevel();
		return p1 - p2;
	}
}