package kr.astar.battlepass.items

import com.nexomc.nexo.api.NexoItems
import dev.lone.itemsadder.api.CustomStack
import kr.astar.battlepass.BattlePass
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder

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
                NexoItems.itemFromId(id)?.also { println(it.type) }?.build()
            }
        }
    }

    fun encode(itemStack: ItemStack): String {
        return String(Base64Coder.encode(itemStack.serializeAsBytes()))
    }

    fun decode(base64: String): ItemStack {
        return ItemStack.deserializeBytes(Base64Coder.decode(base64))
    }

    fun encodeItems(items: List<ItemStack>): String {
        return String(ItemStack.serializeItemsAsBytes(items), Charsets.UTF_8)
    }

    fun decodeItems(base64: String): List<ItemStack> {
        return ItemStack.deserializeItemsFromBytes(Base64Coder.decode(base64)).toList()
    }

    object items {
        private val type = ItemType.valueOf(configData.items.type.uppercase())
        fun getPreviousArrow() = create("previous", type)
        fun getNextArrow() = create("next", type)
        fun getExpLeft() = create("exp-left", type)
        fun getExpMiddle() = create("exp-middle", type)
        fun getExpRight() = create("exp-right", type)
        fun getLocked() = create("locked", type)
        fun getUnlocked() = create("unlocked", type)
        fun getInfo() = create("info_pass", type)
    }
}