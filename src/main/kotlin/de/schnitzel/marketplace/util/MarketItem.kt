package de.schnitzel.marketplace.util

import org.bukkit.inventory.ItemStack
import java.util.*

data class MarketItem(
    val id: Int,
    val ownerUuid: UUID,
    val price: Double,
    val item: ItemStack
)
