package io.github.goto1134.structurizr.export.d2

import com.structurizr.export.Diagram
import com.structurizr.view.View

class D2Diagram(view: View, definition: String) : Diagram(view, definition) {
    override fun getFileExtension() = "d2"
}
