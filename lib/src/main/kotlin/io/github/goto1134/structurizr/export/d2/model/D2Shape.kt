package io.github.goto1134.structurizr.export.d2.model

/**
 * Shape of D2 object
 *
 * @see [D2 Shapes](https://d2lang.com/tour/shapes/)
 */
enum class D2Shape(private val value: String) {
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

    override fun toString() = value
}
