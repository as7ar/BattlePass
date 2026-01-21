package kr.astar.battlepass.gui

import com.google.gson.Gson
import com.google.gson.JsonObject
import kr.astar.battlepass.commands.handler.PassCommandHandler
import kr.astar.battlepass.data.UserData
import kr.astar.battlepass.experience.LevelingManager
import kr.astar.battlepass.item.ItemManager
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryType

class PassGUIListener: Listener {

    private val claimSlot=arrayOf(34, 35)
    private val previousSlot= 36
    private val nextSlot= 44

    @EventHandler
    fun InventoryClickEvent.onClick() {
        val player = whoClicked as? Player ?: return
        if (this.inventory.holder !is PassGUI) return
        if (clickedInventory?.type== InventoryType.PLAYER) return
        isCancelled= true

        if (slot in claimSlot) {
            val userData = UserData(player.uniqueId)
            val level=LevelingManager.getLv(player)
            val claimed= UserData(player.uniqueId).config.getInt("${player.uniqueId}.claimed-lv", 0)
            userData.setValue("${player.uniqueId}.claimed-lv", LevelingManager.getLv(player))

            player.closeInventory()
            PassCommandHandler().openPass(player)

            val file= RewardGUI().file
            val json= Gson().fromJson(file.reader(Charsets.UTF_8), JsonObject::class.java)
            for (i in claimed..<level) {
                val item= ItemManager.decode(json["$i"].asString.toString())
                player.give(item)
            }
        }
        if (slot == previousSlot) {

        }
        if (slot == nextSlot) {

        }
    }

    @EventHandler
    fun InventoryCloseEvent.rewardGUI() {
        if (this.inventory.holder !is RewardGUI) return
        RewardGUI().saveRewards(this.inventory.contents.toMutableList())
    }
}