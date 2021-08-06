package net.pluginstube.skywars.plugin.gamestate

import net.pluginstube.skywars.plugin.SkyWarsPlugin
import net.pluginstube.skywars.plugin.gamestate.states.EndingState
import net.pluginstube.skywars.plugin.gamestate.states.PlayingState
import net.pluginstube.skywars.plugin.gamestate.states.WaitingState
import net.pluginstube.skywars.plugin.utility.register
import net.pluginstube.skywars.plugin.utility.unregister

class GameStateManager(private val plugin: SkyWarsPlugin) {

    val states = mapOf(
        GameState.WAITING to WaitingState(plugin),
        GameState.PLAYING to PlayingState(plugin),
        GameState.ENDING to EndingState(plugin)
    )

    var currentGameState: GameState = GameState.CREATING
    lateinit var currentState: State

    init {
        switchTo(GameState.WAITING)
    }

    fun switchTo(gameState: GameState) {
        if (currentGameState != GameState.CREATING) {
            currentState.end()
            currentState.unregister()
        }

        currentGameState = gameState
        currentState = states[gameState]!!

        currentState.start()
        currentState.register(plugin)
    }
}