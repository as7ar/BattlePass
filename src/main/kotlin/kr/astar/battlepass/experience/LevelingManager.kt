package kr.astar.battlepass.experience

import kr.astar.battlepass.BattlePass
import kr.astar.battlepass.data.PassType
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

    fun getExp(offlinePlayer: OfflinePlayer, type: PassType= PassType.DEFAULT) = expYaml.getInt("${offlinePlayer.uniqueId}.${type.name.lowercase()}.exp")
    fun getLv(offlinePlayer: OfflinePlayer, type: PassType= PassType.DEFAULT) = expYaml.getInt("${offlinePlayer.uniqueId}.${type.name.lowercase()}.lv")

    private fun save(offlinePlayer: OfflinePlayer, exp: Int, lv: Int, type: PassType= PassType.DEFAULT) {
        val yaml = expYaml
        val base = "${offlinePlayer.uniqueId}.${type.name.lowercase()}"
        yaml.set("$base.exp", exp)
        yaml.set("$base.lv", lv)
        yaml.save(expFile)
    }

    fun addExp(offlinePlayer: OfflinePlayer, amount: Int, type: PassType= PassType.DEFAULT) {
        val exp= getExp(offlinePlayer, type)
        val lv = getLv(offlinePlayer, type)

        val totalExp = lv*levelingExp+exp+amount

        var finalExp = totalExp % levelingExp
        var finalLv = totalExp / levelingExp

        if (finalLv>=maxLv) {
            finalLv = maxLv
            finalExp=0
        }

        save(offlinePlayer, finalExp, finalLv, type)
    }

    fun takeExp(offlinePlayer: OfflinePlayer, amount: Int, type: PassType= PassType.DEFAULT) {
        val exp= getExp(offlinePlayer, type)
        val lv = getLv(offlinePlayer, type)

        var finalExp=0
        var finalLv=0

        val totalExp = lv*levelingExp+exp
        val afterTotalExp = totalExp - amount

        if (afterTotalExp<=0) {
            save(offlinePlayer, 0, 0, type)
            return
        }

        finalExp = totalExp % levelingExp
        finalLv = totalExp / levelingExp

        save(offlinePlayer, finalExp, finalLv, type)
    }

    private val rewardSlotArray = arrayOf(18, 1, 20, 3, 22, 5, 24, 7, 26) // total: 9
    fun rewardSlotGenerator(offlinePlayer: OfflinePlayer, page: Int, type: PassType= PassType.DEFAULT): Array<Int> {
        val np= numberOfPassPageGenerator(offlinePlayer, type)
        if (page<np) return rewardSlotArray
        if (page>np) return arrayOf()

        val lvSlots=getLv(offlinePlayer, type) % rewardSlotArray.size
        val slotArray= rewardSlotArray.take(lvSlots).toTypedArray()

        return slotArray
    }

    fun numberOfPassPageGenerator(offlinePlayer: OfflinePlayer, type: PassType= PassType.DEFAULT): Int {
        val lv=getLv(offlinePlayer, type)
        var page = lv / rewardSlotArray.size
        if (lv%rewardSlotArray.size != 0) page+=1
        if (page==0) return 1
        return page
    }
}