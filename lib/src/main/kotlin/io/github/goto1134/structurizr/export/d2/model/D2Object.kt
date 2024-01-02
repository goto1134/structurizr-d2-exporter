package io.github.goto1134.structurizr.export.d2.model

import com.structurizr.export.IndentingWriter


fun IndentingWriter.indented(block: IndentingWriter.() -> Unit) {
    indent()
    block()
    outdent()
}

/**
 * @param K D2 object property name
 * @param V D2 object property value
 */
open class D2Property<K, V>(@JvmField val keyword: K, @JvmField protected val value: V) {
    open fun write(writer: IndentingWriter) = writer.writeLine("$keyword: $value")
}

class D2WrappedStringProperty<K>(keyword: K, value: String) : D2Property<K, String>(keyword, value) {
    override fun write(writer: IndentingWriter) {
        writer.writeLine("$keyword: \"$value\"")
    }
}

sealed class D2Object(
    protected val properties: List<D2Property<D2Keyword, *>>, protected val style: List<D2Property<D2StyleKeyword, *>>
) {
    open fun writeObject(writer: IndentingWriter, writeInside: (IndentingWriter) -> Unit = { }) {
        properties.write(writer)
        if (style.isNotEmpty()) {
            writer.writeLine("${D2Keyword.STYLE}: {")
            writer.indented {
                style.write(writer)
            }
            writer.writeLine("}")
        }
        writeInside(writer)
    }

    private fun List<D2Property<*, *>>.write(writer: IndentingWriter) {
        for (property in sortedBy { it.keyword.toString() }) {
            property.write(writer)
        }
    }
}

class GlobalObject private constructor(
    properties: List<D2Property<D2Keyword, *>>, style: List<D2Property<D2StyleKeyword, *>>
) : D2Object(properties, style) {
    companion object {
        fun build(block: PropertyBuilder.() -> Unit = {}) = with(PropertyBuilder().apply(block)) {
            GlobalObject(properties, style)
        }
    }
}

open class NamedObject protected constructor(
    protected val name: String, properties: List<D2Property<D2Keyword, *>>, style: List<D2Property<D2StyleKeyword, *>>
) : D2Object(properties, style) {
    companion object {
        fun build(name: String, block: PropertyBuilder.() -> Unit = {}) = with(PropertyBuilder().apply(block)) {
            NamedObject(name, properties, style)
        }
    }

    open fun openObject(writer: IndentingWriter) {
        writer.writeLine("$name: {")
    }

    override fun writeObject(writer: IndentingWriter, writeInside: (IndentingWriter) -> Unit) {
        openObject(writer)
        writer.indented {
            super.writeObject(writer, writeInside)
        }
        writer.writeLine("}")
    }
}

class TextObject private constructor(
    name: String,
    private val language: String,
    private val text: String,
    properties: List<D2Property<D2Keyword, *>>,
    style: List<D2Property<D2StyleKeyword, *>>
) : NamedObject(name, properties, style) {

    companion object {
        fun build(name: String, language: String, text: String, block: PropertyBuilder.() -> Unit = {}) =
            with(PropertyBuilder().apply(block)) {
                TextObject(name, language, text, properties, style)
            }

        val UNICODE_NEWLINE_PATTERN = Regex("\\R")
    }

    override fun openObject(writer: IndentingWriter) {
        writer.writeLine("$name: |`$language")
        writer.indented {
            text.splitToSequence(UNICODE_NEWLINE_PATTERN).forEach(::writeLine)
        }
        writer.writeLine("`| {")
    }
}

class PropertyBuilder {
    companion object {
        const val STROKE_DASHED = 5
        const val STROKE_DOTTED = 2
    }

    internal val properties: MutableList<D2Property<D2Keyword, *>> = ArrayList()
    internal val style: MutableList<D2Property<D2StyleKeyword, *>> = ArrayList()

    fun label(label: String) = apply {
        properties.add(D2WrappedStringProperty(D2Keyword.LABEL, label))
    }

    fun shape(shape: D2Shape?) = apply {
        if (shape != null) properties.add(D2Property(D2Keyword.SHAPE, shape))
    }

    fun icon(icon: String?) = apply {
        if (!icon.isNullOrBlank()) properties.add(D2WrappedStringProperty(D2Keyword.ICON, icon))
    }

    fun link(link: String?) = apply {
        if (!link.isNullOrBlank()) properties.add(D2WrappedStringProperty(D2Keyword.LINK, link))
    }

    fun tooltip(tooltip: String?) = apply {
        if (!tooltip.isNullOrBlank()) properties.add(D2WrappedStringProperty(D2Keyword.TOOLTIP, tooltip))
    }

    fun fill(fill: String) = apply {
        style.add(D2WrappedStringProperty(D2StyleKeyword.FILL_COLOR, fill))
    }

    fun fillPattern(fillPattern: D2FillPattern?) = apply {
        if (fillPattern != null) style.add(D2Property(D2StyleKeyword.FILL_PATTERN, fillPattern))
    }

    fun direction(direction: D2Direction?) = apply {
        if (direction != null) properties.add(D2Property(D2Keyword.DIRECTION, direction))
    }

    fun stroke(stroke: String) = apply {
        style.add(D2WrappedStringProperty(D2StyleKeyword.STROKE_COLOR, stroke))
    }

    fun strokeWidth(width: Int?) = apply {
        if (width != null) style.add(D2Property(D2StyleKeyword.STROKE_WIDTH, width))
    }

    fun dashed() = strokeDash(STROKE_DASHED)
    fun dotted() = strokeDash(STROKE_DOTTED)
    @Suppress("MemberVisibilityCanBePrivate")
    fun strokeDash(dash: Int) = apply {
        style.add(D2Property(D2StyleKeyword.STROKE_DASH, dash))
    }

    fun multiple(value: Boolean) = apply {
        style.add(D2Property(D2StyleKeyword.SHAPE_MULTIPLE, value))
    }

    fun fontColor(fontColor: String) = apply {
        style.add(D2WrappedStringProperty(D2StyleKeyword.TEXT_FONT_COLOR, fontColor))
    }

    fun fontSize(fontSize: Int) = apply {
        style.add(D2Property(D2StyleKeyword.TEXT_FONT_SIZE, fontSize))
    }

    fun opacity(opacity: Double) = apply {
        style.add(D2Property(D2StyleKeyword.OPACITY, opacity))
    }

    fun animated(animated: Boolean) = apply {
        if (animated) style.add(D2Property(D2StyleKeyword.CONNECTION_ANIMATED, true))
    }

    fun near(near: D2NearConstant) = apply {
        properties.add(D2Property(D2Keyword.NEAR, near.toString()))
    }
}
