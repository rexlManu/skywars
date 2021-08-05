package net.pluginstube.skywars.plugin.config.converter

import org.bukkit.configuration.file.YamlConfiguration

interface Converter<C> {
    fun from(config: YamlConfiguration): C
    fun to(value: C, config: YamlConfiguration)
}