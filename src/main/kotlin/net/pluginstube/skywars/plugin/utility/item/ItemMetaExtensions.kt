package net.pluginstube.skywars.plugin.utility.item

import net.pluginstube.skywars.plugin.utility.EnchantmentNode
import net.pluginstube.skywars.plugin.utility.NEW_LINE_SPLIT
import net.pluginstube.skywars.plugin.utility.withColors
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.meta.ItemMeta

/**
 * Creates a new ItemMeta based on a [Material] and applies the given body to it.
 *
 * @param T The ItemMeta type to be created.
 */
inline fun <reified T : ItemMeta> itemMeta(material: Material, body: T.() -> Unit) =
    Bukkit.getItemFactory().getItemMeta(material)
        .let { it as? T }
        ?.apply(body)
        ?: throw IllegalArgumentException("ItemMeta for provided material does not match actual type parameter")

/**
 * Returns the lore joined to a String with new lines, sets the lore by splitting the given String at \n.
 */
var ItemMeta.stringLore: String?
    get() = lore?.joinToString("\n")
    set(value) {
        lore = value?.split(NEW_LINE_SPLIT)
    }

/**
 * @see ItemMeta.getDisplayName
 * @see ItemMeta.setDisplayName
 */
var ItemMeta.name: String?
    get() = if (hasDisplayName()) displayName else null
    set(value) {
        if (value != null) {
            displayName = value.withColors()
        }
    }

/** Adds the given [ItemFlag]s to this ItemMeta. */
fun ItemMeta.flags(vararg flags: ItemFlag) = addItemFlags(*flags)

/** Adds the given [ItemFlag] to this ItemMeta. */
fun ItemMeta.flag(flag: ItemFlag) = addItemFlags(flag)

/**
 * Creates an [EnchantmentNode] for this ItemMeta, applies the given
 * body and adds all enchantments configured to this ItemMeta.
 */
inline fun ItemMeta.enchant(ignoringRestrictions: Boolean = false, body: EnchantmentNode.() -> Unit) {
    EnchantmentNode().apply(body).set.forEach {
        addEnchant(it.enchantment, it.level, ignoringRestrictions)
    }

}