package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.ListArgumentBuilder
import dev.jorel.commandapi.arguments.LongArgument
import dev.jorel.commandapi.executors.CommandExecutor
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.Itemhunt.Companion.prefix
import love.chihuyu.config.ConfigKeys
import love.chihuyu.data.TargetCategory

object ItemhuntSettings {

    val setMaterials: CommandAPICommand = CommandAPICommand("materials")
        .withArguments(
            ListArgumentBuilder<String>("出現する目標アイテム").allowDuplicates(false).withList(TargetCategory.values().map { it.name }).withStringMapper().build()
        )
        .executes(CommandExecutor { sender, args ->
            val value = args[0] as List<String>
            plugin.config.set(ConfigKeys.MATERIALS.key, value)
            plugin.saveConfig()
            sender.sendMessage("$prefix 出現する目標アイテムを設定しました")
        })

    val getMaterials: CommandAPICommand = CommandAPICommand(ConfigKeys.MATERIALS.key)
        .executes(CommandExecutor { sender, args ->
            val list = plugin.config.getList(ConfigKeys.MATERIALS.key)
            if (list == null) {
                sender.sendMessage("$prefix 未設定の項目です")
                return@CommandExecutor
            }
            sender.sendMessage("$prefix 出現する目標アイテムは以下の通りです\n" + list.joinToString("\n") { "・$it" })
        })

    val setTargets: CommandAPICommand = CommandAPICommand(ConfigKeys.TARGETS.key)
        .withArguments(
            IntegerArgument("1フェーズあたりの目標アイテムの数", 1)
        )
        .executes(CommandExecutor { sender, args ->
            val value = args[0] as Int
            plugin.config.set(ConfigKeys.TARGETS.key, value)
            plugin.saveConfig()
            sender.sendMessage("$prefix 出現する目標アイテムの数を設定しました")
        })

    val getTargets: CommandAPICommand = CommandAPICommand(ConfigKeys.TARGETS.key)
        .executes(CommandExecutor { sender, args ->
            val value = plugin.config.getInt(ConfigKeys.TARGETS.key)
            if (value == 0) {
                sender.sendMessage("$prefix 未設定の項目です")
                return@CommandExecutor
            }
            sender.sendMessage("$prefix 出現する目標アイテムの数は${value}です")
        })

    val setPhaseTime: CommandAPICommand = CommandAPICommand(ConfigKeys.PHASE_TIME.key)
        .withArguments(
            LongArgument("1フェーズあたりの時間(秒)", 1)
        )
        .executes(CommandExecutor { sender, args ->
            val value = args[0] as Long
            plugin.config.set(ConfigKeys.PHASE_TIME.key, value)
            plugin.saveConfig()
            sender.sendMessage("$prefix フェーズの時間を設定しました")
        })

    val getPhaseTime: CommandAPICommand = CommandAPICommand(ConfigKeys.PHASE_TIME.key)
        .executes(CommandExecutor { sender, args ->
            val value = plugin.config.getLong(ConfigKeys.PHASE_TIME.key)
            if (value == 0L) {
                sender.sendMessage("$prefix 未設定の項目です")
                return@CommandExecutor
            }
            sender.sendMessage("$prefix 1フェーズあたりの時間は${value}秒です")
        })

    val setPhases: CommandAPICommand = CommandAPICommand(ConfigKeys.PHASES.key)
        .withArguments(
            IntegerArgument("フェーズ数", 1)
        )
        .executes(CommandExecutor { sender, args ->
            val value = args[0] as Int
            plugin.config.set(ConfigKeys.PHASES.key, value)
            plugin.saveConfig()
            sender.sendMessage("$prefix フェーズ数を設定しました")
        })

    val getPhases: CommandAPICommand = CommandAPICommand(ConfigKeys.PHASES.key)
        .executes(CommandExecutor { sender, args ->
            val value = plugin.config.getInt(ConfigKeys.PHASES.key)
            if (value == 0) {
                sender.sendMessage("$prefix 未設定の項目です")
                return@CommandExecutor
            }
            sender.sendMessage("$prefix フェーズ数は${value}です")
        })

    val settings = CommandAPICommand("settings")
        .withSubcommands(
            setMaterials,
            getMaterials,
            setTargets,
            getTargets,
            setPhaseTime,
            getPhaseTime,
            setPhases,
            getPhases
        )
}