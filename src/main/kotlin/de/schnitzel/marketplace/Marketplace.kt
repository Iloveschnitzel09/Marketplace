package de.schnitzel.marketplace

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import de.schnitzel.marketplace.commands.AddTestCommand
import de.schnitzel.marketplace.commands.OpenComparisonCommand
import de.schnitzel.marketplace.database.DatabaseService
import org.bukkit.plugin.java.JavaPlugin

class Marketplace : SuspendingJavaPlugin() {
    override fun onLoad() {
        DatabaseService.establishConnection(plugin.dataPath)
        DatabaseService.createTables()
    }

    override fun onEnable() {
        registerCommands()

        logger.info("Marketplace Plugin erfolgreich aktiviert")
    }

    override fun onDisable() {
        DatabaseService.closeConnection()

        logger.info("Marketplace Plugin deaktiviert")
    }

    private fun registerCommands() {
        OpenComparisonCommand()
        AddTestCommand(plugin = this)
    }
}

val plugin get() = JavaPlugin.getPlugin(Marketplace::class.java)
