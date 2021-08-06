package net.pluginstube.skywars.plugin.player

import net.pluginstube.skywars.plugin.kit.entities.Kit
import java.util.*

/**
 * The data class for the player to store various things
 */
data class GamePlayer(
    val uniqueId: UUID
) {
    // Default no kit is selected
    var kit: Kit? = null
}
