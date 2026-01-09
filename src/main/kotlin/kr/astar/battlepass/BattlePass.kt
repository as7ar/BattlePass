package kr.astar.battlepass

import kr.astar.battlepass.data.ConfigData
import kr.astar.battlepass.data.config
import org.bukkit.plugin.java.JavaPlugin

class BattlePass : JavaPlugin() {
    companion object {
        lateinit var plugin: BattlePass
            private set
        lateinit var configData: ConfigData
            private set
    }

    override fun onLoad() {
        plugin = this
        saveDefaultConfig()
    }

    override fun onEnable() {
        configData = config {
            gui {
                title = this@BattlePass.config.getString("gui.title") ?: ""
            }
        }
    }
}