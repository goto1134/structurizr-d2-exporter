package io.github.goto1134.structurizr.export.d2.model

import com.structurizr.export.IndentingWriter
import io.github.goto1134.structurizr.export.d2.indented

open class D2Object(
    @JvmField protected val name: String,
    protected val properties: List<D2Property<D2Keyword, *>>,
    protected val style: List<D2Property<D2StyleKeyword, *>>
) {

    companion object {
        fun build(name: String, block: Builder.() -> Unit): D2Object {
            return Builder(name).apply(block).build()
        }
    }

    open fun openObject(writer: IndentingWriter) {
        writer.writeLine("$name: {")
    }

    private fun writeProperties(writer: IndentingWriter) {
        properties.write(writer)
        writer.writeLine("${D2Keyword.STYLE}: {")
        writer.indented {
            style.write(writer)
        }
        writer.writeLine("}")
    }

    private fun List<D2Property<*, *>>.write(writer: IndentingWriter) {
        for (property in sortedBy { it.keyword.toString() }) {
            property.write(writer)
        }
    }

    fun writeObject(writer: IndentingWriter) {
        openObject(writer)
        writer.indented {
            writeProperties(writer)
        }
        writer.writeLine("}")
    }

    class Builder(private val name: String) {
        companion object {
            const val STROKE_DASHED = 5
            const val STROKE_DOTTED = 2
        }

        private val properties: MutableList<D2Property<D2Keyword, *>> = ArrayList()
        private val style: MutableList<D2Property<D2StyleKeyword, *>> = ArrayList()

        fun label(label: String): Builder = apply {
            properties.add(D2WrappedStringProperty(D2Keyword.LABEL, label))
        }

        fun shape(shape: D2Shape): Builder = apply {
            properties.add(D2Property(D2Keyword.SHAPE, shape))
        }

        fun icon(icon: String?): Builder = apply {
            if (!icon.isNullOrBlank()) {
                properties.add(D2WrappedStringProperty(D2Keyword.ICON, icon))
            }
        }

        fun link(link: String?): Builder = apply {
            if (!link.isNullOrBlank()) {
                properties.add(D2WrappedStringProperty(D2Keyword.LINK, link))
            }
        }

        fun tooltip(tooltip: String?): Builder = apply {
            if (!tooltip.isNullOrBlank()) {
                properties.add(D2WrappedStringProperty(D2Keyword.TOOLTIP, tooltip))
            }
        }

        fun fill(fill: String): Builder = apply {
            style.add(D2WrappedStringProperty(D2StyleKeyword.FILL_COLOR, fill))
        }

        fun stroke(stroke: String): Builder = apply {
            style.add(D2WrappedStringProperty(D2StyleKeyword.STROKE_COLOR, stroke))
        }

        fun strokeWidth(width: Int?): Builder = apply {
            if (width != null) style.add(D2Property(D2StyleKeyword.STROKE_WIDTH, width))
        }

        fun dashed(): Builder = strokeDash(STROKE_DASHED)

        fun dotted(): Builder = strokeDash(STROKE_DOTTED)

        fun strokeDash(dash: Int): Builder = apply {
            style.add(D2Property(D2StyleKeyword.STROKE_DASH, dash))
        }


        fun multiple(value: Boolean): Builder = apply {
            style.add(D2Property(D2StyleKeyword.SHAPE_MULTIPLE, value))
        }

        fun fontColor(fontColor: String): Builder = apply {
            style.add(D2WrappedStringProperty(D2StyleKeyword.TEXT_FONT_COLOR, fontColor))
        }

        fun fontSize(fontSize: Int): Builder = apply {
            style.add(D2Property(D2StyleKeyword.TEXT_FONT_SIZE, fontSize))
        }

        fun opacity(opacity: Double): Builder = apply {
            style.add(D2Property(D2StyleKeyword.OPACITY, opacity))
        }

        fun withGroupStyle(): Builder {
            return fill("white").stroke("black")
        }

        fun build(): D2Object {
            return D2Object(name, properties, style)
        }
    }
}
