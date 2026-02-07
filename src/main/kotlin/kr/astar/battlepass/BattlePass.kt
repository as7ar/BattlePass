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
        val configData: ConfigData
            get() = config {
                gui {
                    title = BattlePass.plugin.config.getString("settings.gui.title") ?: ""
                    premiumTitle = BattlePass.plugin.config.getString("settings.gui.premium-title") ?: ""
                    rewardTitle = BattlePass.plugin.config.getString("settings.gui.reward-title") ?: ""
                }
                items {
                    type = BattlePass.plugin.config.getString("settings.item-plugin") ?: ""
                }
                pass {
                    levelingExp = BattlePass.plugin.config.getInt("settings.pass.leveling-exp")
                    maxLevel = BattlePass.plugin.config.getInt("settings.pass.max-level")
                }
            }
    }

    override fun onLoad() {
        plugin = this

        saveResource("reward.json", false)
        saveResource("reward-premium.json", false)
        saveDefaultConfig()
    }

    override fun onEnable() {

        server.pluginManager.registerEvents(PassGUIListener(), this)

        PassCommand().register(this.server.commandMap)
    }
}