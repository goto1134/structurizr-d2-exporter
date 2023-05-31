package io.github.goto1134.structurizr.export.d2

import com.structurizr.model.DeploymentNode
import com.structurizr.model.Element
import com.structurizr.model.GroupableElement
import com.structurizr.view.*
import io.github.goto1134.structurizr.export.d2.D2Exporter.Companion.STRUCTURIZR_GROUP_SEPARATOR_PROPERTY_NAME
import io.github.goto1134.structurizr.export.d2.model.D2FillPattern
import io.github.goto1134.structurizr.export.d2.model.D2Shape

val ElementStyle.d2Opacity get() = opacity?.toDouble()?.div( 100)

val ElementStyle.d2FillPattern get() = D2FillPattern.get(properties[D2Exporter.D2_FILL_PATTERN])

val ElementStyle.d2Shape
    get() = when (shape) {
        Shape.Person, Shape.Robot -> D2Shape.PERSON
        Shape.Cylinder -> D2Shape.CYLINDER
        Shape.Folder -> D2Shape.PACKAGE
        Shape.Ellipse -> D2Shape.OVAL
        Shape.Circle -> D2Shape.CIRCLE
        Shape.Hexagon -> D2Shape.HEXAGON
        Shape.Pipe -> D2Shape.QUEUE
        Shape.Diamond -> D2Shape.DIAMOND
        null -> null
        else -> D2Shape.RECTANGLE
    }

val Element.d2Id get() = "container_$id"

val Element.d2GroupId get() = (this as? GroupableElement)?.d2GroupId

val GroupableElement.d2GroupId: String? get() = parentGroupIdSequenceOrNull()?.joinToString(".")

fun GroupableElement.parentGroupIdSequenceOrNull() = parentGroupSequenceOrNull()?.map { "\"group_$it\"" }

fun GroupableElement.parentGroupSequenceOrNull(): Sequence<String>? {
    val group = group?.takeUnless { it.isEmpty() } ?: return null
    return when (val separator = model.properties[STRUCTURIZR_GROUP_SEPARATOR_PROPERTY_NAME]) {
        null -> sequenceOf(group)
        else -> group.splitToSequence(separator)
    }
}

data class GroupWithPath(val parent: Element?, val relativePath: String, val fullGroup: String, val group: String) {
    fun absolutePathInView(view: ModelView) = buildString {
        parent?.absolutePathInView(view)?.let { append(it, ".") }
        append(relativePath)
    }
}

fun GroupableElement.groupsWithPathsOrNull(): Sequence<GroupWithPath>? {
    val groupSeparator = model.properties[STRUCTURIZR_GROUP_SEPARATOR_PROPERTY_NAME]
    return parentGroupSequenceOrNull()?.scan(GroupWithPath(parent, "", "", "")) { wrapper, groupName ->
        GroupWithPath(
            parent = parent,
            relativePath = buildString {
                if (wrapper.relativePath.isNotEmpty()) append(wrapper.relativePath, ".")
                append("\"group_", groupName, "\"")
            },
            fullGroup = buildString {
                if (wrapper.fullGroup.isNotEmpty()) append(wrapper.fullGroup, groupSeparator)
                append(groupName)
            },
            group = groupName
        )
    }?.drop(1)
}

val Element.hasMultipleInstances get() = this is DeploymentNode && "1" != instances

fun Element.inViewNotRoot(view: ModelView) = view.isElementInView(this)

fun Element.inViewOrRoot(view: ModelView): Boolean {
    return view is ComponentView && equals(view.container)
            || view is ContainerView && equals(view.softwareSystem)
            || view is DynamicView && equals(view.element)
            || inViewNotRoot(view)
}

private fun Element.nameInView(view: ModelView) = buildString {
    if (inViewNotRoot(view)) d2GroupId?.let { append(it, ".") }
    append(d2Id)
}

private fun Element.parentSequence(): Sequence<Element> = generateSequence(this) { it.parent }

fun Element.absolutePathInView(view: ModelView): String {
    return parentSequence()
        .takeWhile { it.inViewOrRoot(view) }.asIterable()
        .reversed()
        .joinToString(separator = ".") { it.nameInView(view) }
}
