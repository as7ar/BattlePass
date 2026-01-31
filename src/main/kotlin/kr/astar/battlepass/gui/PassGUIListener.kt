package kr.astar.battlepass.gui

import com.google.gson.Gson
import com.google.gson.JsonObject
import kr.astar.battlepass.commands.handler.PassCommandHandler
import kr.astar.battlepass.data.PassType
import kr.astar.battlepass.data.UserData
import kr.astar.battlepass.experience.LevelingManager
import kr.astar.battlepass.item.ItemManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType
import java.util.UUID

class PassGUIListener: Listener {

    private val claimSlot=arrayOf(34, 35)
    private val previousSlot= 36
    private val nextSlot= 44
    private val userPageData= mutableMapOf<UUID, Int>()

    @EventHandler
    fun InventoryClickEvent.onClick() {
        val player = whoClicked as? Player ?: return

        val isPass = inventory.holder is PassGUI
        val isPremium = inventory.holder is PremiumPassGUI
        if (!isPass && !isPremium) return
        if (clickedInventory?.type == InventoryType.PLAYER) return
        isCancelled = true

        val path = if (isPremium) "premium" else "default"
        val type = PassType.valueOf(path.uppercase())
        var page = userPageData[player.uniqueId] ?: run {
            userPageData[player.uniqueId]=LevelingManager.numberOfPassPageGenerator(player, type)
            userPageData[player.uniqueId]!!
        }

        val maxPage = LevelingManager.numberOfPassPageGenerator(player, type)

        if (slot in claimSlot) {
            val userData = UserData(player.uniqueId)
            val level = LevelingManager.getLv(player, type)
            val claimed = userData.config.getInt("${player.uniqueId}.$path.claimed-lv", 0)

            if (level <= claimed) return

            userData.setValue("${player.uniqueId}.$path.claimed-lv", level)

            val file = if (isPremium) PremiumRewardGUI().file else RewardGUI().file
            val json = Gson().fromJson(file.reader(Charsets.UTF_8), JsonObject::class.java)

            for (i in claimed until level) {
                val item = ItemManager.decode(json["$i"].asString)
                player.give(item)
            }

            player.closeInventory()
            PassCommandHandler().openPass(player, type)
            return
        }

        when (slot) {
            previousSlot -> {
                if (page <= 1) return
                page--
            }
            nextSlot -> {
                if (page >= maxPage) return
                page++
            }
            else -> return
        }

        userPageData[player.uniqueId] = page

        val rewardSlots = LevelingManager.rewardSlotGenerator(player, page, type)

        val numberOfExpSlot = LevelingManager.getLv(player, type) % 9
        val expSlots = if (page >= LevelingManager.numberOfPassPageGenerator(player, type)) {
            (9..17).take(numberOfExpSlot)
        } else { (9..17) }
        val rewardSlotArray = arrayOf(18, 1, 20, 3, 22, 5, 24, 7, 26)

        val claimed = UserData(player.uniqueId).config.getInt("${player.uniqueId}.$path.claimed-lv")

        inventory.apply {
            (9..17).forEach { setItem(it, null) }
            expSlots.forEach { setItem(it,
                when (it) {
                    9 -> ItemManager.items.expLeft
                    17 -> ItemManager.items.expRight
                    else -> ItemManager.items.expMiddle
                }
            ) }

            rewardSlotArray.forEach { setItem(it, ItemManager.items.locked) }
            rewardSlots.forEach { setItem(it, ItemManager.items.unlocked) }
            val claimedSlots = if (
                page >= LevelingManager.numberOfPassPageGenerator(player, type)
                ) {
                rewardSlotArray.take(claimed % 9)
            } else {
                rewardSlotArray.toList()
            }
            claimedSlots.forEach {
                setItem(it, ItemManager.items.claimed)
            }
        }
        player.updateInventory()
    }


    @EventHandler
    fun InventoryCloseEvent.rewardGUI() {
        if (this.inventory.holder is RewardGUI) {
            RewardGUI().saveRewards(this.inventory.contents.toMutableList())
        }
        if (this.inventory.holder is PremiumRewardGUI) {
            PremiumRewardGUI().saveRewards(this.inventory.contents.toMutableList())
        }
    }
}