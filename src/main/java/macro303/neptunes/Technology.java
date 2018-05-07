package macro303.neptunes;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

public class Technology implements Comparable<Technology> {
	@SerializedName("level")
	private Integer level;
	@NotNull
	private String name = "Unknown";
	@SerializedName("value")
	private Double value;

	public int getLevel() {
		return level;
	}

	@NotNull
	public String getName() {
		return name;
	}

	public void setName(@NotNull String name) {
		this.name = name;
	}

	@Override
	public int compareTo(@NotNull Technology other) {
		if (name.compareToIgnoreCase(other.name) != 0)
			return name.compareToIgnoreCase(other.name);
		if (level.compareTo(other.level) != 0)
			return level.compareTo(other.level);
		return value.compareTo(other.value);
	}
}
