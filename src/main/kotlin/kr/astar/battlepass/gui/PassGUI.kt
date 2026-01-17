package kr.astar.battlepass.gui

import com.google.gson.Gson
import kr.astar.battlepass.BattlePass
import kr.astar.battlepass.experience.LevelingManager
import kr.astar.battlepass.items.ItemManager
import kr.astar.battlepass.utils.toMiniMessage
import net.kyori.adventure.text.Component
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import java.io.File

class PassGUI: InventoryHolder {
    private val plugin = BattlePass.plugin

    private val configData= BattlePass.configData
    private val inventory = Bukkit.createInventory(
        this, 9*6,
        configData.gui.title.toMiniMessage()
    )

    private val previousSlot= 36
    private val nextSlot= 44

    private val file = File(plugin.dataFolder, "reward.json")
    private val gson = Gson()

    override fun getInventory(): Inventory {

        inventory.setItem(previousSlot, ItemManager.items.getPreviousArrow())
        inventory.setItem(nextSlot, ItemManager.items.getNextArrow())

        return inventory
    }
}