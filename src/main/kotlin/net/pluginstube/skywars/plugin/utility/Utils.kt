package net.pluginstube.skywars.plugin.utility

import org.bukkit.ChatColor
import org.bukkit.Color


class Utils {
    companion object {
        fun translateChatColorToColor(chatColor: ChatColor?): Color? {
            when (chatColor) {
                ChatColor.AQUA -> return Color.AQUA
                ChatColor.BLACK -> return Color.BLACK
                ChatColor.BLUE -> return Color.BLUE
                ChatColor.DARK_AQUA -> return Color.BLUE
                ChatColor.DARK_BLUE -> return Color.BLUE
                ChatColor.DARK_GRAY -> return Color.GRAY
                ChatColor.DARK_GREEN -> return Color.GREEN
                ChatColor.DARK_PURPLE -> return Color.PURPLE
                ChatColor.DARK_RED -> return Color.RED
                ChatColor.GOLD -> return Color.YELLOW
                ChatColor.GRAY -> return Color.GRAY
                ChatColor.GREEN -> return Color.GREEN
                ChatColor.LIGHT_PURPLE -> return Color.PURPLE
                ChatColor.RED -> return Color.RED
                ChatColor.WHITE -> return Color.WHITE
                ChatColor.YELLOW -> return Color.YELLOW
                else -> {
                }
            }
            return null
        }
    }
}