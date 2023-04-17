package io.github.goto1134.structurizr.export.d2.model

import com.structurizr.export.IndentingWriter

class D2WrappedStringProperty<K>(keyword: K, value: String) : D2Property<K, String>(keyword, value) {
    override fun write(writer: IndentingWriter) {
        writer.writeLine("$keyword: \"$value\"")
    }
}
