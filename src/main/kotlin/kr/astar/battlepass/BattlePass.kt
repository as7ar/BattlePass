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

        saveResource("reward.json", false)
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
                title = config.getString("settings.gui.title") ?: ""
                premiumTitle = config.getString("settings.gui.premium-title") ?: ""
                rewardTitle = config.getString("settings.gui.reward-title") ?: ""
            }
            items {
                type = config.getString("settings.item-plugin") ?: ""
            }
            command {
                name = config.getString("commands.name") ?: ""
                aliases = config.getStringList("commands.aliases")
                description = config.getString("commands.description") ?: ""
            }
            pass {
                levelingExp = config.getInt("settings.pass.leveling-exp")
                maxLevel = config.getInt("settings.pass.max-level")
            }
        }
    }
}