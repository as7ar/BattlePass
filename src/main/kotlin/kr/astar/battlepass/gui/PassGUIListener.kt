package kr.astar.battlepass.gui

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class PassGUIListener: Listener {
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

    enum class PassType {
        DEFAULT,
        PREMIUM
    }
}