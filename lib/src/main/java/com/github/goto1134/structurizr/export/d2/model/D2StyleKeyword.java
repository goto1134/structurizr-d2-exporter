package com.github.goto1134.structurizr.export.d2.model;

/**
 * @see <a href="https://d2lang.com/tour/style">D2 Style</a>
 */
public enum D2StyleKeyword {
    FILL_COLOR("fill"),
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

    private final String name;

    D2StyleKeyword(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
