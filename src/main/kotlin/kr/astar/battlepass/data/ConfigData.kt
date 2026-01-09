package kr.astar.battlepass.data

data class ConfigData(
    val gui: GUIData
)


data class GUIData(
    val title: String
)

@DslMarker
annotation class ConfigDsl

@ConfigDsl
class ConfigBuilder {
    private var guiBuilder: GUIBuilder? = null

    fun gui(block: GUIBuilder.() -> Unit) {
        guiBuilder = GUIBuilder().apply(block)
    }

    fun build(): ConfigData {
        val gui = guiBuilder?.build()
            ?: error("gui is required")
        return ConfigData(gui)
    }
}

@ConfigDsl
class GUIBuilder {
    var title: String = ""

    fun build(): GUIData {
        if (title.isBlank()) error("gui.title is null")
        return GUIData(title)
    }
}

fun config(block: ConfigBuilder.() -> Unit): ConfigData {
    return ConfigBuilder().apply(block).build()
}