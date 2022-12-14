package love.chihuyu.utils

import com.convallyria.languagy.api.language.key.TranslationKey
import love.chihuyu.Itemhunt.Companion.plugin
import love.chihuyu.Itemhunt.Companion.translator
import love.chihuyu.game.GameManager
import love.chihuyu.game.PlayerData
import love.chihuyu.game.TargetItem
import net.kyori.adventure.text.Component
import org.bukkit.ChatColor
import org.bukkit.scoreboard.Criteria
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.RenderType
import java.util.*

object ScoreboardUtil {

    fun updateServerScoreboard() {
        GameManager.board.objectives.forEach(Objective::unregister)

        val objTarget = GameManager.board.registerNewObjective(
            "main",
            Criteria.DUMMY,
            Component.text("   ${ChatColor.GOLD}${ChatColor.UNDERLINE}${ChatColor.BOLD}Item Hunt${ChatColor.RESET}   ")
        ).apply {
            this.displaySlot = DisplaySlot.SIDEBAR
            this.renderType = RenderType.INTEGER
        }

        val objRanking = GameManager.board.registerNewObjective(
            "ranking",
            Criteria.DUMMY,
            Component.empty()
        ).apply {
            this.displaySlot = DisplaySlot.PLAYER_LIST
        }

        if (!GameManager.started) {
            objTarget.unregister()

            val objWaiting = GameManager.board.registerNewObjective(
                "main",
                Criteria.DUMMY,
                Component.text("   ${ChatColor.GOLD}${ChatColor.UNDERLINE}${ChatColor.BOLD}Item Hunt${ChatColor.RESET}   "),
                RenderType.INTEGER
            ).apply {
                this.displaySlot = DisplaySlot.SIDEBAR
            }

            val scores = mutableListOf(
                " ",
                "待機中...",
                "  "
            )

            scores.forEachIndexed { index, s ->
                objWaiting.getScore(s).score = scores.lastIndex - index
            }

            objRanking.unregister()

            plugin.server.onlinePlayers.forEach { player -> player.scoreboard = GameManager.board }
            return
        }

        val scores = mutableListOf(
            " ",
            "目標リスト",
            "  "
        )

        TargetItem.activeTarget.filterNotNull().forEachIndexed { index, material ->
            val jpPlayer =
                plugin.server.onlinePlayers.firstOrNull { it.locale() == Locale.JAPAN } ?: plugin.server.onlinePlayers.random()
            val translated =
                translator.getTranslationFor(jpPlayer, TranslationKey.of(material.translationKey())).colour().first()
            val point = TargetDataUtil.getPoint(material)
            scores.add(index + 2, "・$translated ${ChatColor.GRAY}(${point}pt)${ChatColor.RESET}")
        }

        scores.forEachIndexed { index, s ->
            objTarget.getScore(s).score = scores.lastIndex - index

            plugin.server.onlinePlayers.forEach { player ->
                objRanking.getScore(player).score = PlayerData.points[player.uniqueId]?.toList()?.sumOf { it.second } ?: 0
                player.scoreboard = GameManager.board
            }
        }
    }
}