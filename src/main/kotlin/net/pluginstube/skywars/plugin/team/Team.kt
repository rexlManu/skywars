package net.pluginstube.skywars.plugin.team

import org.bukkit.ChatColor
import java.util.*

class Team(var teamName: TeamName) {
    var members = mutableListOf<UUID>()
    var diedMembers = mutableListOf<UUID>()

}