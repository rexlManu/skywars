package net.pluginstube.skywars.plugin.config

import net.pluginstube.skywars.plugin.utility.asFormatted
import net.pluginstube.skywars.plugin.utility.message
import org.bukkit.Location
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Player
import java.nio.file.Path

class LocationHandler(dataFolder: Path) {
    val configFile = dataFolder.resolve("locations.yml")
    val configuration = YamlConfiguration.loadConfiguration(configFile.toFile())

    fun set(key: String, location: Location) {
        configuration.set(key, location)
        configuration.save(configFile.toFile())
    }

    fun get(key: String): Location? {
        return configuration.get(key) as Location?
    }

    fun teleport(key: String, player: Player) {
        val location = get(key)
        if (location != null) {
            player.teleport(location)
        }else {
            player.sendMessage("Die Position &p$key &tkonnte nicht gefunden werden.".asFormatted())
        }
    }
}