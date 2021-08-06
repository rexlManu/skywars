package net.pluginstube.skywars.plugin.gamestate.states

import fr.mrmicky.fastboard.FastBoard
import net.pluginstube.skywars.plugin.SkyWarsPlugin
import net.pluginstube.skywars.plugin.gamestate.GameState
import net.pluginstube.skywars.plugin.gamestate.State
import net.pluginstube.skywars.plugin.inventory.KitInventory
import net.pluginstube.skywars.plugin.inventory.MapInventory
import net.pluginstube.skywars.plugin.inventory.TeamInventory
import net.pluginstube.skywars.plugin.inventory.event.PlayerKitSelectEvent
import net.pluginstube.skywars.plugin.inventory.event.PlayerMapSelectEvent
import net.pluginstube.skywars.plugin.inventory.event.PlayerTeamSelectEvent
import net.pluginstube.skywars.plugin.utility.*
import net.pluginstube.skywars.plugin.utility.item.name
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.player.*
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.scheduler.BukkitTask
import java.util.*

/**
 * Handles the lobby state and prepares players for the [PlayingState ]
 */
class WaitingState(private val plugin: SkyWarsPlugin) : State, Runnable {
    private val startSeconds = 60
    val leaveItem = item(Material.NETHER_STAR) {
        meta<ItemMeta> {
            name = "&td● &pSpiel verlassen &td▶ &tRechtsklick &td●".withParameters().withColors()
        }
    }
    val kitSelectorItem = item(Material.CHEST) {
        meta<ItemMeta> {
            name = "&td● &pKitauswahl &td▶ &tRechtsklick &td●".withParameters().withColors()
        }
    }
    val mapSelectorItem = item(Material.PAPER) {
        meta<ItemMeta> {
            name = "&td● &pMapvoting &td▶ &tRechtsklick &td●".withParameters().withColors()
        }
    }

    val teamSelectorItem = item(Material.BED) {
        meta<ItemMeta> {
            name = "&td● &pTeamauswahl &td▶ &tRechtsklick &td●".withParameters().withColors()
        }
    }

    val forceStartItem = item(Material.EMERALD) {
        meta<ItemMeta> {
            name = "&td● &pSpiel starten &td▶ &tRechtsklick &td●".withParameters().withColors()
        }
    }
    val fastBoards = HashMap<UUID, FastBoard>()

