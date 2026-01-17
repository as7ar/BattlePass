package kr.astar.battlepass.gui

import kr.astar.battlepass.data.PassType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

class PassGUIListener: Listener {

    private val claimSlot=arrayOf(34, 35)

    @EventHandler
    fun InventoryClickEvent.onClick() {
        val player = whoClicked as? Player ?: return
        if (
            this.inventory.holder !is PassGUI
            || this.inventory.holder !is PremiumPassGUI
        ) return
        isCancelled= true
        val passType = if (
            this.inventory.holder is PassGUI
        ) PassType.DEFAULT
        else {
            PassType.PREMIUM
        }


    }

    @EventHandler
    fun InventoryCloseEvent.rewardGUI() {
        if (this.inventory.holder !is RewardGUI) return
        RewardGUI().saveRewards(this.inventory.contents.toMutableList())
    }
}