package macro303.neptunes_pride;

/**
 * Created by Macro303 on 2018-04-17.
 */
class Technology {
	private double value = 0.0;
	private int level = 0;

	double getValue() {
		return value;
	}

	int getLevel() {
		return level;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Technology)) return false;

		Technology that = (Technology) o;

		if (Double.compare(that.value, value) != 0) return false;
		return level == that.level;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		temp = Double.doubleToLongBits(value);
		result = (int) (temp ^ (temp >>> 32));
		result = 31 * result + level;
		return result;
	}

	@Override
	public String toString() {
		return "Technology{" +
				"value=" + value +
				", level=" + level +
				'}';
	}
}