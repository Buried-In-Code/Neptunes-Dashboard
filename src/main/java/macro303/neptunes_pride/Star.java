package macro303.neptunes_pride;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.NotNull;

/**
 * Created by Macro303 on 2018-04-17.
 */
public class Star implements Comparable<Star> {
	@SerializedName("uid")
	private int starID;
	@SerializedName("n")
	private String name;
	@SerializedName("puid")
	private int playerID;
	private String v;
	@SerializedName("y")
	private double yCoordinate;
	@SerializedName("x")
	private double xCoordinate;

	public int getStarID() {
		return starID;
	}

	public String getName() {
		return name;
	}

	public int getPlayerID() {
		return playerID;
	}

	public String getV() {
		return v;
	}

	public double getyCoordinate() {
		return yCoordinate;
	}

	public double getxCoordinate() {
		return xCoordinate;
	}

	@Override
	public int compareTo(@NotNull Star other) {
		return 0;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Star)) return false;

		Star star = (Star) o;

		if (starID != star.starID) return false;
		if (playerID != star.playerID) return false;
		if (Double.compare(star.yCoordinate, yCoordinate) != 0) return false;
		if (Double.compare(star.xCoordinate, xCoordinate) != 0) return false;
		if (name != null ? !name.equals(star.name) : star.name != null) return false;
		return v != null ? v.equals(star.v) : star.v == null;
	}

	@Override
	public int hashCode() {
		int result;
		long temp;
		result = starID;
		result = 31 * result + (name != null ? name.hashCode() : 0);
		result = 31 * result + playerID;
		result = 31 * result + (v != null ? v.hashCode() : 0);
		temp = Double.doubleToLongBits(yCoordinate);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(xCoordinate);
		result = 31 * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public String toString() {
		return "Star{" +
				"starID=" + starID +
				", name='" + name + '\'' +
				", playerID=" + playerID +
				", v='" + v + '\'' +
				", yCoordinate=" + yCoordinate +
				", xCoordinate=" + xCoordinate +
				'}';
	}
}