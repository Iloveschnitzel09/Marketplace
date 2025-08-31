package de.schnitzel.marketplace.gui


import com.github.stefvanschie.inventoryframework.gui.GuiItem
import com.github.stefvanschie.inventoryframework.gui.type.ChestGui
import com.github.stefvanschie.inventoryframework.pane.OutlinePane
import com.github.stefvanschie.inventoryframework.pane.Pane
import com.github.stefvanschie.inventoryframework.pane.StaticPane
import de.schnitzel.marketplace.util.ShopItem
import de.schnitzel.marketplace.util.getShopItems
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin


class ComparisonGui() : ChestGui(6, "Preisvergleich") {
    init {
        val pane = StaticPane(0, 1,9, 6, Pane.Priority.HIGH)
        val outlinePane = OutlinePane(0, 0, 9, 6, Pane.Priority.LOW)
        outlinePane.addItem(
            GuiItem(ItemStack(Material.GRAY_STAINED_GLASS_PANE))
        )

        this.setOnGlobalClick{ event: InventoryClickEvent ->
            event.isCancelled =
                true
        }


        val shopItems = getShopItems(plugin as JavaPlugin)
        var x = 0
        var y = 0

        for ((index, shopItem) in shopItems.withIndex()) {
            pane.addItem(GuiItem(makeItemStack(shopItem)), x, y)

            x++
            if (x >= 9) {
                x = 0
                y++
            }
        }


        outlinePane.setRepeat(true)

        this.addPane(outlinePane)
        this.addPane(pane)

    }

    fun makeItemStack(shopItem: ShopItem) : ItemStack {
        val itemStack = shopItem.item.clone()
        itemStack.amount = shopItem.amount

        val meta = itemStack.itemMeta
        meta?.let {
            val displayName = Component.text("> ", NamedTextColor.DARK_GRAY)
                .append(
                    Component.text(
                        itemStack.type.name.replace("_", " ").lowercase()
                            .replaceFirstChar { it.uppercase() },
                        TextColor.fromHexString("#3ecfd8")
                    )
                )
                .decoration(TextDecoration.ITALIC, false)


            it.displayName(displayName)

            val sellerName = Bukkit.getPlayer(shopItem.ownerUUID)?.name
                ?: Bukkit.getOfflinePlayer(shopItem.ownerUUID).name ?: "Unbekannt"

            val lore = listOf(
                loreLine("Verkäufer: ", sellerName,false),
                loreLine("Preis: ", shopItem.price.toString(), false),
                Component.empty(),
                loreLine("Linksklick", " - Zum Kaufen",true)
            )

            it.lore(lore)
            itemStack.itemMeta = it
        }
        return itemStack
    }

    fun loreLine(
        label: String,
        value: String? = null,
        change: Boolean
    ): Component {
        var comp : Component = Component.empty()
        val labelColor= NamedTextColor.GRAY
        val valueColor = TextColor.fromHexString("#f6c053")

        if (!change) {
            comp = Component.text("• ", NamedTextColor.DARK_GRAY)
                .append(Component.text(label, labelColor))

            if (value != null) {
                comp = comp.append(Component.text(value, valueColor))
            }
        } else {
             comp = Component.text("  ", NamedTextColor.DARK_GRAY)
                .append(Component.text(label, valueColor))

            if (value != null) {
                comp = comp.append(Component.text(value, labelColor))
            }
        }

        return comp.decoration(TextDecoration.ITALIC, false)
    }


}