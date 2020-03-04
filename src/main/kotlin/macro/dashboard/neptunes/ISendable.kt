package macro.dashboard.neptunes

/**
 * Created by Macro303 on 2019-Nov-25
 */
interface ISendable {
	fun toJson(full: Boolean = true): Map<String, Any?>
}