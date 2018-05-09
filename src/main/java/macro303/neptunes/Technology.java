package macro303.neptunes;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-05-07.
 */
public class Technology {
	@NotNull
	@SerializedName("level")
	private Integer level = -1;
	@NotNull
	@SerializedName("value")
	private Double value = -1.0;

	public int getLevel() {
		return level;
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