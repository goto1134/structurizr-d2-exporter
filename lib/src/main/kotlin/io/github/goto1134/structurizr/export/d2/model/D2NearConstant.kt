package io.github.goto1134.structurizr.export.d2.model

/**
 * Constants for positioning text in the diagram.
 *
 * @see [D2 text positioning near a constant](https://d2lang.com/tour/positions/#near)
 */
enum class D2NearConstant(private val value: String) {
    TOP_LEFT("top-left"),
    TOP_CENTER("top-center"),
    TOP_RIGHT("top-right"),
    CENTER_LEFT("center-left"),
    CENTER_RIGHT("center-right"),
    BOTTOM_LEFT("bottom-left"),
    BOTTOM_CENTER("bottom-center"),
    BOTTOM_RIGHT("bottom-right");

    override fun toString() = value

    companion object {
        fun get(value: String?) = entries.firstOrNull { it.value.equals(value, ignoreCase = true) }
        fun getOrDefault(value: String?, default: D2NearConstant) = get(value) ?: default
    }
}
