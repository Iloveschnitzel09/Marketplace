package de.schnitzel.marketplace.commands

import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.util.io.BukkitObjectOutputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.sql.DriverManager
import java.sql.SQLException
import java.util.Base64

fun AddTestCommand(plugin: JavaPlugin) = commandAPICommand("test") {
    withPermission("schnitzel.marketplace.command.test")
    playerExecutor { player, _ ->
        insertTestItem(plugin, player.uniqueId.toString())
    }
}

private fun insertTestItem(plugin: JavaPlugin, playerUUID: String) {
    val dbFile = getDatabaseFile(plugin)
    val url = "jdbc:sqlite:${dbFile.absolutePath}"

    try {
        DriverManager.getConnection(url).use { connection ->
            connection.prepareStatement(
                "INSERT INTO shop_items (owner_uuid, itemstack, price, amount) VALUES (?, ?, ?, ?);"
            ).use { ps ->
                ps.setString(1, playerUUID)
                ps.setString(2, itemStackToString(ItemStack(Material.OAK_TRAPDOOR)))
                ps.setDouble(3, 1000.0)
                ps.setInt(4, 10)
                ps.executeUpdate()
            }
        }
    } catch (e: SQLException) {
        e.printStackTrace()
    }
}

private fun getDatabaseFile(plugin: JavaPlugin): File {
    val folder = plugin.dataFolder
    if (!folder.exists()) {
        folder.mkdirs()
    }
    return File(folder, "database.db")
}

private fun itemStackToString(item: ItemStack): String {
    val baos = ByteArrayOutputStream()
    BukkitObjectOutputStream(baos).use { oos ->
        oos.writeObject(item)
    }
    return Base64.getEncoder().encodeToString(baos.toByteArray())
}