package de.schnitzel.marketplace.database.tables

import de.schnitzel.marketplace.util.deserializeItemStack
import de.schnitzel.marketplace.util.serializeToBytes
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.statements.api.ExposedBlob

object MarketItemEntryTable : Table("marketplace_entries") {
    val id = integer("id").autoIncrement()
    val ownerUuid = uuid("owner")
    val price = double("price")
    val item = blob("item").transform({ it.bytes.deserializeItemStack() }, { ExposedBlob(it.serializeToBytes()) })

    override val primaryKey = PrimaryKey(id)
}