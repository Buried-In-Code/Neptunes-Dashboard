package macro.dashboard.v2

import java.util.*

/**
 * Created by Macro303 on 2019-Nov-25
 */
interface ISchema {
	fun toJson(vararg filterKeys: String): SortedMap<String, Any?>
}