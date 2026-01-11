package kr.astar.battlepass.items

import com.nexomc.nexo.api.NexoItems
import dev.lone.itemsadder.api.CustomStack
import kr.astar.battlepass.BattlePass
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack

object ItemManager {
    private val plugin = BattlePass.plugin
    private val configData= BattlePass.configData

    enum class ItemType {
        ITEMSADDER,
        NEXO
    }

    private fun checkItemPlugin(type: ItemType): Boolean {
        return when (type) {
            ItemType.ITEMSADDER -> {
                Bukkit.getPluginManager().getPlugin("ItemsAdder")!=null
            }
            ItemType.NEXO -> {
                Bukkit.getPluginManager().getPlugin("Nexo")!=null
            }
        }
    }

    fun create(id: String, type: ItemType): ItemStack? {
        return when (type) {
            ItemType.ITEMSADDER -> {
                if (!checkItemPlugin(type)) return null
                CustomStack.getInstance("battlepass:${id}")?.itemStack
            }
            ItemType.NEXO -> {
                if (!checkItemPlugin(type)) return null
                NexoItems.itemFromId(id)?.build()
            }
        }
    }

    object items {
        private val type = ItemType.valueOf(configData.items.type.uppercase())
        fun getPreviousArrow() = create("previous-arrow", type)
        fun getNextArrow() = create("next-arrow", type)
        fun getExpLeft() = create("exp-left", type)
        fun getExpMiddle() = create("exp-middle", type)
        fun getExpRight() = create("exp-right", type)
        fun getLocked() = create("locked", type)
        fun getUnlocked() = create("unlocked", type)
    }
}