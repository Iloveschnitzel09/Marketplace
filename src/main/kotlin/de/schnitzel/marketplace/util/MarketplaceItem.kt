package de.schnitzel.marketplace.util

import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.io.BukkitObjectInputStream
import java.io.ByteArrayInputStream
import java.io.File
import java.sql.DriverManager
import java.sql.SQLException
import java.util.Base64
import java.util.UUID

data class ShopItem(
    val ownerUUID: UUID,
    val item: ItemStack,
    val price: Double,
    val amount: Int
)

fun getShopItems(plugin: JavaPlugin): List<ShopItem> {
    val shopItems = mutableListOf<ShopItem>()

    try {
        val dbFile = getDatabaseFile(plugin)
        val url = "jdbc:sqlite:${dbFile.absolutePath}"

        DriverManager.getConnection(url).use { connection ->
            connection.prepareStatement("SELECT * FROM shop_items;").use { ps ->
                val rs = ps.executeQuery()
                while (rs.next()) {
                    val shopItem = createShopItemFromResultSet(rs)
                    shopItems.add(shopItem)
                }
            }
        }
    } catch (e: SQLException) {
        plugin.logger.severe("Fehler beim Laden der Shop-Items: ${e.message}")
        e.printStackTrace()
    }

    return shopItems
}

private fun createShopItemFromResultSet(rs: java.sql.ResultSet): ShopItem {
    val ownerUUID = UUID.fromString(rs.getString("owner_uuid"))
    val price = rs.getDouble("price")
    val amount = rs.getInt("amount")
    val itemString = rs.getString("itemstack")
    val item = stringToItemStack(itemString)

    return ShopItem(ownerUUID, item, price, amount)
}

private fun getDatabaseFile(plugin: JavaPlugin): File {
    val folder = plugin.dataFolder
    if (!folder.exists()) {
        folder.mkdirs()
    }
    return File(folder, "database.db")
}

private fun stringToItemStack(data: String): ItemStack {
    return try {
        val bytes = Base64.getDecoder().decode(data)
        val bais = ByteArrayInputStream(bytes)
        BukkitObjectInputStream(bais).use { ois ->
            ois.readObject() as ItemStack
        }
    } catch (e: Exception) {
        throw IllegalArgumentException("Fehler beim Deserialisieren des ItemStacks: ${e.message}")
    }
}

