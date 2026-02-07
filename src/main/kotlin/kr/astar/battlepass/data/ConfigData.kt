package kr.astar.battlepass.data

/* =========================
   Data classes
   ========================= */

data class ConfigData(
    val gui: GUIData,
    val items: ItemsData,
    val pass: PassData
)

data class GUIData(
    val title: String,
    val premiumTitle: String,
    val rewardTitle: String
)

data class ItemsData(
    val type: String
)

data class PassData(
    val levelingExp: Int,
    val maxLevel: Int
)

/* =========================
   DSL marker
   ========================= */

@DslMarker
annotation class ConfigDsl

/* =========================
   Root builder
   ========================= */

@ConfigDsl
class ConfigBuilder {
    private var guiBuilder: GUIBuilder? = null
    private var itemsBuilder: ItemsBuilder? = null
    private var passBuilder: PassBuilder? = null

    fun gui(block: GUIBuilder.() -> Unit) {
        guiBuilder = GUIBuilder().apply(block)
    }

    fun items(block: ItemsBuilder.() -> Unit) {
        itemsBuilder = ItemsBuilder().apply(block)
    }

    fun pass(block: PassBuilder.() -> Unit) {
        passBuilder = PassBuilder().apply(block)
    }

    fun build(): ConfigData {
        val gui = guiBuilder?.build() ?: error("gui is required")
        val items = itemsBuilder?.build() ?: error("items is required")
        val pass = passBuilder?.build() ?: error("pass is required")
        return ConfigData(gui, items, pass)
    }
}

/* =========================
   GUI
   ========================= */

@ConfigDsl
class GUIBuilder {
    var title: String = ""
    var premiumTitle: String = ""
    var rewardTitle: String = ""

    fun build(): GUIData {
        return GUIData(title, premiumTitle, rewardTitle)
    }
}

/* =========================
   Items
   ========================= */

@ConfigDsl
class ItemsBuilder {
    var type: String = ""

    fun build(): ItemsData {
        if (type.isBlank()) error("items.type is blank")
        return ItemsData(type)
    }
}

/* =========================
   Pass
   ========================= */

@ConfigDsl
class PassBuilder {
    var levelingExp: Int = 0
    var maxLevel: Int = 0

    fun build(): PassData {
        if (levelingExp <= 0) error("pass.leveling-exp must be > 0")
        if (maxLevel <= 0) error("pass.max-level must be > 0")
        return PassData(levelingExp, maxLevel)
    }
}

/* =========================
   Entry function
   ========================= */

fun config(block: ConfigBuilder.() -> Unit): ConfigData {
    return ConfigBuilder().apply(block).build()
}