    lateinit var countdownTask: BukkitTask
    var seconds = startSeconds
    override fun start() {
        countdownTask = Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, this, 0, 20);
    }

    override fun end() {
        countdownTask.cancel()

        Bukkit.getOnlinePlayers().forEach {
            // Check for player if he doesnt have a team
            if (plugin.teamManager.getTeam(it.uniqueId) == null) {
                plugin.teamManager.findTeam().members += it.uniqueId
            }
            var gamePlayer = plugin.gamePlayerContainer.getPlayer(it.uniqueId)
            // Check for player if he doesnt choose a kit
            if (gamePlayer.kit == null) {
                gamePlayer.kit = plugin.kitManager.kits.first()
            }
        }
    }

    override fun run() {
        if (seconds == 0) {
            countdownTask.cancel()
            plugin.gameStateManager.switchTo(GameState.PLAYING)
            return
        }
        Bukkit.getOnlinePlayers().forEach {
            it.level = 0
            it.exp = seconds.toFloat() / startSeconds.toFloat()
            it.actionbar("&s&l$seconds&ts".withParameters().withColors())
        }

        if (!startAvailable()) {
            seconds = startSeconds
            return
        }

        if (seconds % 10 == 0 || seconds < 6) {
            broadcast("&tDas Spiel startet in &s&l$seconds &tSekunde${if (seconds == 1) "" else "n"}.".asFormatted())
            Bukkit.getOnlinePlayers().forEach {
                it.playSound(Sound.CHICKEN_EGG_POP, 1.5f)
            }
        }

        seconds--;
    }

    private fun startAvailable(): Boolean =
        !(Bukkit.getOnlinePlayers().isEmpty() || Bukkit.getOnlinePlayers().size < plugin.settings.minPlayers)


    @EventHandler
    fun handle(event: PlayerJoinEvent) {
        var player = event.player
        event.joinMessage = "&p${player.name} &that die Runde betreten.".asFormatted()
        PlayerUtils.resetPlayer(player)
        createScoreboard(player)
        plugin.locationHandler.teleport("spawn", player)

        giveItems(player)

        Bukkit.getOnlinePlayers().forEach { if (it != player) updateLines(it) }
    }

    private fun giveItems(player: Player) {
        val teamSelector = plugin.settings.teamSize == 1
        val forceGame = player.hasPermission("skywars.game.forcestart")
        when {
            !forceGame && teamSelector -> {
                player.inventory.setItem(0, kitSelectorItem)
                player.inventory.setItem(3, teamSelectorItem)
                player.inventory.setItem(5, mapSelectorItem)
                player.inventory.setItem(8, leaveItem)
            }
            !forceGame && !teamSelector -> {
                player.inventory.setItem(0, kitSelectorItem)
                player.inventory.setItem(4, mapSelectorItem)
                player.inventory.setItem(8, leaveItem)
            }
            forceGame && !teamSelector -> {
                player.inventory.setItem(0, kitSelectorItem)
                player.inventory.setItem(3, mapSelectorItem)
                player.inventory.setItem(5, forceStartItem)
                player.inventory.setItem(8, leaveItem)
            }
            forceGame && teamSelector -> {
                player.inventory.setItem(0, kitSelectorItem)
                player.inventory.setItem(3, teamSelectorItem)
                player.inventory.setItem(4, forceStartItem)
                player.inventory.setItem(5, mapSelectorItem)
                player.inventory.setItem(8, leaveItem)
            }
        }
    }

    @EventHandler
    fun handle(event: PlayerQuitEvent) {
        event.quitMessage = "&p${event.player.name} &that die Runde verlassen.".asFormatted()
        fastBoards.remove(event.player.uniqueId)
        Bukkit.getOnlinePlayers().forEach { updateLines(it) }
    }

    @EventHandler
    fun handle(event: PlayerInteractEvent) {
        event.isCancelled = true
        val player = event.player

        if (!(event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR)) return

        when (event.item) {
            leaveItem -> {
                player.kickPlayer("")
            }
            mapSelectorItem -> {
                player.playSound(Sound.CHEST_OPEN, 2f)
                MapInventory(plugin, player).apply {
                    register(plugin)
                    open()
                }
            }
            kitSelectorItem -> {
                player.playSound(Sound.CHEST_OPEN, 2f)
                KitInventory(plugin, player).apply {
                    register(plugin)
                    open()
                }
            }
            teamSelectorItem -> {
                player.playSound(Sound.CHEST_OPEN, 2f)
                TeamInventory(plugin, player).apply {
                    register(plugin)
                    open()
                }
            }
            forceStartItem -> {
                when {
                    !startAvailable() -> {
                        player.sendMessage("Das Spiel kann nicht gestartet werden.".asFormatted())
                    }
                    seconds > 5 -> {
                        seconds = 5
                        player.sendMessage("Der Spielstart wurde beschleunigt.".asFormatted())
                        player.playSound(Sound.LEVEL_UP, 1f)
                    }
                    else -> {
                        player.sendMessage("Das Spiel startet bereits gleich.".asFormatted())
                    }
                }
            }
        }
    }

    private fun createScoreboard(player: Player) {
        fastBoards[player.uniqueId] = FastBoard(player)
        fastBoards[player.uniqueId]?.updateTitle("&td« &p&lSkyWars &td»".withParameters().withColors())
        updateLines(player)
    }

    private fun updateLines(player: Player) {
        var gamePlayer = plugin.gamePlayerContainer.getPlayer(player.uniqueId)
        fastBoards[player.uniqueId]?.updateLines(
            listOf(
                "",
                "&tMap&td:",
                " &td» &p" + plugin.gameMapManager.getVotedMap(player.uniqueId) ?: "???",
                "",
                "&tDein Kit&td:",
                " &td» &p" + gamePlayer.kit?.name ?: "???",
                "",
                "&tDein Team&td:",
                " &td» &p" + plugin.teamManager.getTeam(player.uniqueId) ?: "???",
                "",
                "&tSpieler&td:",
                " &td» &p" + Bukkit.getOnlinePlayers().size,
            ).map { it.withParameters().withColors() }.toList()
        )
    }

    @EventHandler
    fun handle(event: PlayerTeamSelectEvent) = updateLines(event.player)

    @EventHandler
    fun handle(event: PlayerKitSelectEvent) = updateLines(event.player)

    @EventHandler
    fun handle(event: PlayerMapSelectEvent) = updateLines(event.player)

    @EventHandler
    fun handle(event: PlayerDropItemEvent) = event.apply { isCancelled = true }

    @EventHandler
    fun handle(event: PlayerPickupItemEvent) = event.apply { isCancelled = true }

    @EventHandler
    fun handle(event: BlockPlaceEvent) = event.apply { isCancelled = true }

    @EventHandler
    fun handle(event: BlockBreakEvent) = event.apply { isCancelled = true }

    @EventHandler
    fun handle(event: PlayerArmorStandManipulateEvent) = event.apply { isCancelled = true }

    @EventHandler
    fun handle(event: EntityDamageEvent) = event.apply { isCancelled = true }

    @EventHandler
    fun handle(event: FoodLevelChangeEvent) = event.apply { isCancelled = true }
}