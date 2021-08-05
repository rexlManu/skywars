package net.pluginstube.skywars.plugin.map

import org.bukkit.Location

class MapPosition(
    val x: Int,
    val y: Int,
    val z: Int,
    val yaw: Float,
    val pitch: Float
) {
    companion object {
        fun fromBukkitLocation(location: Location): MapPosition {
            return MapPosition(location.blockX, location.blockY, location.blockZ, location.yaw, location.pitch)
        }
    }
}