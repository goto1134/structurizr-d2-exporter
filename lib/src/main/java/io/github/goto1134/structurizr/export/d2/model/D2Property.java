package io.github.goto1134.structurizr.export.d2.model;

import com.structurizr.export.IndentingWriter;

/**
 * @param <K> – D2 object property name
 * @param <V> - D2 object property value
 */
public class D2Property<K, V> {

    protected final K keyword;
    protected final V value;

    public D2Property(K keyword, V value) {
        this.keyword = keyword;
        this.value = value;
    }

    public void write(IndentingWriter writer) {
        writer.writeLine(String.format("%s: %s", keyword, value));
    }

    public K getKeyword() {
        return keyword;
    }
}
