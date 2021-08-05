package net.pluginstube.skywars.plugin.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.CreatureSpawnEvent
import org.bukkit.event.weather.WeatherChangeEvent

class BasicEvents : Listener {

    @EventHandler
    fun handle(event: WeatherChangeEvent) = event.apply { isCancelled = true }

    @EventHandler
    fun handle(event: CreatureSpawnEvent) =
        event.apply { isCancelled = spawnReason != CreatureSpawnEvent.SpawnReason.CUSTOM }
}