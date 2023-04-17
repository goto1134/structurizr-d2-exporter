package io.github.goto1134.structurizr.export.d2.model

import com.structurizr.export.IndentingWriter
import io.github.goto1134.structurizr.export.d2.indented

class D2TextObject(
    name: String,
    private val language: String,
    private val text: String,
    properties: List<D2Property<D2Keyword, *>>,
    style: List<D2Property<D2StyleKeyword, *>>
) : D2Object(name, properties, style) {

    companion object {
        fun build(name: String, language: String, text: String, block: Builder.() -> Unit): D2TextObject {
            return Builder(name, language, text).apply(block).build()
        }
    }

    override fun openObject(writer: IndentingWriter) {
        writer.writeLine("$name: |`$language")
        writer.indented {
            text.splitToSequence("\\R".toRegex()).forEach {
                writeLine(it)
            }
        }
        writer.writeLine("`| {")
    }

    class Builder(private val name: String, private val language: String, private val text: String) {
        private val properties: MutableList<D2Property<D2Keyword, *>> = ArrayList()
        private val style: MutableList<D2Property<D2StyleKeyword, *>> = ArrayList()
        fun near(near: D2NearConstant): Builder = apply {
            properties.add(D2Property(D2Keyword.NEAR, near.toString()))
        }

        fun fontColor(fontColor: String?): Builder = apply {
            if (fontColor != null) style.add(D2WrappedStringProperty(D2StyleKeyword.TEXT_FONT_COLOR, fontColor))
        }

        fun fontSize(fontSize: Int): Builder = apply {
            style.add(D2Property(D2StyleKeyword.TEXT_FONT_SIZE, fontSize))
        }

        fun build(): D2TextObject {
            return D2TextObject(name, language, text, properties, style)
        }
    }
}
