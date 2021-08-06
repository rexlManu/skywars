package net.pluginstube.skywars.plugin.team

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import net.pluginstube.skywars.plugin.utility.adapters.ChatColorTypeAdapter
import org.bukkit.ChatColor
import java.io.File
import java.util.*

/**
 * Is loading the team names from config and creates teams
 */
class TeamManager(teamFile: File) {
    val gson: Gson = GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .registerTypeAdapter(ChatColor::class.java, ChatColorTypeAdapter())
        .create()

    val jsonParser = JsonParser()

    val teamNames = jsonParser.parse(teamFile.readText()).asJsonArray.map {
        gson.fromJson(it, TeamName::class.java)
    }

    var teams = teamNames.map { Team(it) }

    /**
     * Find team by member unique id
     * @return null if the member hasn't choose a team
     */
    fun getTeam(uniqueId: UUID): Team? {
        return teams.firstOrNull { it.members.contains(uniqueId) || it.diedMembers.contains(uniqueId) }
    }

    /**
     * Removes member from all teams
     */
    fun removeFromTeams(uniqueId: UUID) {
        teams.forEach { it.members.remove(uniqueId) }
    }

    /**
     * Find a team with the least members
     */
    fun findTeam(): Team {
        return teams.minByOrNull { it.members.size }!!
    }

}