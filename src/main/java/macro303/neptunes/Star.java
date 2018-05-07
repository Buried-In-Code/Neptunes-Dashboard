package macro303.neptunes;

import com.google.gson.annotations.SerializedName;

public class Star {
	@SerializedName("uid")
	private Integer uid;        //Star ID
	@SerializedName("n")
	private String n;           //Name
	@SerializedName("puid")
	private Integer puid;       //Player ID
	@SerializedName("v")
	private String v;           //Visible
	@SerializedName("y")
	private Double y;           //Y Coordinate
	@SerializedName("x")
	private Double x;           //X Coordinate
}
