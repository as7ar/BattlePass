package kr.astar.battlepass.data

import kr.astar.battlepass.BattlePass
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.util.*

class UserData(
    val uuid: UUID
) {
    private val plugin= BattlePass.plugin
    private val file= File(plugin.dataFolder, "/userdata/${uuid}.yml")
    var config = YamlConfiguration.loadConfiguration(file)

    fun reload() {
        config = YamlConfiguration.loadConfiguration(file)
    }

    fun save() {
        config.save(file)
    }

    fun setValue(path: String, value: Any) {
        config.set(path, value)
        save()
    }
}