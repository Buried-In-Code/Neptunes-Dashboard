package macro303.neptunes_pride

/**
 * Created by Macro303 on 2018-04-17.
 */
internal object PlayerTechnology_kotlin : Comparable<PlayerTechnology_kotlin> {
	@JvmField
	internal var scanning: Technology_kotlin? = null
	@JvmField
	internal var propulsion: Technology_kotlin? = null
	@JvmField
	internal var terraforming: Technology_kotlin? = null
	@JvmField
	internal var research: Technology_kotlin? = null
	@JvmField
	internal var weapons: Technology_kotlin? = null
	@JvmField
	internal var banking: Technology_kotlin? = null
	@JvmField
	internal var manufacturing: Technology_kotlin? = null

	override fun compareTo(other: PlayerTechnology_kotlin): Int {
		return compareBy<PlayerTechnology_kotlin>({
			scanning!!.level
				.plus(propulsion!!.level)
				.plus(terraforming!!.level)
				.plus(research!!.level)
				.plus(weapons!!.level)
				.plus(banking!!.level)
				.plus(manufacturing!!.level)
		}).compare(this, other)
	}

	override fun toString(): String {
		return "PlayerTechnology(scanning=$scanning, propulsion=$propulsion, terraforming=$terraforming, research=$research, weapons=$weapons, banking=$banking, manufacturing=$manufacturing)"
	}
}
