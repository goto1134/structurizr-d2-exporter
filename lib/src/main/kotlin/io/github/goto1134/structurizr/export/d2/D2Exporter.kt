package io.github.goto1134.structurizr.export.d2

import com.structurizr.export.AbstractDiagramExporter
import com.structurizr.export.Diagram
import com.structurizr.export.IndentingWriter
import com.structurizr.model.*
import com.structurizr.view.*
import io.github.goto1134.structurizr.export.d2.model.D2Shape
import io.github.goto1134.structurizr.export.d2.model.GlobalObject
import io.github.goto1134.structurizr.export.d2.model.NamedObject
import io.github.goto1134.structurizr.export.d2.model.TextObject

open class D2Exporter : AbstractDiagramExporter() {

    companion object {
        const val D2_USE_C4_PERSON = "d2.use_c4_person"
        const val D2_TITLE_POSITION = "d2.title_position"
        const val D2_ANIMATION = "d2.animation"
        const val D2_CONNECTION_ANIMATED = "d2.animated"
        const val D2_FILL_PATTERN = "d2.fill_pattern"
        const val STRUCTURIZR_INCLUDE_SOFTWARE_SYSTEM_BOUNDARIES = "structurizr.softwareSystemBoundaries"
        const val STRUCTURIZR_GROUP_SEPARATOR_PROPERTY_NAME = "structurizr.groupSeparator"
    }

    override fun createDiagram(view: ModelView, definition: String) = D2Diagram(view, definition)

    override fun export(view: CustomView): Diagram {
        if (view.useD2StepsAnimation(view.animations)) {
            val diagram = exportD2Steps(view, view.animations)
            diagram.legend = createLegend(view)
            return diagram
        }
        return super.export(view)
    }

    override fun export(view: SystemLandscapeView): Diagram {
        if (view.useD2StepsAnimation(view.animations)) {
            val diagram = exportD2Steps(view, view.animations)
            diagram.legend = createLegend(view)
            return diagram
        }
        return super.export(view)
    }

    override fun export(view: SystemContextView): Diagram {
        if (view.useD2StepsAnimation(view.animations)) {
            val diagram = exportD2Steps(view, view.animations)
            diagram.legend = createLegend(view)
            return diagram
        }
        return super.export(view)
    }

    override fun export(view: ContainerView): Diagram {
        if (view.useD2StepsAnimation(view.animations)) {
            val diagram = exportD2Steps(view, view.animations)
            diagram.legend = createLegend(view)
            return diagram
        }
        return super.export(view)
    }

    override fun export(view: ComponentView): Diagram {
        if (view.useD2StepsAnimation(view.animations)) {
            val diagram = exportD2Steps(view, view.animations)
            diagram.legend = createLegend(view)
            return diagram
        }
        return super.export(view)
    }

    override fun export(view: DeploymentView): Diagram {
        if (view.useD2StepsAnimation(view.animations)) {
            val diagram = exportD2Steps(view, view.animations)
            diagram.legend = createLegend(view)
            return diagram
        }
        return super.export(view)
    }

    private fun ModelView.useD2StepsAnimation(animations: List<Animation>): Boolean {
        return animations.isNotEmpty() && animationType == AnimationType.D2
    }

    private fun exportD2Steps(view: ModelView, animations: List<Animation>): Diagram {
        val writer = IndentingWriter()
        writeHeader(view, writer)
        NamedObject.build("steps").writeObject(writer) {
            writeAnimations(view, animations, it)
        }
        writeFooter(view, writer)
        return createDiagram(view, writer.toString())
    }

