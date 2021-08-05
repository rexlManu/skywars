package net.pluginstube.skywars.plugin.config

import org.bukkit.plugin.java.JavaPlugin

class Settings(plugin: JavaPlugin) {

    var teamSize: Int = plugin.config.getInt("team-size", 1)
    var teams: Int = plugin.config.getInt("teams", 8)
    var minPlayers: Int = plugin.config.getInt("min-players", 2)

}