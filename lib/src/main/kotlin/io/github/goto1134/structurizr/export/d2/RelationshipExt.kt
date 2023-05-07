package io.github.goto1134.structurizr.export.d2

import com.structurizr.model.InteractionStyle
import com.structurizr.model.Relationship
import com.structurizr.view.*
import io.github.goto1134.structurizr.export.d2.D2Exporter.Companion.D2_CONNECTION_ANIMATED
import io.github.goto1134.structurizr.export.d2.model.D2Connection

val RelationshipStyle.d2Animated get() = properties[D2_CONNECTION_ANIMATED].toBoolean()

val RelationshipStyle.d2Opacity get() = opacity.toDouble() / 100

val RelationshipView.d2Connection: D2Connection
    get() = if (isResponse == true) D2Connection.REVERSE else D2Connection.DIRECT

fun Relationship.typeOf(view: View): String {
    val typeOf = buildString {
        if (InteractionStyle.Asynchronous == interactionStyle) append("Async ")
        append(technology.orEmpty())
    }.trim()

    return if (typeOf.isEmpty()) typeOf
    else when (view.viewSet.configuration.metadataSymbols ?: MetadataSymbols.SquareBrackets) {
        MetadataSymbols.RoundBrackets -> "($typeOf)"
        MetadataSymbols.CurlyBrackets -> "{$typeOf}"
        MetadataSymbols.AngleBrackets -> "<$typeOf>"
        MetadataSymbols.DoubleAngleBrackets -> "<<$typeOf>>"
        MetadataSymbols.None -> typeOf
        else -> "[$typeOf]"
    }
}

fun Relationship.typeOfOrNull(view: View): String? = typeOf(view).takeUnless(String::isBlank)

fun RelationshipView.relationshipNameInView(view: ModelView): String {
    return buildString {
        append(
            relationship.source.absolutePathInView(view),
            " ",
            d2Connection,
            " ",
            relationship.destination.absolutePathInView(view)
        )
    }
}

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
