package io.github.goto1134.structurizr.export.d2

import com.structurizr.model.InteractionStyle
import com.structurizr.model.Relationship
import com.structurizr.view.*
import io.github.goto1134.structurizr.export.d2.model.D2Connection


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

fun RelationshipView.d2Connection(): D2Connection {
    return when (isResponse) {
        true -> D2Connection.REVERSE
        else -> D2Connection.DIRECT
    }
}

fun RelationshipView.relationshipName(view: ModelView): String {
    return buildString {
        append(
            relationship.source.absolutePathInView(view),
            " ",
            d2Connection(),
            " ",
            relationship.destination.absolutePathInView(view)
        )
    }
}

fun RelationshipStyle.d2Opacity() = opacity.toDouble() / 100
