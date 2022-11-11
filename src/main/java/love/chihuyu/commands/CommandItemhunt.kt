package love.chihuyu.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.CommandPermission

object CommandItemhunt {
    val main = CommandAPICommand("itemhunt")
        .withSubcommands(
            ItemhuntStart.main,
            ItemhuntImport.main,

            ItemhuntSettings.settings
        )
        .withPermission(CommandPermission.OP)
        .withAliases("ih")
}