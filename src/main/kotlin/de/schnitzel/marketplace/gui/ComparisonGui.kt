package de.schnitzel.marketplace.gui


import com.github.shynixn.mccoroutine.folia.launch
import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.Pane
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import de.schnitzel.marketplace.database.DatabaseService
import de.schnitzel.marketplace.util.MarketItem
import dev.slne.surf.surfapi.bukkit.api.builder.buildItem
import dev.slne.surf.surfapi.bukkit.api.builder.buildLore
import dev.slne.surf.surfapi.bukkit.api.builder.displayName
import dev.slne.surf.surfapi.core.api.messages.CommonComponents
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

class ComparisonGui() : ChestGui(6, "Preisvergleich") {
    init {
        plugin.launch {
            val pane = StaticPane(0, 1, 9, 6, Pane.Priority.HIGH)
            val outlinePane = OutlinePane(0, 0, 9, 6, Pane.Priority.LOW)
            outlinePane.addItem(
                GuiItem(ItemStack(Material.GRAY_STAINED_GLASS_PANE))
            )

            setOnGlobalClick { event: InventoryClickEvent ->
                event.isCancelled =
                    true
            }

            var x = 0
            var y = 0

            val marketItem = DatabaseService().loadMarketItems() // hier kannst du owner übergeben, falls nötig

            for ((index, marketItem) in marketItem.withIndex()) {
                pane.addItem(GuiItem(createItemStack(marketItem)), x, y)

                x++
                if (x >= 9) {
                    x = 0
                    y++
                }
            }

            outlinePane.setRepeat(true)

            addPane(outlinePane)
            addPane(pane)
        }
    }

    fun createItemStack(marketItem: MarketItem) =
        buildItem(marketItem.item.type, marketItem.item.amount) {
            displayName {
                darkSpacer(">")
                appendSpace()
                primary(
                    PlainTextComponentSerializer.plainText().serialize(marketItem.item.displayName()),
                    TextDecoration.ITALIC
                )
            }

            val sellerName = Bukkit.getOfflinePlayer(marketItem.ownerUuid).name ?: "Unbekannt"

            buildLore {
                line {
                    append(CommonComponents.EM_DASH)
                    appendSpace()
                    variableKey("Verkäufer")
                    spacer(":")
                    appendSpace()
                    variableValue(sellerName)
                }

                line {
                    append(CommonComponents.EM_DASH)
                    appendSpace()
                    variableKey("Preis")
                    spacer(":")
                    appendSpace()
                    variableValue(marketItem.price)
                }

                line {
                    spacer("Drücke Linksklick, um dieses Item zu kaufen")
                }
            }
        }

}