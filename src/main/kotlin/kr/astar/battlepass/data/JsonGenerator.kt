package kr.astar.battlepass.data

@DslMarker
annotation class JsonDsl

@JsonDsl
class JsonObject {
    private val map = linkedMapOf<String, Any?>()

    infix fun String.to(value: Any?) {
        map[this] = value
    }

    fun obj(key: String, block: JsonObject.() -> Unit) {
        map[key] = JsonObject().apply(block).build()
    }

    fun array(key: String, vararg values: Any?) {
        map[key] = values.toList()
    }

    fun build(): Map<String, Any?> = map
}

fun json(block: JsonObject.() -> Unit): Map<String, Any?> {
    return JsonObject().apply(block).build()
}

fun Any?.toJson(): String = when (this) {
    null -> "null"
    is String -> "\"$this\""
    is Number, is Boolean -> toString()
    is Map<*, *> -> entries.joinToString(
        prefix = "{",
        postfix = "}"
    ) { "\"${it.key}\":${it.value.toJson()}" }
    is Iterable<*> -> joinToString(
        prefix = "[",
        postfix = "]"
    ) { it.toJson() }
    else -> "\"$this\""
}

