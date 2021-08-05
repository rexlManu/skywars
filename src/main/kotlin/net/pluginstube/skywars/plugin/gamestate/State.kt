package net.pluginstube.skywars.plugin.gamestate

import org.bukkit.event.Listener

interface State : Listener {
    fun start();

    fun end();
}