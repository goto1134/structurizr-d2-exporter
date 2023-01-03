package io.github.goto1134.structurizr.export.d2.model;

/**
 * Shape of D2 object
 *
 * @see <a href="https://d2lang.com/tour/shapes">D2 Shapes</a>
 */
public enum D2Shape {

    RECTANGLE("rectangle"),
    SQUARE("square"),
    PAGE("page"),
    PARALLELOGRAM("parallelogram"),
    DOCUMENT("document"),
    CYLINDER("cylinder"),
    QUEUE("queue"),
    PACKAGE("package"),
    STEP("step"),
    CALLOUT("callout"),
    STORED_DATA("stored_data"),
    PERSON("person"),
    DIAMOND("diamond"),
    OVAL("oval"),
    CIRCLE("circle"),
    HEXAGON("hexagon"),
    CLOUD("cloud"),
    TEXT("text"),
    CODE("code"),
    CLASS("class"),
    SQL_TABLE("sql_table"),
    IMAGE("image"),
    SEQUENCE_DIAGRAM("sequence_diagram");

    private final String name;

    D2Shape(String name) {
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
