package de.schnitzel.marketplace.database

import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.sql.Statement

class Database(private val plugin: JavaPlugin) {

    private var connection: Connection? = null

    fun connect() {
        try {
            val dbFile = getDatabaseFile()
            val url = "jdbc:sqlite:${dbFile.absolutePath}"

            connection = DriverManager.getConnection(url)
            plugin.logger.info("SQLite verbunden: ${dbFile.absolutePath}")

            createTables()
        } catch (e: SQLException) {
            plugin.logger.severe("Fehler beim Verbinden zur Datenbank: ${e.message}")
            e.printStackTrace()
        }
    }

    private fun getDatabaseFile(): File {
        val folder = plugin.dataFolder
        if (!folder.exists()) {
            folder.mkdirs()
        }
        return File(folder, "database.db")
    }

    private fun createTables() {
        connection?.createStatement()?.use { stmt: Statement ->
            stmt.executeUpdate(
                """
                CREATE TABLE IF NOT EXISTS shop_items (
                    owner_uuid TEXT NOT NULL,
                    itemstack BLOB NOT NULL,
                    price REAL NOT NULL,
                    amount INTEGER NOT NULL
                );
                """.trimIndent()
            )
        }
    }

    fun getConnection(): Connection {
        return connection ?: throw IllegalStateException("Datenbank ist nicht verbunden")
    }

    fun disconnect() {
        try {
            connection?.close()
            connection = null
            plugin.logger.info("Datenbankverbindung geschlossen")
        } catch (e: SQLException) {
            plugin.logger.warning("Fehler beim Schließen der Datenbankverbindung: ${e.message}")
        }
    }
}
