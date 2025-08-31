package de.schnitzel.marketplace

import de.schnitzel.marketplace.commands.AddTestCommand
import de.schnitzel.marketplace.commands.OpenComparisonCommand
import de.schnitzel.marketplace.database.Database
import org.bukkit.plugin.java.JavaPlugin

class Marketplace : JavaPlugin() {

    private lateinit var database: Database

    override fun onEnable() {
        initializeDatabase()
        registerCommands()
        logger.info("Marketplace Plugin erfolgreich aktiviert")
    }

    override fun onDisable() {
        shutdownDatabase()
        logger.info("Marketplace Plugin deaktiviert")
    }

    private fun initializeDatabase() {
        database = Database(this)
        database.connect()
    }

    private fun registerCommands() {
        OpenComparisonCommand()
        AddTestCommand(plugin = this)
    }

    private fun shutdownDatabase() {
        if (::database.isInitialized) {
            database.disconnect()
        }
    }
}
