package de.schnitzel.marketplace.commands

import de.schnitzel.marketplace.gui.ComparisonGui
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor

fun OpenComparisonCommand() = commandAPICommand("vergleich") {
    withPermission("schnitzel.marketplace.command.vergleich")
    playerExecutor { player, _ ->

        ComparisonGui().show(player)
    }
}