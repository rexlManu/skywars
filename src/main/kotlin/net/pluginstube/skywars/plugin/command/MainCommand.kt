package net.pluginstube.skywars.plugin.command

import net.pluginstube.skywars.plugin.SkyWarsPlugin
import net.pluginstube.skywars.plugin.map.Map
import net.pluginstube.skywars.plugin.map.MapPosition
import net.pluginstube.skywars.plugin.utility.message
import net.pluginstube.skywars.plugin.utility.stopModification
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.World
import org.bukkit.WorldCreator
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player


class MainCommand(private val plugin: SkyWarsPlugin) : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String?>
    ): Boolean {
        var player = sender as Player
        when (if (args.isEmpty()) "" else args[0]) {
            "set" -> {
                if (args.size != 2) return true
                plugin.locationHandler.set(args[1]!!, player.location)
                sender.message("Du hast die Position für &p${args[1]} &tgesetzt.")
            }
            "ranking" -> {
                if (args.size != 2) return true
                var ranking = args[1]!!.toInt()
                if (ranking !in 1..5) return true
                var block = player.getTargetBlock(null as HashSet<Byte>, 10)
                if (block?.type != Material.WALL_SIGN) return true
                plugin.locationHandler.set("ranking-${args[1]!!}", block.location)
                sender.message("Du hast das Ranking für Platz &p${args[1]} &tgesetzt.")
            }
            "import" -> {
                if (args.size != 2) return true
                importWorld(args[1]!!)
            }
            "tp" -> {
                if (args.size != 2) return true
                player.teleport(Bukkit.getWorld(args[1]!!)?.spawnLocation)
            }
            "map" -> {
                when (args.size) {
                    1 -> {
                        sender.message("/skywars map create <name>")
                        sender.message("/skywars map setdisplayitem <map>")
                        sender.message("/skywars map setdescription <map> <description...>")
                        sender.message("/skywars map setworldname <map> <worldName>")
                        sender.message("/skywars map setposition <map> <name>")
                    }
                    3 -> {
                        when (args[2]) {
                            "create" -> {
                                plugin.mapManager.save(Map.create(args[3]!!)).also {
                                    sender.message("Du hast erfolgreich die Map &s${args[3]} &terstellt.")
                                }
                            }
                            "setdisplayitem" -> {
                                if (player.itemInHand.type == Material.AIR) {
                                    sender.message("Du hast &pkein Material &tin der Hand.")
                                    return true;
                                }
                                plugin.mapManager.save(plugin.mapManager.find(args[3]!!).apply {
                                    displayItem.apply {
                                        material = player.itemInHand.type
                                        data = player.itemInHand.durability
                                    }

                                    sender.message("Du hast das Material der Map &p$name&t geändert.")
                                })
                            }
                        }
                    }
                    4 -> {
                        var map = plugin.mapManager.find(args[3]!!);

                        when (args[2]) {
                            "setworldname" -> {
                                plugin.mapManager.save(map.apply {
                                    worldName = args[4]!!

                                    sender.message("Du hast den Worldname der Map &p$name&t zu &p$worldName&t geändert.")
                                })
                            }
                            "setdescription" -> {
                                plugin.mapManager.save(map.apply {
                                    description = args[4]!!

                                    sender.message("Du hast den Beschreibung der Map &p$name&t zu &p$description&t geändert.")
                                })
                            }
                            "setposition" -> {
                                plugin.mapManager.save(map.apply {
                                    positions += args[4]!! to MapPosition.fromBukkitLocation(player.location)

                                    sender.message("Du hast die Position &p${args[4]!!}&t für die Map &p$name&t gesetzt.")
                                })
                            }
                        }
                    }
                    else -> {
                        if (args.size > 4 && args[2] == "setdescription") {
                            plugin.mapManager.save(plugin.mapManager.find(args[3]!!).apply {
                                description = args.drop(3).joinToString()

                                sender.message("Du hast den Beschreibung der Map &p$name&t zu &p$description&t geändert.")
                            })
                        }
                    }
                }
            }
            else -> {
                sender.message("/skywars set <spawn,hologram>")
                sender.message("/skywars ranking <1-5>")
                sender.message("/skywars import <worldName>")
                sender.message("/skywars tp <worldName>")
                sender.message("/skywars map")
            }
        }
        return true
    }

    private fun importWorld(worldName: String): World {
        return WorldCreator(worldName).createWorld().stopModification()
    }
}