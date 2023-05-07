package io.github.goto1134.structurizr.export.d2.model

/**
 * The direction of the diagram elements arrangement
 *
 * @see [D2 Direction](https://d2lang.com/tour/layouts/#direction)
 */
enum class D2Direction(private val value: String) {
    BOTTOM_TO_TOP("up"),
    TOP_TO_BOTTOM("down"),
    RIGHT_TO_LEFT("left"),
    LEFT_TO_RIGHT("right");

    override fun toString() = value
}
