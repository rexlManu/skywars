package net.pluginstube.skywars.plugin.team

import java.util.*

/**
 * The team class for storing its members and other things
 */
class Team(var teamName: TeamName) {
    var members = mutableListOf<UUID>()
    var diedMembers = mutableListOf<UUID>()

}