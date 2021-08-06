package net.pluginstube.skywars.plugin.gamestate.states

import eu.miopowered.packetlistener.entity.PacketPlayer
import net.pluginstube.skywars.plugin.PREFIX
import net.pluginstube.skywars.plugin.SkyWarsPlugin
import net.pluginstube.skywars.plugin.countdown.Countdown
import net.pluginstube.skywars.plugin.gamestate.State
import net.pluginstube.skywars.plugin.utility.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Sound
import org.bukkit.WorldCreator
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.event.player.PlayerMoveEvent

class PlayingState(private val plugin: SkyWarsPlugin) : State {

    var startCountdown = Countdown(30).apply {
        everyTick = { tick ->
            Bukkit.getOnlinePlayers().forEach {
                PacketPlayer.of(it).sendFullTitle("&p$tick".withParameters(), PREFIX, 14, 1, 5)
                it.playSound(Sound.CHICKEN_EGG_POP, 2f)
            }
        }
        finish = {
            Bukkit.getOnlinePlayers().forEach {
                it.message("Das Spiel fängt nun an.")
            }
            gracePeriodCountdown.start(plugin)
        }
    }

    var gracePeriodCountdown = Countdown(30).apply {
        everyTick = {

        }
        finish = {
            Bukkit.getOnlinePlayers().forEach {
                it.message("Die Schutzzeit ist nun vorbei.")
            }
        }
    }

    override fun start() {
        plugin.gameMapManager.currentMap = plugin.gameMapManager.maps.maxByOrNull { it.voters.size }!!.map
        val currentMap = plugin.gameMapManager.currentMap
        Bukkit.createWorld(WorldCreator(currentMap.worldName)).stopModification()

        Bukkit.getOnlinePlayers().forEach {
            PlayerUtils.resetPlayer(it)
            val team = plugin.teamManager.getTeam(it.uniqueId)!!
            it.teleport(currentMap.location("team-spawn-${team.teamName}"))
        }

        startCountdown.start(plugin)
    }

    override fun end() {
        TODO("Not yet implemented")
    }

    @EventHandler
    fun handle(event: PlayerMoveEvent) {
        if (!startCountdown.running) return
        val to: Location = event.to
        val from: Location = event.from
        if (to.x != from.x || to.z != from.z) {
            to.x = from.x
            to.z = from.z
            return
        }
    }

    @EventHandler
    fun handle(event: BlockBreakEvent) {
        if (startCountdown.running) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun handle(event: BlockPlaceEvent) {
        if (startCountdown.running) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun handle(event: PlayerInteractEvent) {
        if (startCountdown.running) {
            event.isCancelled = true
        }
    }

    @EventHandler
    fun handle(event: EntityDamageEvent) {
        if (event.entity is Player && gracePeriodCountdown.running) {
            event.isCancelled = true
        }
    }
}