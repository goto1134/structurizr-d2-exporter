package io.github.goto1134.structurizr.export.d2

import com.structurizr.export.AbstractDiagramExporter
import com.structurizr.export.IndentingWriter
import com.structurizr.model.*
import com.structurizr.view.*
import io.github.goto1134.structurizr.export.d2.model.D2Object
import io.github.goto1134.structurizr.export.d2.model.D2TextObject

open class D2Exporter : AbstractDiagramExporter() {

    companion object {
        const val D2_TITLE_POSITION = "d2.title_position"
    }

    override fun createDiagram(view: ModelView, definition: String) = D2Diagram(view, definition)

    override fun writeHeader(view: ModelView, writer: IndentingWriter) {
        D2TextObject.build("title", "md", "# ${view.d2Title}") {
            near(view.d2TitlePosition)
        }.writeObject(writer)
        val d2Direction = view.automaticLayout?.getD2Direction()
        d2Direction?.toD2Property()?.write(writer)
    }

    override fun writeElements(view: ModelView, elements: List<GroupableElement>, writer: IndentingWriter) {
        super.writeElements(view, elements, writer)
        elements.asSequence()
            .filter { it.group != null }
            .distinctBy { it.group }
            .forEach { writeGroup(view, it, writer) }
    }

    protected fun writeGroup(view: ModelView, element: GroupableElement, writer: IndentingWriter) {
        val groupAbsolutePathInView = element.groupAbsolutePathInView(view) ?: return
        D2Object.build(groupAbsolutePathInView) {
            label(element.group)
            withGroupStyle()
        }.writeObject(writer)
    }

    override fun writeFooter(view: ModelView, writer: IndentingWriter) = Unit
    override fun startEnterpriseBoundary(view: ModelView, enterpriseName: String, writer: IndentingWriter) = Unit
    override fun endEnterpriseBoundary(view: ModelView, writer: IndentingWriter) = Unit
    override fun startGroupBoundary(view: ModelView, group: String, writer: IndentingWriter) = Unit
    override fun endGroupBoundary(view: ModelView, writer: IndentingWriter) = Unit

    override fun startSoftwareSystemBoundary(
        view: ModelView,
        softwareSystem: SoftwareSystem,
        writer: IndentingWriter
    ) = softwareSystem.writeD2Object(view, writer)

    override fun endSoftwareSystemBoundary(view: ModelView, writer: IndentingWriter) = Unit

    override fun startContainerBoundary(
        view: ModelView,
        container: Container,
        writer: IndentingWriter
    ) = container.writeD2Object(view, writer)

    override fun endContainerBoundary(view: ModelView, writer: IndentingWriter) = Unit

    override fun startDeploymentNodeBoundary(
        view: DeploymentView,
        deploymentNode: DeploymentNode,
        writer: IndentingWriter
    ) = deploymentNode.writeD2Object(view, writer)

    override fun endDeploymentNodeBoundary(view: ModelView, writer: IndentingWriter) = Unit

    override fun writeElement(
        view: ModelView,
        element: Element,
        writer: IndentingWriter
    ) = element.writeD2Object(view, writer)

    override fun writeRelationship(view: ModelView, relationshipView: RelationshipView, writer: IndentingWriter) {
        val relationshipStyle = findRelationshipStyle(view, relationshipView.relationship)
        D2Object.build(relationshipView.relationshipNameInView(view)) {
            label(relationshipView.d2LabelInView(view))
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

    private fun Element.writeD2Object(view: ModelView, writer: IndentingWriter) =
        d2ObjectInView(view).writeObject(writer)

    private fun Element.d2ObjectInView(view: ModelView): D2Object {
        val style = findElementStyle(view, this)
        return D2Object.build(absolutePathInView(view)) {
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

    private fun Element.d2Label(view: ModelView): String = buildString {
        append(name)
        typeOfOrNull(view)?.let {
            append("\n", it)
        }
    }

    private fun Element.typeOfOrNull(view: ModelView, includeMetadataSymbols: Boolean = true): String? =
        typeOf(view, this, includeMetadataSymbols).takeUnless(String::isBlank)
}