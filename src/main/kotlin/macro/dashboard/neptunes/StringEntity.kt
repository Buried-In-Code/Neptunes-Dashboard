package macro.dashboard.neptunes

import org.jetbrains.exposed.dao.Entity
import org.jetbrains.exposed.dao.EntityClass
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IdTable
import org.jetbrains.exposed.sql.Column

/**
 * Created by Macro303 on 2019-Nov-26
 */
open class StringIdTable(name: String = "", columnName: String = "id") : IdTable<String>(name) {
	override val id: Column<EntityID<String>> = text(columnName).primaryKey().entityId()
}

abstract class StringEntity(id: EntityID<String>) : Entity<String>(id)

abstract class StringEntityClass<out E : StringEntity>(table: IdTable<String>, entityType: Class<E>? = null) :
	EntityClass<String, E>(table, entityType)