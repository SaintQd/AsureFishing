package org.saintqd.asurefishing.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.saintqd.asurefishing.AsureFishing
import org.saintqd.asurelib.AsureLib

class AsureFishingCommands {

    companion object {

        fun setupCommands(plugin : AsureFishing) {
            val manager = plugin.lifecycleManager
            manager.registerEventHandler(LifecycleEvents.COMMANDS, {
                val commands: Commands = it.registrar()
                commands.register(
                    Commands.literal("asurefishing")
                        .executes { commandContext: CommandContext<CommandSourceStack?>? ->
                            commandContext!!.getSource()!!.sender.sendMessage(
                                AsureLib.inst().langManager.parseLangString(AsureFishing.inst(), "not_enough_arguments")
                            )
                            Command.SINGLE_SUCCESS
                        }
                        .then(
                            Commands.literal("reload")
                                .requires { predicate: CommandSourceStack? ->
                                    predicate!!.sender.hasPermission("asurefishing.admin")
                                }
                                .executes { ctx: CommandContext<CommandSourceStack?>? ->
                                    reloadCommand(
                                        ctx!!.getSource()!!.sender
                                    )
                                    Command.SINGLE_SUCCESS
                                }
                        )
                        .build(),
                    "Основная команда."
                )
            })
        }

        private fun reloadCommand(sender: CommandSender?) {
            AsureFishing.inst().loadData()
            if (sender is Player) sender.sendMessage(
                AsureLib.inst().langManager.parseLangString(AsureFishing.inst(), "reload_message")
            )
        }
    }

}