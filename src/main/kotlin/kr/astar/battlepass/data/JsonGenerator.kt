package kr.astar.battlepass.data

import com.google.gson.*

@DslMarker
annotation class GsonDsl

@GsonDsl
class JsonObjectDsl {
    val obj = JsonObject()

    infix fun String.to(value: Any?) {
        obj.add(this, value.toJsonElement())
    }

    fun obj(key: String, block: JsonObjectDsl.() -> Unit) {
        obj.add(key, JsonObjectDsl().apply(block).obj)
    }

    fun arr(key: String, block: JsonArrayDsl.() -> Unit) {
        obj.add(key, JsonArrayDsl().apply(block).arr)
    }
}

@GsonDsl
class JsonArrayDsl {
    val arr = JsonArray()

    operator fun Any?.unaryPlus() {
        arr.add(this.toJsonElement())
    }
}

fun json(block: JsonObjectDsl.() -> Unit): JsonObject {
    return JsonObjectDsl().apply(block).obj
}


fun Any?.toJsonElement(): JsonElement = when (this) {
    null -> JsonNull.INSTANCE
    is JsonElement -> this
    is String -> JsonPrimitive(this)
    is Number -> JsonPrimitive(this)
    is Boolean -> JsonPrimitive(this)
    else -> JsonPrimitive(toString())
}

// TEST/EXAMPLE
private val json = json {
    "test" to "test1"
    "enabled" to true
    arr("array!") {
        + "first"
        + "second"
    }
    obj("anyObject") {
        "oooo" to "aa"
        "bb" to "az"
    }
}