    private fun writeAnimations(view: ModelView, animations: List<Animation>, writer: IndentingWriter) {
        val stepsAnimationState = StepsAnimationState()
        val elements = view.elements.associateBy { it.id }
        val relationships = view.relationships.associateBy { it.id }

        for (animation in animations.sortedBy { it.order }) {
            NamedObject.build(animation.order.toString()).writeObject(writer) {
                writeAnimationElements(
                    view,
                    animation.elements
                        .mapNotNull { elements[it]?.element }
                        .filterIsInstance<GroupableElement>()
                        .sortedBy { it.id },
                    stepsAnimationState,
                    writer
                )
                writer.writeLine()
                writeAnimationRelationships(
                    view,
                    animation.relationships.mapNotNull { relationships[it] }.sortedBy { it.id },
                    writer
                )
            }
        }
    }

    private fun writeAnimationRelationships(
        view: ModelView,
        relationships: List<RelationshipView>,
        writer: IndentingWriter
    ) {
        for (relationshipView in relationships) {
            writeRelationship(view, relationshipView, writer)
        }
    }

    private fun writeAnimationElements(
        view: ModelView,
        elementsInStep: List<GroupableElement>,
        stepsAnimationState: StepsAnimationState,
        writer: IndentingWriter
    ) {
        if (view is ContainerView) {
            elementsInStep.filterIsInstance<Container>().map { it.softwareSystem }.sortedBy { it.id }.forEach {
                stepsAnimationState.addElement(it) {
                    startSoftwareSystemBoundary(view, it, writer)
                }
            }
        }
        if (view is ComponentView) {
            val containers = elementsInStep.filterIsInstance<Component>().map { it.container }.sortedBy { it.id }
            if (view.includeSoftwareSystemBoundaries) {
                containers.map { it.softwareSystem }.sortedBy { it.id }.forEach {
                    stepsAnimationState.addElement(it) {
                        startSoftwareSystemBoundary(view, it, writer)
                    }
                }
            }
            containers.forEach {
                stepsAnimationState.addElement(it) {
                    startContainerBoundary(view, it, writer)
                }
            }
        }
        elementsInStep.forEach {
            writeElementWithGroup(view, writer, it, stepsAnimationState)
        }
    }

    private fun writeElementWithGroup(
        view: ModelView,
        writer: IndentingWriter,
        element: GroupableElement,
        stepsAnimationState: StepsAnimationState
    ) {
        stepsAnimationState.addElement(element) {
            writeElement(view, element, writer)
        }
        element.groupsWithPathsOrNull()
            ?.filter(stepsAnimationState::addGroup)
            ?.forEach {
                writeGroup(view, it, writer)
            }
    }

    override fun isAnimationSupported(view: ModelView) = view.animationType == AnimationType.FRAMES

    override fun writeHeader(view: ModelView, writer: IndentingWriter) {
        TextObject.build("title", "md", "# ${view.d2Title}") {
            near(view.d2TitlePosition)
        }.writeObject(writer)

        GlobalObject.build {
            direction(view.d2Direction)
            fillPattern(view.d2FillPattern)
        }.writeObject(writer)
    }

    override fun writeElements(view: ModelView, elements: List<GroupableElement>, writer: IndentingWriter) {
        elements.asSequence()
            .mapNotNull { it.groupsWithPathsOrNull() }
            .flatten()
            .distinct()
            .forEach {
                writeGroup(view, it, writer)
            }
        elements.sortedBy { it.id }.forEach {
            writeElementOrDeploymentNode(view, it, writer)
        }
    }

    private fun writeElementOrDeploymentNode(view: ModelView, element: Element, writer: IndentingWriter) {
        if (view is DeploymentView && element is DeploymentNode) {
            writeDeploymentNode(view, element, writer)
        } else {
            writeElement(view, element, writer)
        }
    }

    private fun writeDeploymentNode(view: DeploymentView, deploymentNode: DeploymentNode, writer: IndentingWriter) {
        startDeploymentNodeBoundary(view, deploymentNode, writer)
        val elements = sequenceOf(
            deploymentNode.children.asSequence(),
            deploymentNode.infrastructureNodes.asSequence(),
            deploymentNode.softwareSystemInstances.asSequence(),
            deploymentNode.containerInstances.asSequence()
        ).flatten()
            .filter { it.inViewNotRoot(view) }
            .toList()
        writeElements(view, elements, writer)
        endDeploymentNodeBoundary(view, writer)
    }

