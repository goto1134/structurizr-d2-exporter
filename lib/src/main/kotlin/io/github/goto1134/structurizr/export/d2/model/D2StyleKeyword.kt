package io.github.goto1134.structurizr.export.d2.model

/**
 * @see [D2 Style](https://d2lang.com/tour/style)
 */
@Suppress("unused")
enum class D2StyleKeyword(private val value: String) {
    FILL_COLOR("fill"),
    FILL_PATTERN("fill-pattern"),
    STROKE_COLOR("stroke"),
    STROKE_WIDTH("stroke-width"),
    STROKE_DASH("stroke-dash"),
    BORDER_RADIUS("border-radius"),
    OPACITY("opacity"),
    TEXT_FONT("font"),
    TEXT_FONT_SIZE("font-size"),
    TEXT_FONT_COLOR("font-color"),
    TEXT_BOLD("bold"),
    TEXT_ITALIC("italic"),
    TEXT_UNDERLINE("underline"),

    SHAPE_SHADOW("shadow"),
    SHAPE_MULTIPLE("multiple"),
    SQUARE_IS_3D("3d"),

    CONNECTION_ANIMATED("animated"),
    CONNECTION_FILLED("filled");

    override fun toString() = value
}
