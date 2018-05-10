package macro303.neptunes.technology;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-05-07.
 */
public class Technology {
	@NotNull
	private IntegerProperty level;
	@NotNull
	private DoubleProperty value;

	public Technology(int level, double value) {
		this.level = new SimpleIntegerProperty(level);
		this.value = new SimpleDoubleProperty(value);
	}

	public int getLevel() {
		return level.get();
	}

	public void setLevel(int level) {
		this.level.set(level);
	}

	public @NotNull IntegerProperty levelProperty() {
		return level;
	}

	public double getValue() {
		return value.get();
	}

	public void setValue(double value) {
		this.value.set(value);
	}

	public @NotNull DoubleProperty valueProperty() {
		return value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Technology)) return false;

		Technology that = (Technology) o;

		if (!level.equals(that.level)) return false;
		return value.equals(that.value);
	}

	@Override
	public int hashCode() {
		int result = level.hashCode();
		result = 31 * result + value.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Technology{" +
				"level=" + level +
				", value=" + value +
				'}';
	}
}