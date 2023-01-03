package io.github.goto1134.structurizr.export.d2;

import com.structurizr.export.Diagram;
import com.structurizr.view.View;

public class D2Diagram extends Diagram {

    public D2Diagram(View view, String definition) {
        super(view, definition);
    }

    @Override
    public String getFileExtension() {
        return "d2";
    }
}
