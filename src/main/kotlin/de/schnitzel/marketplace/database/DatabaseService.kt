package de.schnitzel.marketplace.database

import de.schnitzel.marketplace.database.tables.MarketItemEntryTable
import de.schnitzel.marketplace.plugin
import de.schnitzel.marketplace.util.MarketItem
import dev.slne.surf.database.DatabaseManager
import dev.slne.surf.database.database.DatabaseProvider
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class DatabaseService {
    private lateinit var databaseProvider: DatabaseProvider

    fun connect() {
        databaseProvider = DatabaseManager(plugin.dataPath, plugin.dataPath).databaseProvider
        databaseProvider.connect()

        createTables()
    }

    fun disconnect() {
        databaseProvider.disconnect()
    }

    fun createTables() {
        transaction {
            SchemaUtils.create(MarketItemEntryTable)
        }
    }

    suspend fun loadMarketItems(owner: UUID? = null) = newSuspendedTransaction(Dispatchers.IO) {
        if (owner != null) {
            return@newSuspendedTransaction MarketItemEntryTable.selectAll()
                .where(MarketItemEntryTable.ownerUuid eq owner).map {
                MarketItem(
                    ownerUuid = owner,
                    item = it[MarketItemEntryTable.item],
                    price = it[MarketItemEntryTable.price],
                    id = it [MarketItemEntryTable.id]
                )
            }.toObjectSet()
        }

        MarketItemEntryTable.selectAll().map {
            MarketItem(
                ownerUuid = it[MarketItemEntryTable.ownerUuid],
                item = it[MarketItemEntryTable.item],
                price = it[MarketItemEntryTable.price],
                id = it [MarketItemEntryTable.id]
            )
        }.toObjectSet()
    }

    suspend fun saveMarketItem(marketItem: MarketItem) = newSuspendedTransaction(Dispatchers.IO) {
        MarketItemEntryTable.insert {
            it[ownerUuid] = marketItem.ownerUuid
            it[item] = marketItem.item
            it[price] = marketItem.price
        }
    }

    suspend fun saveMarketItems(items: ObjectSet<MarketItem>) = items.forEach { saveMarketItem(it) }

    companion object {
        val INSTANCE = DatabaseService()
    }
}

val databaseService get() = DatabaseService.INSTANCE
