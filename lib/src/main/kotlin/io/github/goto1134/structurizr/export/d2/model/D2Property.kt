package io.github.goto1134.structurizr.export.d2.model

import com.structurizr.export.IndentingWriter

/**
 * @param <K> – D2 object property name
 * @param <V> - D2 object property value
</V></K> */
open class D2Property<K, V>(@JvmField val keyword: K, @JvmField protected val value: V) {
    open fun write(writer: IndentingWriter) = writer.writeLine("$keyword: $value")
}
