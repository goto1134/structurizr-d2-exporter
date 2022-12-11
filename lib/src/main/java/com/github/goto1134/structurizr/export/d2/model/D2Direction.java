package com.github.goto1134.structurizr.export.d2.model;

/**
 * The direction of the diagram elements arrangement
 *
 * @see <a href="https://d2lang.com/tour/layouts#direction">D2 Direction</a>
 */
public enum D2Direction {
    BOTTOM_TO_TOP("up"),
    TOP_TO_BOTTOM("down"),
    RIGHT_TO_LEFT("left"),
    LEFT_TO_RIGHT("right");
    private final String name;

    D2Direction(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
