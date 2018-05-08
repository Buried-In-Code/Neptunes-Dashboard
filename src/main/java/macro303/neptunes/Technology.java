package macro303.neptunes;

import com.google.gson.annotations.SerializedName;

public class Technology {
	@SerializedName("level")
	private Integer level;
	@SerializedName("value")
	private Double value;

	public int getLevel() {
		return level;
	}
}
