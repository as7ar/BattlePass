package kr.astar.battlepass.commands.handler

import kr.astar.battlepass.BattlePass
import kr.astar.battlepass.data.PassType
import kr.astar.battlepass.experience.LevelingManager
import kr.astar.battlepass.gui.PassGUI
import kr.astar.battlepass.gui.PremiumPassGUI
import kr.astar.battlepass.items.ItemManager
import kr.astar.battlepass.utils.toMiniMessage
import net.kyori.adventure.text.Component
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class PassCommandHandler {
    private val plugin= BattlePass.plugin
    private val configData = BattlePass.configData

    private val expSlotArray = (9..17).toList().toTypedArray()
    fun openPass(player: Player, type: PassType) {
        val info = ItemManager.items.getInfo().apply {
            val meta = this?.itemMeta ?: return@apply
            meta.displayName("<bold><aqua>정보".toMiniMessage())
            val lore = meta.lore() ?: mutableListOf<Component>()
            lore.add("<gold>플레이어: <white>${player.name} ( <aqua>Lv.<white>${
                LevelingManager.getLv(player)
            } )".toMiniMessage())
            lore.add("<yellow>다음 레벨까지 필요 경험치: <gold> ${
                configData.pass.levelingExp-LevelingManager.getExp(player)
            }".toMiniMessage())
            meta.lore(lore)
            this.itemMeta = meta
        }

        val inv = when (type) {
            PassType.DEFAULT-> PassGUI().inventory
            PassType.PREMIUM-> PremiumPassGUI().inventory
        }

        player.openInventory(inv.apply {
            setItem(40, info)
        })
    }

    fun handleExpSetting(
        sender: Player,
        target: OfflinePlayer,
        action: String,
        amount: Int
    ) {
        if (amount <= 0) return

        when (action) {
            "추가" -> {
                LevelingManager.addExp(target, amount)
                sender.sendMessage(
                    "플레이어 <green>${target.name}에게 ${amount}만큼의 경험치를 지급했습니다.".toMiniMessage()
                )
            }

            "제거" -> {
                LevelingManager.takeExp(target, amount)
                sender.sendMessage(
                    "플레이어 <red>${target.name}에게서 ${amount}만큼의 경험치를 없앴습니다.".toMiniMessage()
                )
            }

            "설정" -> {
                LevelingManager.takeExp(target, LevelingManager.getExp(target))
                LevelingManager.addExp(target, amount)
                sender.sendMessage(
                    "플레이어 <yellow>${target.name}의 경험치를 $amount 로 설정했습니다.".toMiniMessage()
                )
            }
        }
    }

}