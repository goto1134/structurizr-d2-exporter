package com.github.goto1134.structurizr.export.d2.model;

/**
 * Representd different types of D2 relationship connections
 *
 * @see <a href="https://d2lang.com/tour/connections">D2 Connections</a>
 */
public enum D2Connection {
    WITHOUT_DIRECTION("--"),
    DIRECT("->"),
    REVERSE("<-"),
    BIDIRECTIONAL("<->>");
    private final String value;

    D2Connection(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
