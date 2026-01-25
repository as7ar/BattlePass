package kr.astar.battlepass.item

import com.nexomc.nexo.api.NexoItems
import dev.lone.itemsadder.api.CustomStack
import kr.astar.battlepass.BattlePass
import net.momirealms.craftengine.bukkit.api.CraftEngineItems
import net.momirealms.craftengine.core.util.Key
import org.bukkit.Bukkit
import org.bukkit.inventory.ItemStack
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder
import java.util.EnumMap

/* 나중에 갈아 엎을때 어케함
* 아이템은 한번 던져질거 같은데
* Folra 제작 하시져 시간되시면 저도 투입함 */

object ItemManager {
    private val plugin = BattlePass.plugin
    private val configData = BattlePass.configData

    enum class ItemType(val pluginName: String) {
        ITEMSADDER("ItemsAdder"),
        NEXO("Nexo"),
        CRAFTENGINE("CraftEngine"),
        // VANILLA(null);
    }

    private val enabledPlugins: Map<ItemType, Boolean> = ItemType.entries.associateWith { type ->
        Bukkit.getPluginManager().isPluginEnabled(type.pluginName)
    }

    fun create(id: String, type: ItemType): ItemStack? {
        if (enabledPlugins[type] != true) return null

        return when (type) {
            ItemType.ITEMSADDER -> CustomStack.getInstance("battlepass:$id")?.itemStack
            ItemType.NEXO -> NexoItems.itemFromId(id)?.build()
            ItemType.CRAFTENGINE -> CraftEngineItems.byId(Key.of(id))?.buildItemStack()
        }
    }

    fun encode(itemStack: ItemStack): String {
        return String(Base64Coder.encode(itemStack.serializeAsBytes()))
    }

    fun decode(base64: String): ItemStack {
        return ItemStack.deserializeBytes(Base64Coder.decode(base64))
    }

    object items {
        private val type by lazy {
            runCatching { ItemType.valueOf(configData.items.type.uppercase()) }
                .getOrDefault(ItemType.ITEMSADDER)
        }

        val previousArrow by lazy { create("previous", type) }
        val nextArrow by lazy { create("next", type) }
        val expLeft by lazy { create("exp-left", type) }
        val expMiddle by lazy { create("exp-middle", type) }
        val expRight by lazy { create("exp-right", type) }
        val locked by lazy { create("locked", type) }
        val unlocked by lazy { create("unlocked", type) }
        val info by lazy { create("info_pass", type) }
        val claimed by lazy { create("reward_claimed", type) }
    }
}