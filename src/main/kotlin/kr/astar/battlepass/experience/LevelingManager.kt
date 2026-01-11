package kr.astar.battlepass.experience

import kr.astar.battlepass.BattlePass
import org.bukkit.OfflinePlayer
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

object LevelingManager {
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

        var finalExp = totalExp % levelingExp
        var finalLv = totalExp / levelingExp

        if (finalLv>=maxLv) {
            finalLv = maxLv
            finalExp=0
        }

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

    private val rewardSlotArray = arrayOf(1, 3, 5, 7, 18, 20, 22, 24, 26) // total: 9
    fun expSlotGenerator(offlinePlayer: OfflinePlayer, page: Int): Array<Int> {
        val np= numberOfPassPageGenerator(offlinePlayer)
        if (page<np) return rewardSlotArray

        //todo: complete
        return rewardSlotArray
    }

    fun numberOfPassPageGenerator(offlinePlayer: OfflinePlayer): Int {
        val lv=getLv(offlinePlayer)
        var page=0
        page+=lv/9
        if (lv%9!=0) page+=1
        return page
    }
}