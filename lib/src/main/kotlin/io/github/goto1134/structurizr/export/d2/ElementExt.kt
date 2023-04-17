package io.github.goto1134.structurizr.export.d2

import com.structurizr.model.DeploymentNode
import com.structurizr.model.Element
import com.structurizr.model.GroupableElement
import com.structurizr.view.*

fun ElementStyle.d2Opacity() = opacity.toDouble() / 100

fun RelationshipView.d2LabelInView(view: View): String {
    return buildString {
        if (view is DynamicView) {
            append(order, " – ")
        }
        append(description.ifBlank { relationship.description })
        if (view !is DynamicView) {
            relationship.typeOfOrNull(view)?.let { append("\n", it) }
        }
    }
}

@JvmInline
value class Group(private val name: String) {
    val d2Id get() = "\"group_$name\""
}

val Element.d2Id get() = "container_$id"

val Element.d2GroupId get() = if (this is GroupableElement && !group.isNullOrEmpty()) Group(group).d2Id else null

val Element.hasMultipleInstances get() = this is DeploymentNode && "1" != instances

fun Element.inView(view: ModelView) = view.isElementInView(this)

fun Element.inViewOrRoot(view: ModelView): Boolean {
    return view is ComponentView && equals(view.container)
            || view is ContainerView && equals(view.softwareSystem)
            || view is DynamicView && equals(view.element)
            || inView(view)
}

private fun Element.nameInView(view: ModelView) = buildString {
    if (inView(view) && d2GroupId != null) append(d2GroupId, ".")
    append(d2Id)
}

private fun Element.parentSequence(): Sequence<Element> = generateSequence(this) { it.parent }

fun Element.absolutePathInView(view: ModelView): String {
    return parentSequence().takeWhile { it.inViewOrRoot(view) }.asIterable().reversed()
        .joinToString(separator = ".") { it.nameInView(view) }
}

fun Element.groupAbsolutePathInView(view: ModelView): String? {
    return listOfNotNull(parent?.absolutePathInView(view), d2GroupId).joinToString(".").takeUnless(String::isBlank)
}