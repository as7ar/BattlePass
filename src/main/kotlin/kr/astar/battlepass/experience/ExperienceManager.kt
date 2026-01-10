package kr.astar.battlepass.experience

import kr.astar.battlepass.BattlePass
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object ExperienceManager {
    private val plugin = BattlePass.plugin
    private val datafolder = File(plugin.dataFolder, "/data")
    private val expFile= File(datafolder, "exp")
    private val expYaml
        get() = YamlConfiguration.loadConfiguration(expFile)


}