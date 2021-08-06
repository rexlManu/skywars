package net.pluginstube.skywars.plugin.countdown

import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

class Countdown(val timeCount: Int) {
    var everyTick: ((tick: Int) -> Unit)? = null
    var finish: (() -> Unit)? = null
    lateinit var task: BukkitTask
    var tick = timeCount
    var running = true

    init {
        reset()
    }

    fun start(plugin: JavaPlugin) {
        task = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, {
            if (tick <= 0) {
                stop()
                return@runTaskTimerAsynchronously
            }
            everyTick?.invoke(tick)
            tick--
        }, 0, 20)
    }

    fun stop() {
        running = false
        task.cancel()
        finish?.invoke()
    }

    fun reset() {
        tick = timeCount
    }
}