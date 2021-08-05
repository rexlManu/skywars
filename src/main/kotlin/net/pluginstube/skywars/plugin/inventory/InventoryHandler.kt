package net.pluginstube.skywars.plugin.inventory

import net.pluginstube.skywars.plugin.utility.unregister
import net.pluginstube.skywars.plugin.utility.withColors
import net.pluginstube.skywars.plugin.utility.withParameters
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.ItemStack


abstract class InventoryHandler(val player: Player, title: String, rows: Int) : Listener {

    val inventory = Bukkit.createInventory(null, rows * 9, "&td● &p$title".withParameters().withColors())

    val patternItems = this.patternItems()
    val pattern = this.pattern()

    init {
        for (x in pattern.indices) {
            for (y in 0 until pattern[x].size) {
                val code: Char = pattern[x][y]
                if (code == 'x') continue
                inventory.setItem(x * 9 + y, patternItems[code])
            }
        }

        setItems()
    }

    fun open() {
        player.openInventory(inventory)
    }

    abstract fun onClick(itemStack: ItemStack, event: InventoryClickEvent)

    abstract fun setItems()

    abstract fun pattern(): Array<CharArray>
    abstract fun patternItems(): Map<Char, ItemStack>

    @EventHandler
    fun handle(event: InventoryClickEvent) {
        if (event.clickedInventory != inventory) return

        event.isCancelled = true
        if (event.currentItem != null)
            onClick(event.currentItem, event)
    }

    @EventHandler
    fun handle(event: InventoryCloseEvent) {
        if (event.inventory == inventory)
            this.unregister()
    }

    fun addItem(itemStack: ItemStack): Int {
        (1..inventory.size).forEach {
            val item = inventory.getItem(it)
            if (item == null || item.type == Material.AIR) {
                inventory.setItem(it, itemStack)
                return it
            }
        }
        return -1
    }


}