package kr.astar.battlepass.commands

import kr.astar.battlepass.BattlePass
import kr.astar.battlepass.commands.handler.PassCommandHandler
import kr.astar.battlepass.data.PassType
import kr.astar.battlepass.gui.PassGUI
import kr.astar.battlepass.gui.RewardGUI
import kr.astar.battlepass.util.toComponent
import kr.astar.battlepass.util.toMiniMessage
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PassCommand: BaseCommand(
    BattlePass.configData.command.name, BattlePass.configData.command.aliases,
    BattlePass.configData.command.description, "astar.pass.command"
) {
    private val plugin = BattlePass.plugin
    private var configData = BattlePass.configData

    private val premiumPermission = "astar.pass.premium"
    private val adminPermission = "astar.pass.admin"

    override fun execute(
        sender: CommandSender,
        args: Array<out String>
    ): Boolean {
        if (sender !is Player) return false
        if (args.isEmpty()) {
            sender.openInventory(PassGUI().inventory)
            return true
        }

        try {
            if (args[0]=="패스열기") {
                PassCommandHandler().openPass(sender, PassType.DEFAULT)
            }
            if (args[0]=="프리미엄패스열기" && sender.hasPermission(premiumPermission)) {
                PassCommandHandler().openPass(sender, PassType.PREMIUM)
            }

            if (args[0]=="설정") {
                if (args[1]=="리로드") {
                    plugin.reloadConfig()
                    plugin.reloadConfigData()
                }

                if (args[1]=="패스보상") {
                    sender.openInventory(RewardGUI().inventory)
                }

                if (args[1]=="경험치") {
                    PassCommandHandler().handleExpSetting(
                        sender, Bukkit.getOfflinePlayer(args[3]),
                        args[2], args[4].toIntOrNull() ?: run {
                            sender.sendMessage("<red>유효하지 않은 숫자를 입력했습니다".toMiniMessage())
                            return true
                        }
                    )
                }
            }
        } catch (e: ArrayIndexOutOfBoundsException) {
            sender.sendMessage("&c올바른 명령어를 입력하세요".toComponent())
        }

        return true
    }

    override fun tabComplete(
        sender: CommandSender,
        args: Array<out String>
    ): List<String?> {
        val tab = mutableListOf<String>()
        if (args.size==1) {
            tab.addAll(listOf("패스열기", "프리미엄패스열기"))
            if (sender.hasPermission(adminPermission)) tab.add("설정")
        }

        if (args.size==2 && args[0]=="설정") {
            tab.addAll(listOf(
                "리로드",
                "패스보상",
                "경험치"
            ))
        }

        if (args.size==3 && args[1]=="경험치") {
            tab.addAll(listOf(
                "추가", "제거"
            ))
        }

        if (args.size==4 && args[1]=="경험치") {
            tab.addAll(Bukkit.getOnlinePlayers().map { it.name })
        }

        if (args.size==5 && args[1]=="경험치") {
            tab.addAll(listOf(
                "숫자"
            ))
        }

        return tab
    }

}