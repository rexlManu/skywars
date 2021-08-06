package net.pluginstube.skywars.plugin.map

import net.pluginstube.skywars.plugin.utility.item.DisplayItem
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import kotlin.collections.Map

class Map(
    val name: String,
    var description: String,
    var worldName: String,
    var positions: Map<String, MapPosition?>,
    var displayItem: DisplayItem
) {

    companion object {
        fun create(name: String): net.pluginstube.skywars.plugin.map.Map {
            return Map(name, "", "", HashMap(), DisplayItem(Material.STONE, 1))
        }
    }

    fun displayItemStack() = displayItem.toItemStack(name)

    fun location(key: String): Location? {
        val position = positions[key] ?: return null
        return Location(
            Bukkit.getWorld(worldName),
            position.x.toDouble(),
            position.y.toDouble(),
            position.z.toDouble(),
            position.yaw,
            position.pitch
        )
    }
}