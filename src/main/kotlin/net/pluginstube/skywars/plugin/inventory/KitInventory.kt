package net.pluginstube.skywars.plugin.inventory

import net.pluginstube.skywars.plugin.SkyWarsPlugin
import net.pluginstube.skywars.plugin.inventory.event.PlayerKitSelectEvent
import net.pluginstube.skywars.plugin.utility.*
import net.pluginstube.skywars.plugin.utility.item.name
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class KitInventory(val plugin: SkyWarsPlugin, player: Player) : InventoryHandler(player, "Kitauswahl", 5) {

    var kitSlots = mutableMapOf<Int, Int>()

    override fun onClick(itemStack: ItemStack, event: InventoryClickEvent) {
        var kitIndex: Int = kitSlots[event.slot] ?: return
        var kit = plugin.kitManager.kits[kitIndex]

        var gamePlayer = plugin.gamePlayerContainer.getPlayer(player.uniqueId)
        if (kit == gamePlayer.kit) return

        gamePlayer.kit = kit

        player.message("Du hast das Kit &p${kit.name}&t ausgewählt.")
        player.playSound(Sound.LEVEL_UP, 1f)

        Bukkit.getPluginManager().callEvent(PlayerKitSelectEvent(player))
    }

    override fun setItems() {
        kitSlots.clear()
        plugin.kitManager.kits.forEachIndexed { index, kit ->
            val slot = addItem(item(kit.displayItem.material) {
                meta<ItemMeta> {
                    displayName = "&p${kit.name}"
                    durability = kit.displayItem.data
                    lore = kit.description.withParameters().split("%nl")
                }
            })
            kitSlots[slot] = index
        }
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