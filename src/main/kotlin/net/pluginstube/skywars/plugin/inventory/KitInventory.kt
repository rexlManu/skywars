package net.pluginstube.skywars.plugin.inventory

import net.pluginstube.skywars.plugin.utility.item
import net.pluginstube.skywars.plugin.utility.item.name
import net.pluginstube.skywars.plugin.utility.meta
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class KitInventory(player: Player) : InventoryHandler(player, "Kitauswahl", 5) {
    override fun onClick(itemStack: ItemStack) {
    }

    override fun pattern(): Array<CharArray> = arrayOf(
        charArrayOf('t', 'b', 't', 't', 'b', 't', 't', 'b', 't'),
        charArrayOf('t', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't'),
        charArrayOf('b', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 'b'),
        charArrayOf('t', 'x', 'x', 'x', 'x', 'x', 'x', 'x', 't'),
        charArrayOf('t', 'b', 't', 't', 'b', 't', 't', 'b', 't'),
    )

    override fun patternItems(): Map<Char, ItemStack> = mapOf(
        Pair('t', item(Material.STAINED_GLASS_PANE) {
            durability = 3
            meta<ItemMeta> {
                name = "&r"
            }
        }),
        Pair('b', item(Material.STAINED_GLASS_PANE) {
            durability = 11
            meta<ItemMeta> {
                name = "&r"
            }
        })

    )
}