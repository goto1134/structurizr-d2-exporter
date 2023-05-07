package io.github.goto1134.structurizr.export.d2.model

/**
 * Represents different types of D2 relationship connections
 *
 * @see [D2 Connections](https://d2lang.com/tour/connections/)
 */
enum class D2Connection(private val value: String) {
    WITHOUT_DIRECTION("--"),
    DIRECT("->"),
    REVERSE("<-"),
    BIDIRECTIONAL("<->");

    override fun toString() = value
}
