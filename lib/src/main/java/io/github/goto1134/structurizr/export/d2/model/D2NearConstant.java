package io.github.goto1134.structurizr.export.d2.model;

/**
 * Constants for positioning text in the diagram.
 *
 * @see <a href="https://d2lang.com/tour/text/#near-a-constant">D2 text positioning near a constant</a>
 */
public enum D2NearConstant {

    TOP_LEFT("top-left"),
    TOP_CENTER("top-center"),
    TOP_RIGHT("top-right"),
    CENTER_LEFT("center-left"),
    CENTER_RIGHT("center-right"),
    BOTTOM_LEFT("bottom-left"),
    BOTTOM_CENTER("bottom-center"),
    BOTTOM_RIGHT("bottom-right"),
;

    private final String name;

    D2NearConstant(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
