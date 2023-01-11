package io.github.goto1134.structurizr.export.d2.model;

import com.structurizr.export.IndentingWriter;

import java.util.*;

public class D2Object {

    public static class Builder {

        public static final int STROKE_DASHED = 5;
        public static final int STROKE_DOTTED = 2;

        private final String name;
        private final List<D2Property<D2Keyword, ?>> properties = new ArrayList<>();
        private final List<D2Property<D2StyleKeyword, ?>> style = new ArrayList<>();

        public Builder(String name) {
            this.name = name;
        }

        public Builder label(String label) {
            properties.add(new D2WrappedStringProperty<>(D2Keyword.LABEL, label));
            return this;
        }

        public Builder shape(D2Shape shape) {
            properties.add(new D2Property<>(D2Keyword.SHAPE, shape));
            return this;
        }

        public Builder icon(Optional<String> icon) {
            icon.filter(it -> !it.isEmpty())
                    .ifPresent(it -> properties.add(new D2WrappedStringProperty<>(D2Keyword.ICON, it)));
            return this;
        }

        public Builder link(Optional<String> link) {
            link.filter(it -> !it.isEmpty())
                    .ifPresent(it -> properties.add(new D2WrappedStringProperty<>(D2Keyword.LINK, it)));
            return this;
        }

        public Builder tooltip(String tooltip) {
            properties.add(new D2WrappedStringProperty<>(D2Keyword.TOOLTIP, tooltip));
            return this;
        }

        public Builder fill(String fill) {
            style.add(new D2WrappedStringProperty<>(D2StyleKeyword.FILL_COLOR, fill));
            return this;
        }

        public Builder stroke(String stroke) {
            style.add(new D2WrappedStringProperty<>(D2StyleKeyword.STROKE_COLOR, stroke));
            return this;
        }

        public Builder strokeWidth(Optional<Integer> widthOptional) {
            if (widthOptional.isPresent()) {
                return strokeWidth(widthOptional.get());
            }
            return this;
        }

        public Builder strokeWidth(Integer width) {
            style.add(new D2Property<>(D2StyleKeyword.STROKE_WIDTH, width));
            return this;
        }

        public Builder strokeDash(Integer dash) {
            style.add(new D2Property<>(D2StyleKeyword.STROKE_DASH, dash));
            return this;
        }

        public Builder dashed() {
            return strokeDash(STROKE_DASHED);
        }

        public Builder dotted() {
            return strokeDash(STROKE_DOTTED);
        }

        public Builder multiple(boolean value) {
            style.add(new D2Property<>(D2StyleKeyword.SHAPE_MULTIPLE, value));
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

        public Builder opacity(Double opacity) {
            style.add(new D2Property<>(D2StyleKeyword.OPACITY, opacity));
            return this;
        }

        public Builder withGroupStyle() {
            return fill("white").stroke("black");
        }

        public D2Object build() {
            return new D2Object(name, properties, style);
        }
    }

    public static Builder builder(String name) {
        return new Builder(name);
    }

    protected final String name;
    protected final List<D2Property<D2Keyword, ?>> properties;
    protected final List<D2Property<D2StyleKeyword, ?>> style;

    public D2Object(String name, List<D2Property<D2Keyword, ?>> properties, List<D2Property<D2StyleKeyword, ?>> style) {
        this.name = name;
        this.properties = Collections.unmodifiableList(properties);
        this.style = Collections.unmodifiableList(style);
    }

    public void startObject(IndentingWriter writer) {
        writeHeader(writer);
        writeProperties(writer);
    }

    public void writeHeader(IndentingWriter writer) {
        writer.writeLine(String.format("%s: {", name));
        writer.indent();
    }

    private void writeProperties(IndentingWriter writer) {
        properties.stream().sorted(Comparator.comparing(it -> it.getKeyword().toString())).forEach(p -> p.write(writer));
        writer.writeLine(String.format("%s: {", D2Keyword.STYLE));
        writer.indent();
        style.stream().sorted(Comparator.comparing(it -> it.getKeyword().toString())).forEach(p -> p.write(writer));
        endObject(writer);
    }

    public void writeObject(IndentingWriter writer) {
        startObject(writer);
        endObject(writer);
    }

    public static void endObject(IndentingWriter writer) {
        writer.outdent();
        writer.writeLine("}");
    }
}
