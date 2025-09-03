package de.schnitzel.marketplace

import com.github.shynixn.mccoroutine.folia.SuspendingJavaPlugin
import de.schnitzel.marketplace.commands.AddTestCommand
import de.schnitzel.marketplace.commands.OpenComparisonCommand
import de.schnitzel.marketplace.database.databaseService
import org.bukkit.plugin.java.JavaPlugin

class Marketplace : SuspendingJavaPlugin() {

    override fun onEnable() {
        registerCommands()
        databaseService.connect()

        logger.info("Marketplace Plugin erfolgreich aktiviert")
    }

    override fun onDisable() {
        databaseService.disconnect()

        logger.info("Marketplace Plugin deaktiviert")
    }

    private fun registerCommands() {
        OpenComparisonCommand()
        AddTestCommand(plugin = this)
    }
}

val plugin get() = JavaPlugin.getPlugin(Marketplace::class.java)
