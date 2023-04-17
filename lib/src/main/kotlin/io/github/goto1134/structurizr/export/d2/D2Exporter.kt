package io.github.goto1134.structurizr.export.d2

import com.structurizr.export.AbstractDiagramExporter
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.model.Container
import com.structurizr.model.DeploymentNode
import com.structurizr.model.Element
import com.structurizr.model.SoftwareSystem
import com.structurizr.view.*
import io.github.goto1134.structurizr.export.d2.model.D2Object
import io.github.goto1134.structurizr.export.d2.model.D2TextObject

class D2Exporter : AbstractDiagramExporter() {

    companion object {
        const val D2_TITLE_POSITION = "d2.title_position"
    }

    override fun writeHeader(view: ModelView, writer: IndentingWriter) {
        D2TextObject.build("title", "md", "# ${view.d2Title}") {
            near(view.d2TitlePosition)
        }.writeObject(writer)
        val d2Direction = view.automaticLayout?.getD2Direction()
        d2Direction?.toD2Property()?.write(writer)
    }

    override fun writeFooter(view: ModelView, writer: IndentingWriter) = Unit
    override fun startEnterpriseBoundary(view: ModelView, enterpriseName: String, writer: IndentingWriter) = Unit
    override fun endEnterpriseBoundary(view: ModelView, writer: IndentingWriter) = Unit

    override fun startGroupBoundary(view: ModelView, group: String, writer: IndentingWriter) {
        D2Object.build(Group(group).d2Id) {
            label(group)
            withGroupStyle()
        }.startObject(writer)
    }

    override fun endGroupBoundary(view: ModelView, writer: IndentingWriter) {
        D2Object.endObject(writer)
    }

    override fun startSoftwareSystemBoundary(view: ModelView, softwareSystem: SoftwareSystem, writer: IndentingWriter) {
        softwareSystem.d2ObjectInView(view).startObject(writer)
    }

    override fun endSoftwareSystemBoundary(view: ModelView, writer: IndentingWriter) {
        D2Object.endObject(writer)
    }

    override fun startContainerBoundary(view: ModelView, container: Container, writer: IndentingWriter) {
        container.d2ObjectInView(view).startObject(writer)
    }

    override fun endContainerBoundary(view: ModelView, writer: IndentingWriter) {
        D2Object.endObject(writer)
    }

    override fun startDeploymentNodeBoundary(
        view: DeploymentView, deploymentNode: DeploymentNode, writer: IndentingWriter
    ) {
        deploymentNode.d2ObjectInView(view).startObject(writer)
    }

    override fun endDeploymentNodeBoundary(view: ModelView, writer: IndentingWriter) {
        D2Object.endObject(writer)
    }

    override fun writeElement(view: ModelView, element: Element, writer: IndentingWriter) {
        element.d2ObjectInView(view).writeObject(writer)
    }

    override fun writeRelationship(view: ModelView, relationshipView: RelationshipView, writer: IndentingWriter) {
        val relationshipStyle = findRelationshipStyle(view, relationshipView.relationship)
        D2Object.build(relationshipView.relationshipName(view)) {
            label(relationshipView.d2Label(view))
            opacity(relationshipStyle.d2Opacity())
            stroke(relationshipStyle.color)
            fontSize(relationshipStyle.fontSize)
            when (relationshipStyle.style) {
                LineStyle.Dashed -> dashed()
                LineStyle.Dotted -> dotted()
                else -> Unit
            }
            strokeWidth(relationshipStyle.thickness)
        }.writeObject(writer)
    }


    override fun createDiagram(view: ModelView, definition: String): Diagram {
        return D2Diagram(view, definition)
    }

    private fun Element.d2ObjectInView(view: ModelView): D2Object {
        val style = findElementStyle(view, this)
        return D2Object.build(d2Id) {
            label(d2Label(view))
            shape(style.shape.d2Shape())
            icon(style.icon)
            link(url)
            tooltip(description)
            fill(style.background)
            stroke(style.stroke)
            strokeWidth(style.strokeWidth)
            opacity(style.d2Opacity())
            when (style.border) {
                Border.Dashed -> dashed()
                Border.Dotted -> dotted()
                else -> Unit
            }
            multiple(hasMultipleInstances)
            fontColor(style.color)
            fontSize(style.fontSize)
        }
    }

    private fun Element.d2Label(view: ModelView): String {
        return buildString {
            append(name)
            typeOfOrNull(view)?.let {
                append("\n", it)
            }
        }
    }

    private fun Element.typeOfOrNull(view: ModelView, includeMetadataSymbols: Boolean = true): String? {
        return typeOf(view, this, includeMetadataSymbols).takeUnless(String::isBlank)
    }
}