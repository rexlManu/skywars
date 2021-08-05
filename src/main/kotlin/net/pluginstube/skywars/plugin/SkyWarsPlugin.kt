package net.pluginstube.skywars.plugin

import net.pluginstube.skywars.plugin.command.MainCommand
import net.pluginstube.skywars.plugin.config.LocationHandler
import net.pluginstube.skywars.plugin.config.Settings
import net.pluginstube.skywars.plugin.gamestate.GameStateManager
import net.pluginstube.skywars.plugin.map.MapManager
import net.pluginstube.skywars.plugin.map.game.GameMapManager
import net.pluginstube.skywars.plugin.team.TeamManager
import net.pluginstube.skywars.plugin.utility.stopModification
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

class SkyWarsPlugin : JavaPlugin() {

    lateinit var locationHandler: LocationHandler
    lateinit var settings: Settings
    lateinit var gameStateManager: GameStateManager
    lateinit var mapManager: MapManager
    lateinit var gameMapManager: GameMapManager
    lateinit var teamManager: TeamManager

    override fun onEnable() {
        dataFolder.mkdir()
        config.options().copyDefaults(true)
        saveDefaultConfig()
        reloadConfig()

        locationHandler = LocationHandler(dataFolder.toPath())
        settings = Settings(this)
        gameStateManager = GameStateManager(this)
        mapManager = MapManager(dataFolder.toPath().resolve("/maps"))
        gameMapManager = GameMapManager(mapManager)
        teamManager = TeamManager(dataFolder.toPath().resolve("teams.json").toFile())

        Bukkit.getPluginManager().registerEvents(gameMapManager, this)

        getCommand("skywars").executor = MainCommand(this)

        Bukkit.getWorlds().forEach { it.stopModification() }
    }
}