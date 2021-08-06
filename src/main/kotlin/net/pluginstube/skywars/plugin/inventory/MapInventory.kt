package net.pluginstube.skywars.plugin.inventory

import net.pluginstube.skywars.plugin.SkyWarsPlugin
import net.pluginstube.skywars.plugin.inventory.event.PlayerMapSelectEvent
import net.pluginstube.skywars.plugin.utility.*
import net.pluginstube.skywars.plugin.utility.item.name
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class MapInventory(var plugin: SkyWarsPlugin, player: Player) :
    InventoryHandler(player, "Mapvoting", 5) {

    var mapSlots = mutableMapOf<Int, Int>()

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

    override fun onClick(itemStack: ItemStack, event: InventoryClickEvent) {
        var mapIndex: Int = mapSlots[event.slot] ?: return
        var gameMap = plugin.gameMapManager.maps[mapIndex]

        if (gameMap == plugin.gameMapManager.getVotedMap(player.uniqueId)) return

        plugin.gameMapManager.removeVotes(player.uniqueId)
        gameMap.voters.add(player.uniqueId)

        player.message("Du hast für die Map &p${gameMap.map.name}&t abgestimmt.")
        player.playSound(Sound.LEVEL_UP, 1f)

        Bukkit.getPluginManager().callEvent(PlayerMapSelectEvent(player))
    }

    override fun setItems() {
        mapSlots.clear()
        plugin.gameMapManager.maps.forEachIndexed { index, gameMap ->
            val map = gameMap.map
            val slot = addItem(item(map.displayItem.material) {
                meta<ItemMeta> {
                    displayName = "&p${map.name}".withParameters()
                    lore = listOf("&t${map.description}".withParameters())
                    durability = map.displayItem.data
                    amount = gameMap.voters.size
                }
            })
            mapSlots[slot] = index
        }
    }

    @EventHandler
    fun handle(event: PlayerMapSelectEvent) {
        setItems()
    }
}
