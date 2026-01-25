package kr.astar.battlepass.gui

import com.google.gson.Gson
import kr.astar.battlepass.BattlePass
import kr.astar.battlepass.item.ItemManager.decode
import kr.astar.battlepass.item.ItemManager.encode
import kr.astar.battlepass.util.toMiniMessage
import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import java.io.File

class RewardGUI : InventoryHolder {
    private val plugin = BattlePass.plugin

    private val configData = BattlePass.configData
    private val inventory = Bukkit.createInventory(
        this,
        9 * 6,
        configData.gui.rewardTitle.toMiniMessage()
    )
    val file = File(plugin.dataFolder, "reward.json")
    private val gson = Gson()

    override fun getInventory(): Inventory {
        loadRewards()
        return inventory
    }

    private fun loadRewards() {
        if (!file.exists()) return

        val data = file.reader().use {
            gson.fromJson(it, Map::class.java)
        } as Map<*, *>

        data.forEach { (slotKey, value) ->
            val slot = slotKey.toString().toIntOrNull() ?: return@forEach
            val base64 = value as? String ?: return@forEach

            if (slot !in 0 until inventory.size) return@forEach
            inventory.setItem(slot, decode(base64))
        }
    }

    fun saveRewards(items: List<ItemStack?>) {
        if (!file.parentFile.exists()) file.parentFile.mkdirs()

        val map = mutableMapOf<String, String>()

        items.forEachIndexed { index, item ->
            if (item == null || item.type.isAir) return@forEachIndexed
            map[index.toString()] = encode(item)
        }

        file.writer().use {
            gson.toJson(map, it)
        }
    }
}
