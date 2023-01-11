package io.github.goto1134.structurizr.export.d2.model;

import com.structurizr.export.IndentingWriter;

import java.util.ArrayList;
import java.util.List;

public class D2TextObject extends D2Object {

    public static class Builder {
        private final String name;
        private final String language;
        private final String text;
        private final List<D2Property<D2Keyword, ?>> properties = new ArrayList<>();
        private final List<D2Property<D2StyleKeyword, ?>> style = new ArrayList<>();

        public Builder(String name, String language, String text) {
            this.name = name;
            this.language = language;
            this.text = text;
        }

        public Builder near(D2NearConstant near) {
            return near(near.toString());
        }

        public Builder near(String near) {
            properties.add(new D2Property<>(D2Keyword.NEAR, near));
            return this;
        }

        public Builder fontColor(String fontColor) {
            style.add(new D2WrappedStringProperty<>(D2StyleKeyword.TEXT_FONT_COLOR, fontColor));
            return this;
        }

        public Builder fontSize(Integer fontSize) {
            style.add(new D2Property<>(D2StyleKeyword.TEXT_FONT_SIZE, fontSize));
            return this;
        }

        public D2TextObject build() {
            return new D2TextObject(name, language, text, properties, style);
        }
    }

    public static Builder builder(String name, String language, String text) {
        return new Builder(name, language, text);
    }

    protected final String language;
    protected final String text;

    public D2TextObject(String name, String language, String text, List<D2Property<D2Keyword, ?>> properties, List<D2Property<D2StyleKeyword, ?>> style) {
        super(name, properties, style);
        this.language = language;
        this.text = text;
    }

    @Override
    public void writeHeader(IndentingWriter writer) {
        writer.writeLine(String.format("%s: |`%s", name, language));
        writer.indent();
        for (String line : text.split("\\R")) {
            writer.writeLine(line);
        }
        writer.outdent();
        writer.writeLine("`| {");
        writer.indent();
    }
}
