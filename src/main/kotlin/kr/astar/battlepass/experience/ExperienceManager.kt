package kr.astar.battlepass.experience

import kr.astar.battlepass.BattlePass
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import kotlin.math.abs
import kotlin.math.max

object ExperienceManager {
    private val plugin = BattlePass.plugin
    private val configData = BattlePass.configData

    private val datafolder = File(plugin.dataFolder, "/data")
    private val expFile= File(datafolder, "exp")
    private val expYaml
        get() = YamlConfiguration.loadConfiguration(expFile)

    init {
        if (!datafolder.exists()) datafolder.mkdirs()
        if (!expFile.exists()) expFile.createNewFile()
    }

    private val maxLv = configData.pass.maxLevel
    private val levelingExp = configData.pass.levelingExp

    fun getExp(offlinePlayer: OfflinePlayer) = expYaml.getInt("${offlinePlayer.uniqueId}.exp")
    fun getLv(offlinePlayer: OfflinePlayer) = expYaml.getInt("${offlinePlayer.uniqueId}.lv")

    private fun save(offlinePlayer: OfflinePlayer, exp: Int, lv: Int) {
        val yaml = expYaml
        val base = offlinePlayer.uniqueId.toString()
        yaml.set("$base.exp", exp)
        yaml.set("$base.lv", lv)
        yaml.save(expFile)
    }

    fun addExp(offlinePlayer: OfflinePlayer, amount: Int) {
        val exp= getExp(offlinePlayer)
        val lv = getLv(offlinePlayer)

        val totalExp = lv*levelingExp+exp+amount

        val finalExp = totalExp % levelingExp
        val finalLv = totalExp / levelingExp

        save(offlinePlayer, finalExp, finalLv)
    }

    fun takeExp(offlinePlayer: OfflinePlayer, amount: Int) {
        val exp= getExp(offlinePlayer)
        val lv = getLv(offlinePlayer)

        var finalExp=0
        var finalLv=0

        val totalExp = lv*levelingExp+exp
        val afterTotalExp = totalExp - amount
        if (afterTotalExp<=0) {
            save(offlinePlayer, 0, 0)
            return
        }

        finalExp = totalExp % levelingExp
        finalLv = totalExp / levelingExp

        save(offlinePlayer, finalExp, finalLv)
    }
}