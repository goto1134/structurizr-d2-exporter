package io.github.goto1134.structurizr.export.d2.model

/**
 * D2 object keyword
 */
@Suppress("unused")
enum class D2Keyword(private val value: String) {
    LABEL("label"),

    /**
     * [D2Shape]
     */
    SHAPE("shape"),

    /**
     * Map of [D2StyleKeyword] to values
     */
    STYLE("style"),

    /**
     * URL
     */
    ICON("icon"),
    TOOLTIP("tooltip"),
    LINK("link"),
    NEAR("near"),
    IMAGE_SHAPE_WIDTH("width"),
    IMAGE_SHAPE_HEIGHT("height"),

    /**
     * [D2Direction]
     */
    DIRECTION("direction");

    override fun toString() = value
}
