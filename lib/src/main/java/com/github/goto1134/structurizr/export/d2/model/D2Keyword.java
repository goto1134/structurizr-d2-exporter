package com.github.goto1134.structurizr.export.d2.model;

/**
 * D2 object keyword
 */
public enum D2Keyword {

    LABEL("label"),
    /**
     * {@link D2Shape}
     */
    SHAPE("shape"),
    /**
     * Map of {@link D2StyleKeyword} to values
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
     * {@link D2Direction}
     */
    DIRECTION("direction");

    private final String name;

    D2Keyword(String name) {
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
