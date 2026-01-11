package kr.astar.battlepass.commands

import kr.astar.battlepass.BattlePass
import kr.astar.battlepass.data.config
import kr.astar.battlepass.gui.PassGUI
import kr.astar.battlepass.gui.PremiumPassGUI
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class PassCommand: BaseCommand(
    BattlePass.configData.command.name, BattlePass.configData.command.aliases,
    BattlePass.configData.command.description, "astar.pass.command"
) {
    private val plugin= BattlePass.plugin

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

        if (args[0]=="패스열기") {
            sender.openInventory(PassGUI().inventory)
        }
        if (args[0]=="프리미엄패스열기" && sender.hasPermission(premiumPermission)) {
            sender.openInventory(PremiumPassGUI().inventory)
        }

        if (args[0]=="설정") {
            if (args[1]=="리로드") {
                plugin.reloadConfig()
                plugin.reloadConfigData()
            }
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

        if (args.size==2 && args[1]=="설정") {
            tab.addAll(listOf(
                "리로드",
                "보상추가",
                "경험치"
            ))
        }

        return tab
    }

}