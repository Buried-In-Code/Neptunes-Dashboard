package macro.dashboard.neptunes

/**
 * Created by Macro303 on 2019-Nov-26
 */
interface IEntry {
	fun insert(): IEntry
	fun update(): IEntry
	fun delete()
}