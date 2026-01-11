package kr.astar.battlepass.gui

import kr.astar.battlepass.BattlePass
import kr.astar.battlepass.utils.toMiniMessage
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class PassGUI: InventoryHolder {
    private val configData= BattlePass.configData
    private val inventory = Bukkit.createInventory(
        this, 9*6,
        configData.gui.title.toMiniMessage()
    )

    private val rewardSlotArray = arrayOf(1, 3, 5, 7, 18, 20, 22, 24, 26)
    private val expSlotArray = (9..17).toList().toTypedArray()
    private val claimSlot=arrayOf(34, 35)
    private val previousSlot= 36
    private val nextSlot= 44

    override fun getInventory(): Inventory {
        return inventory
    }
}