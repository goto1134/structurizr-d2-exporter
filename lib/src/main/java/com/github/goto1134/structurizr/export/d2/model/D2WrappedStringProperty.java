package com.github.goto1134.structurizr.export.d2.model;

import com.structurizr.export.IndentingWriter;

public class D2WrappedStringProperty<K> extends D2Property<K, String> {

    public D2WrappedStringProperty(K keyword, String value) {
        super(keyword, value);
    }

    public void write(IndentingWriter writer) {
        writer.writeLine(String.format("%s: \"%s\"", keyword, value));
    }
}
