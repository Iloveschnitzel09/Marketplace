package de.schnitzel.marketplace.util

import org.bukkit.inventory.ItemStack
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

/**
 * Credits to ChatGPT 5
 */

fun ItemStack.serializeToBytes(): ByteArray {
    val map = this.serialize()
    ByteArrayOutputStream().use { byteOut ->
        ObjectOutputStream(byteOut).use { out ->
            out.writeObject(map)
        }
        return byteOut.toByteArray()
    }
}

fun ByteArray.deserializeItemStack(): ItemStack =
    runCatching {
        ByteArrayInputStream(this).use { byteIn ->
            ObjectInputStream(byteIn).use { input ->
                @Suppress("UNCHECKED_CAST")
                val map = input.readObject() as Map<String, Any>
                ItemStack.deserialize(map)
            }
        }
    }.getOrNull() ?: error("There was an error deserializing the ItemStack: ${String(this)}")