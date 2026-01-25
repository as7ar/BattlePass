package kr.astar.battlepass.gui

import kr.astar.battlepass.BattlePass
import kr.astar.battlepass.item.ItemManager
import kr.astar.battlepass.util.toMiniMessage
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder

class PremiumPassGUI: InventoryHolder {
    private val configData= BattlePass.configData
    private val inventory = Bukkit.createInventory(
        this, 9*6,
        configData.gui.premiumTitle.toMiniMessage()
    )

    private val previousSlot = 36
    private val nextSlot = 44

    override fun getInventory(): Inventory {
        inventory.setItem(previousSlot, ItemManager.items.previousArrow)
        inventory.setItem(nextSlot, ItemManager.items.nextArrow)
        return inventory
    }
}