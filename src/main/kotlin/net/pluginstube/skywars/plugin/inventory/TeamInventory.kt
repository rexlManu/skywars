package net.pluginstube.skywars.plugin.inventory

import net.pluginstube.skywars.plugin.SkyWarsPlugin
import net.pluginstube.skywars.plugin.utility.*
import net.pluginstube.skywars.plugin.utility.item.name
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

class TeamInventory(var plugin: SkyWarsPlugin, player: Player, var update: (() -> Unit)? = null) :
    InventoryHandler(player, "Teamauswahl", 5) {

    var teamSlots = mutableMapOf<Int, Int>()

    override fun setItems() {
        teamSlots.clear()
        plugin.teamManager.teams.forEachIndexed { index, team ->
            val slot = addItem(item(Material.STAINED_CLAY) {
                meta<ItemMeta> {
                    displayName = "&p${team.teamName.name}"
                    durability = team.teamName.dyeColor.woolData.toShort()
                    lore = team.members.map { "&t${Bukkit.getPlayer(it).name}".withParameters() }
                }
            })
            teamSlots[slot] = index
        }
    }

    override fun onClick(itemStack: ItemStack, event: InventoryClickEvent) {
        var teamIndex: Int = teamSlots[event.slot] ?: return
        var team = plugin.teamManager.teams[teamIndex]

        if (team == plugin.teamManager.getTeam(player.uniqueId)) return
        if (plugin.settings.teamSize == team.members.size) {
            player.message("Das Team &p${team.teamName.name}&t ist bereits voll.")
            return
        }
        plugin.teamManager.removeFromTeams(player.uniqueId)
        team.members.add(player.uniqueId)

        player.message("Du hast das Team &p${team.teamName.name}&t betreten.")
        player.playSound(Sound.LEVEL_UP, 1f)

        update?.invoke()
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