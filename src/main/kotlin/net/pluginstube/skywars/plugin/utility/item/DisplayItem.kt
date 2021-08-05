package net.pluginstube.skywars.plugin.utility.item

import net.pluginstube.skywars.plugin.utility.item
import net.pluginstube.skywars.plugin.utility.meta
import org.bukkit.Material
import org.bukkit.inventory.meta.ItemMeta

class DisplayItem(var material: Material, var data: Short) {
    fun toItemStack(name: String) = item(material) {
        durability = this@DisplayItem.data
        meta<ItemMeta> {
            this.name = name
        }
    }
}