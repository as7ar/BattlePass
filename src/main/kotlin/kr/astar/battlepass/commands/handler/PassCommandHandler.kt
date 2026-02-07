package kr.astar.battlepass.commands.handler

import kr.astar.battlepass.BattlePass
import kr.astar.battlepass.data.PassType
import kr.astar.battlepass.data.UserData
import kr.astar.battlepass.experience.LevelingManager
import kr.astar.battlepass.gui.PassGUI
import kr.astar.battlepass.gui.PremiumPassGUI
import kr.astar.battlepass.gui.RewardGUI
import kr.astar.battlepass.item.ItemManager
import kr.astar.battlepass.util.toMiniMessage
import net.kyori.adventure.text.Component
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

class PassCommandHandler {
    private val plugin = BattlePass.plugin
    private val configData = BattlePass.configData

    private val expSlotArray = (9..17).toList().toTypedArray()
    fun openPass(player: Player, type: PassType= PassType.DEFAULT) {
        val info = ItemManager.items.info.apply {
            val meta = this?.itemMeta ?: return@apply
            meta.displayName("<bold><aqua>정보".toMiniMessage())

            val lore = meta.lore() ?: mutableListOf<Component>()
            lore.clear()
            lore.add("<gold>플레이어: <white>${player.name} ( <aqua>Lv.<white>${
                LevelingManager.getLv(player, type)
            } )".toMiniMessage())
            lore.add("<yellow>다음 레벨까지 필요 경험치: <gold> ${
                configData.pass.levelingExp-LevelingManager.getExp(player, type)
            }".toMiniMessage())
            meta.lore(lore)

            this.itemMeta = meta
        }

        val inv = when (type) {
            PassType.DEFAULT-> PassGUI().inventory
            PassType.PREMIUM-> PremiumPassGUI().inventory
            else-> RewardGUI().inventory
        }

        val numberOfExpSlot = LevelingManager.getLv(player, type)%9
        val expSlots = expSlotArray.take(numberOfExpSlot)

        val rewardSlotArray = arrayOf(18, 1, 20, 3, 22, 5, 24, 7, 26)
        val rewardSlots = LevelingManager.rewardSlotGenerator(
            player,
            LevelingManager.numberOfPassPageGenerator(player, type),
            type
        )

        val claimed= UserData(player.uniqueId).config.getInt("${player.uniqueId}.${type.name.lowercase()}.claimed-lv")

        player.openInventory(inv.apply {
            setItem(40, info) // info
            expSlots.forEach { // exp
                setItem(it, when (it) {
                    9 -> ItemManager.items.expLeft
                    17 -> ItemManager.items.expRight
                    else -> ItemManager.items.expMiddle
                })
            }
            rewardSlotArray.forEach { setItem(it, ItemManager.items.locked) }
            rewardSlots.forEach { setItem(it, ItemManager.items.unlocked) }
            rewardSlotArray.take(claimed%9).forEach { setItem(it, ItemManager.items.claimed) }
        })
    }

    fun handleExpSetting(
        sender: Player,
        target: OfflinePlayer,
        action: String,
        amount: Int,
        type: PassType
    ) {
        if (amount <= 0) return

        when (action) {
            "추가" -> {
                LevelingManager.addExp(target, amount, type)
                sender.sendMessage(
                    "플레이어 <green>${target.name}<white>에게 ${
                        if (type== PassType.PREMIUM) "<aqua>프리미엄" else { "<gray>일반" }
                    } <gray>패스 <aqua>${amount}<white>만큼의 경험치를 지급했습니다.".toMiniMessage()
                )
            }

            "제거" -> {
                LevelingManager.takeExp(target, amount, type)
                sender.sendMessage(
                    "플레이어 <red>${target.name}<white>에게서 ${
                        if (type== PassType.PREMIUM) "<aqua>프리미엄" else { "<gray>일반" }
                    } <aqua>${amount}<white>만큼의 경험치를 없앴습니다.".toMiniMessage()
                )
            }

            "설정" -> {
                LevelingManager.takeExp(target, LevelingManager.getExp(target), type)
                LevelingManager.addExp(target, amount, type)
                sender.sendMessage(
                    "플레이어 <yellow>${target.name}<white>의 ${
                        if (type== PassType.PREMIUM) "<aqua>프리미엄" else { "<gray>일반" }
                    } 경험치를 <aqua>${amount}<white> 로 설정했습니다.".toMiniMessage()
                )
            }
        }
    }

}