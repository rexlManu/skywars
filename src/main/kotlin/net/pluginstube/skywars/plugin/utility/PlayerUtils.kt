package net.pluginstube.skywars.plugin.utility

import org.bukkit.Bukkit

import org.bukkit.GameMode

import org.bukkit.entity.Player
import org.bukkit.potion.PotionEffect
import org.bukkit.util.Vector
import java.util.function.Consumer


class PlayerUtils {
    companion object {
        fun resetPlayer(player: Player) {
            player.closeInventory()
            player.inventory.clear()
            player.activePotionEffects.forEach(Consumer { potionEffect: PotionEffect ->
                player.removePotionEffect(
                    potionEffect.type
                )
            })
            player.health = 20.0
            player.maxHealth = player.maxHealth
            player.foodLevel = 20
            player.gameMode = GameMode.ADVENTURE
            player.allowFlight = false
            player.velocity = Vector(0, 0, 0)
            player.level = 0
            player.exp = 0f
            player.walkSpeed = 0.2f
            player.scoreboard = Bukkit.getScoreboardManager().newScoreboard
            player.maximumNoDamageTicks = 20
        }
    }

}