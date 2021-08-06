package net.pluginstube.skywars.plugin.kit

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import net.pluginstube.skywars.plugin.kit.entities.Kit
import net.pluginstube.skywars.plugin.utility.adapters.MaterialTypeAdapter
import org.bukkit.Material
import java.nio.file.Files
import java.nio.file.Path
import java.util.stream.Collectors

class KitManager(kitDirectory: Path) {
    val gson: Gson = GsonBuilder()
        .serializeNulls()
        .setPrettyPrinting()
        .registerTypeAdapter(Material::class.java, MaterialTypeAdapter())
        .create()

    val kits = Files.list(kitDirectory).map {
        gson.fromJson(Files.readString(it), Kit::class.java)
    }.collect(Collectors.toList())
}