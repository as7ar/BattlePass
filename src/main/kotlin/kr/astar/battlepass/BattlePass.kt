package kr.astar.battlepass

import kr.astar.battlepass.commands.PassCommand
import kr.astar.battlepass.data.ConfigData
import kr.astar.battlepass.data.config
import kr.astar.battlepass.gui.PassGUI
import kr.astar.battlepass.gui.PassGUIListener
import org.bukkit.plugin.java.JavaPlugin

class BattlePass : JavaPlugin() {
    companion object {
        lateinit var plugin: BattlePass
            private set
        lateinit var configData: ConfigData
    }

    override fun onLoad() {
        plugin = this
        saveDefaultConfig()
        reloadConfigData()
    }

    override fun onEnable() {

        server.pluginManager.registerEvents(PassGUIListener(), this)

        PassCommand().register(
            this.server.commandMap
        )
    }

    fun reloadConfigData() {
        configData = config {
            gui {
                title = config.getString("gui.title") ?: ""
                premiumTitle = config.getString("gui.premium-title") ?: ""
            }
            items {
                type = config.getString("items.type") ?: ""
            }
            command {
                name = config.getString("commands.name") ?: ""
                aliases = config.getStringList("commands.aliases")
                description = config.getString("commands.description") ?: ""
            }
            pass {
                levelingExp = config.getInt("pass.leveling-exp")
                maxLevel = config.getInt("pass.max-level")
            }
        }
    }
}