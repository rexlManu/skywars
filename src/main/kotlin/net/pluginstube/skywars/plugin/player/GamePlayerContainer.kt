package net.pluginstube.skywars.plugin.player

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import java.util.*

/**
 * Used to store [GamePlayer] on join and destroy on quit
 */
class GamePlayerContainer : Listener {

    /**
     * All online players as [GamePlayer]
     */
    var players = mutableMapOf<UUID, GamePlayer>()

    /**
     * Get the player by its unique id
     */
    fun getPlayer(uniqueId: UUID): GamePlayer {
        return players.getOrPut(uniqueId) { GamePlayer(uniqueId) }
    }

    @EventHandler
    fun handle(event: PlayerJoinEvent) {
        players[event.player.uniqueId] = GamePlayer(event.player.uniqueId)
    }

    @EventHandler
    fun handle(event: PlayerQuitEvent) {
        players.remove(event.player.uniqueId)
    }
}