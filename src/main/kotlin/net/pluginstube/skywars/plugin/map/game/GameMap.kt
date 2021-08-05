package net.pluginstube.skywars.plugin.map.game

import net.pluginstube.skywars.plugin.map.Map
import java.util.*

class GameMap(var map: Map) {
    var voters = mutableListOf<UUID>()
}