package net.pluginstube.skywars.plugin.kit.entities

import org.bukkit.Color

data class KitItem(
    var displayName: String?,
    var data: Short = 0,
    var amount: Int = 1,
    var enchantments: List<KitEnchantment>,
    var color: Color?
)