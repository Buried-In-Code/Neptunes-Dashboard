package macro303.neptunes;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-05-07.
 */
public class Star {
	@NotNull
	@SerializedName("uid")
	private Integer uid = -1;           //Star ID
	@NotNull
	@SerializedName("n")
	private String n = "Unknown";       //Name
	@NotNull
	@SerializedName("puid")
	private Integer puid = -1;          //Player ID
	@NotNull
	@SerializedName("v")
	private String v = "Unknown";       //Visible
	@NotNull
	@SerializedName("y")
	private Double y = -1.0;            //Y Coordinate
	@NotNull
	@SerializedName("x")
	private Double x = -1.0;            //X Coordinate

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Star)) return false;

		Star star = (Star) o;

		if (!uid.equals(star.uid)) return false;
		if (!n.equals(star.n)) return false;
		if (!puid.equals(star.puid)) return false;
		if (!v.equals(star.v)) return false;
		if (!y.equals(star.y)) return false;
		return x.equals(star.x);
	}

	@Override
	public int hashCode() {
		int result = uid.hashCode();
		result = 31 * result + n.hashCode();
		result = 31 * result + puid.hashCode();
		result = 31 * result + v.hashCode();
		result = 31 * result + y.hashCode();
		result = 31 * result + x.hashCode();
		return result;
	}

	@Override
	public String toString() {
		return "Star{" +
				"uid=" + uid +
				", n='" + n + '\'' +
				", puid=" + puid +
				", v='" + v + '\'' +
				", y=" + y +
				", x=" + x +
				'}';
	}
}