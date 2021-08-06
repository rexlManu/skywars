package net.pluginstube.skywars.plugin.map

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.pluginstube.skywars.plugin.utility.adapters.MaterialTypeAdapter
import net.pluginstube.skywars.plugin.utility.stopModification
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.WorldCreator
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.stream.Collectors

class MapManager(val mapDirectory: Path) {
    val gson: Gson = GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .registerTypeAdapter(Material::class.java, MaterialTypeAdapter())
        .create()

    var maps: List<Map> = Files.list(mapDirectory).map {
        gson.fromJson(Files.readString(it), Map::class.java)
    }.collect(Collectors.toList())

    init {
        maps.forEach {
            println("Found map ${it.name}")
        }
    }

    fun find(name: String): Map {
        return maps.first { map -> map.name == name }
    }

    fun save(map: Map) {
        Files.writeString(
            mapDirectory.resolve("${map.name}.json"),
            gson.toJson(map),
            StandardOpenOption.WRITE,
            StandardOpenOption.TRUNCATE_EXISTING,
            StandardOpenOption.CREATE
        )
    }
}