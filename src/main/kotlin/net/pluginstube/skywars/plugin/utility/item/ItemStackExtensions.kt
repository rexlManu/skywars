package net.pluginstube.skywars.plugin.utility

import net.pluginstube.skywars.plugin.utility.item.itemMeta
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

internal val NEW_LINE_SPLIT = "\n".toRegex()

/**
 * Creates a new [ItemStack] based on the given type and applies the given body to it.
 */
inline fun item(type: Material, body: ItemStack.() -> Unit) =
    ItemStack(type).apply(body)

/**
 * Creates a new [ItemStack] based on another ItemStack and applies the given body to it.
 *
 * @param interactive Whether this item should be an instance
 */
inline fun item(copy: ItemStack, body: ItemStack.() -> Unit) =
    ItemStack(copy).apply(body)

/**
 * Assigns and accesses this ItemStack's [ItemMeta].
 *
 * @see itemMeta
 */
inline fun <reified T : ItemMeta> ItemStack.meta(body: T.() -> Unit) {
    val newMeta = itemMeta(type, body)
    itemMeta = newMeta
}

/**
 * Creates an [EnchantmentNode] for this ItemStack, applies the given
 * body and adds all enchantments configured to this ItemStack.
 */
inline fun ItemStack.enchant(unsafe: Boolean = false, body: EnchantmentNode.() -> Unit) {
    val addMethod = if (unsafe) ::addUnsafeEnchantment else ::addEnchantment
    EnchantmentNode().apply(body).let {
        it.set.forEach { container ->
            val (enchantment, level) = container
            addMethod(enchantment, level)
        }
    }
}