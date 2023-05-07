package io.github.goto1134.structurizr.export.d2

import com.structurizr.model.DeploymentNode
import com.structurizr.model.Element
import com.structurizr.model.GroupableElement
import com.structurizr.view.*
import io.github.goto1134.structurizr.export.d2.model.D2FillPattern
import io.github.goto1134.structurizr.export.d2.model.D2Shape

val ElementStyle.d2Opacity get() = opacity.toDouble() / 100

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
        else -> D2Shape.RECTANGLE
    }

val Element.d2Id get() = "container_$id"

val Element.d2GroupId get() = (this as? GroupableElement)?.d2GroupId

val GroupableElement.d2GroupId get() = group?.takeUnless { it.isEmpty() }?.let { "\"group_$it\"" }

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

fun GroupableElement.groupAbsolutePathInView(view: ModelView): String? {
    return listOfNotNull(parent?.absolutePathInView(view), d2GroupId).joinToString(".")
        .takeUnless(String::isBlank)
}
