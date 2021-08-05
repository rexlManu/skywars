package net.pluginstube.skywars.plugin.map.game

import net.pluginstube.skywars.plugin.map.Map
import net.pluginstube.skywars.plugin.map.MapManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

class GameMapManager(mapManager: MapManager) : Listener {
    lateinit var currentMap: Map
    val maps = mapManager.maps.map { GameMap(it) }

    @EventHandler
    fun handle(event: PlayerQuitEvent) {
        removeVotes(event.player.uniqueId)
    }

    fun removeVotes(uuid: UUID) {
        maps.forEach {
            it.voters.remove(uuid)
        }
    }

    fun getVotedMap(uuid: UUID): GameMap {
        return maps.first { it.voters.contains(uuid) }
    }
}