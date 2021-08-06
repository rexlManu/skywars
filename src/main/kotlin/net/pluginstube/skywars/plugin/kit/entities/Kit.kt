package net.pluginstube.skywars.plugin.kit.entities

import net.pluginstube.skywars.plugin.utility.item.DisplayItem

data class Kit(
    var name: String,
    var cost: Int,
    var description: String,
    var displayItem: DisplayItem,
    var armor: KitArmor,
    var sortingIndex: Int
)