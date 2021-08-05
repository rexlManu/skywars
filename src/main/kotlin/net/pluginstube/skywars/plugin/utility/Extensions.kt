package net.pluginstube.skywars.plugin.utility

import com.cryptomorin.xseries.messages.ActionBar
import net.pluginstube.skywars.plugin.*
import org.bukkit.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

fun Listener.register(plugin: JavaPlugin) = Bukkit.getPluginManager().registerEvents(this, plugin)

fun Listener.unregister() = HandlerList.unregisterAll(this)

fun String.withColors(): String = ChatColor.translateAlternateColorCodes('&', this)

fun String.withParameter(parameter: String, value: String): String {
    return this.replace(parameter, value, false)
}

fun String.withPrefix(): String = PREFIX + this

fun String.withParameters(): String = this.withParameter("&p", PRIMARY_COLOR)
    .withParameter("&s", SECONDARY_COLOR)
    .withParameter("&td", TEXT_DARKER_COLOR)
    .withParameter("&t", TEXT_COLOR)

fun String.asFormatted(): String = this.withPrefix()
    .withParameters()
    .withColors()

fun CommandSender.message(text: String) = this.sendMessage(text.asFormatted())

fun Player.playSound(sound: Sound, pitch: Float) {
    this.playSound(eyeLocation, sound, 1f, pitch)
}

fun Player.actionbar(text: String) {
    ActionBar.sendActionBar(this, text)
}

fun broadcast(text: String) = Bukkit.broadcastMessage(text)

private fun String.withParameter(parameter: String, value: ChatColor): String {
    return this.withParameter(parameter, value.toString())
}

fun World.stopModification(): World {
    return apply {
        setGameRuleValue("mobGriefing", "false")
        setGameRuleValue("doMobSpawning", "false")
        setGameRuleValue("doFireTick", "false")
        setGameRuleValue("doDaylightCycle", "false")
        setGameRuleValue("randomTickSpeed", "0")
        fullTime = 2000
        isAutoSave = false
    }
}

fun Location.toCenterLocation(): Location {
    return clone().add(
        if (blockX > 1) 0.5 else -0.5,
        if (blockY > 1) 0.5 else -0.5,
        if (blockZ > 1) 0.5 else -0.5
    )
}
