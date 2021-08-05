package net.pluginstube.skywars.plugin.team

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser
import net.pluginstube.skywars.plugin.team.adapters.ChatColorTypeAdapter
import org.bukkit.ChatColor
import java.io.File
import java.util.*

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
    fun getTeam(uniqueId: UUID): Team {
        return teams.first { it.members.contains(uniqueId) || it.diedMembers.contains(uniqueId) }
    }

    fun removeFromTeams(uniqueId: UUID) {
        teams.forEach { it.members.remove(uniqueId) }
    }

}