    private fun writeGroup(view: ModelView, groupWithPath: GroupWithPath, writer: IndentingWriter) {
        val allGroupsStyle = view.viewSet.configuration.styles.findElementStyle("Group")
        val currentGroupStyle = view.viewSet.configuration.styles.findElementStyle("Group:${groupWithPath.fullGroup}")
        NamedObject.build(groupWithPath.absolutePathInView(view)) {
            label(groupWithPath.group)
            shape(currentGroupStyle?.let { d2Shape(view, it) } ?: allGroupsStyle?.let { d2Shape(view, it) } ?: D2Shape.RECTANGLE)
            icon(currentGroupStyle?.icon ?: allGroupsStyle?.icon)
            stroke(currentGroupStyle?.stroke ?: currentGroupStyle?.stroke ?: "#cccccc")
            strokeWidth(currentGroupStyle?.strokeWidth ?: allGroupsStyle?.strokeWidth ?: 2)
            fontColor(currentGroupStyle?.color ?: allGroupsStyle?.color ?: "#cccccc")
            fontSize(currentGroupStyle?.fontSize ?: allGroupsStyle?.fontSize ?: 24)
            opacity(currentGroupStyle?.d2Opacity ?: currentGroupStyle?.d2Opacity ?: 1.0)
            dashed()
            fill(currentGroupStyle?.background ?: allGroupsStyle?.background ?: "#ffffff")
            fillPattern(currentGroupStyle?.d2FillPattern ?: allGroupsStyle?.d2FillPattern)
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
        NamedObject.build(relationshipView.relationshipNameInView(view)) {
            animated(relationshipStyle.d2Animated)
            label(relationshipView.d2LabelInView(view))
            opacity(relationshipStyle.d2Opacity ?: 1.0)
            stroke(relationshipStyle.color ?: "#707070")
            strokeWidth(relationshipStyle.thickness ?: 2)
            fontSize(relationshipStyle.fontSize ?: 24)
            when (relationshipStyle.style) {
                LineStyle.Dotted -> dotted()
                LineStyle.Solid -> Unit
                LineStyle.Dashed, null -> dashed()
            }
        }.writeObject(writer)
    }

    private fun Element.writeD2Object(view: ModelView, writer: IndentingWriter) {
        val style = findElementStyle(view, this)
        NamedObject.build(absolutePathInView(view)) {
            label(d2Label(view))
            shape(d2Shape(view, style))
            icon(style.icon)
            link(url)
            tooltip(description)
            fill(style.background)
            fillPattern(style.d2FillPattern)
            stroke(style.stroke)
            strokeWidth(style.strokeWidth)
            opacity(style.d2Opacity ?: 1.0)
            when (style.border) {
                Border.Dashed -> dashed()
                Border.Dotted -> dotted()
                else -> Unit
            }
            multiple(hasMultipleInstances)
            fontColor(style.color)
            fontSize(style.fontSize)
        }.writeObject(writer)
    }

    private fun Element.d2Label(view: ModelView): String = buildString {
        append(name)
        typeOfOrNull(view)?.let {
            append("\n", it)
        }
    }

    private fun Element.typeOfOrNull(view: ModelView, includeMetadataSymbols: Boolean = true): String? =
        typeOf(view, this, includeMetadataSymbols).takeUnless(String::isBlank)

    class StepsAnimationState {
        private val metElements: MutableSet<String> = mutableSetOf()
        private val metGroups: MutableSet<GroupWithPath> = mutableSetOf()

        fun addGroup(group: GroupWithPath): Boolean = metGroups.add(group)

        fun addElement(element: Element, onNew: () -> Unit) {
            if (metElements.add(element.id)) onNew()
        }
    }
}
