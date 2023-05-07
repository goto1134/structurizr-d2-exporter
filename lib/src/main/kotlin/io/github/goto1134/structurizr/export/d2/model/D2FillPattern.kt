package io.github.goto1134.structurizr.export.d2.model

/**
 * The direction of the diagram elements arrangement
 *
 * @see [D2 Fill Pattern](https://d2lang.com/tour/style/#fill-pattern)
 */
enum class D2FillPattern(private val value: String) {
    DOTS("dots"),
    LINES("lines"),
    GRAIN("grain");

    override fun toString() = value

    companion object {
        fun get(value: String?) = D2FillPattern.values().firstOrNull { it.value.equals(value, ignoreCase = true) }
    }
}
