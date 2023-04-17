package io.github.goto1134.structurizr.export.d2

import com.structurizr.export.IndentingWriter

fun IndentingWriter.indented(block: IndentingWriter.() -> Unit) {
    indent()
    block()
    outdent()
}