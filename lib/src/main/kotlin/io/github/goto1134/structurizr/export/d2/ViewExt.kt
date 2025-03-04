package io.github.goto1134.structurizr.export.d2

import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ModelView
import com.structurizr.view.View
import io.github.goto1134.structurizr.export.d2.AnimationType.D2
import io.github.goto1134.structurizr.export.d2.model.D2Direction
import io.github.goto1134.structurizr.export.d2.model.D2FillPattern
import io.github.goto1134.structurizr.export.d2.model.D2NearConstant
import io.github.goto1134.structurizr.export.d2.model.D2NearConstant.TOP_CENTER

fun View.getViewOrViewSetProperty(key: String) = properties.getOrElse(key) { viewSet.configuration.properties[key] }

val View.d2UseC4Person: Boolean
    get() = getViewOrViewSetProperty(D2Exporter.D2_USE_C4_PERSON).toBoolean()

val View.d2Title: String
    get() = title?.takeUnless(String::isBlank) ?: name

val View.d2TitlePosition: D2NearConstant
    get() = D2NearConstant.getOrDefault(getViewOrViewSetProperty(D2Exporter.D2_TITLE_POSITION), TOP_CENTER)

val View.d2FillPattern
    get() = D2FillPattern.get(getViewOrViewSetProperty(D2Exporter.D2_FILL_PATTERN))

val View.animationType: AnimationType
    get() = AnimationType.getOrDefault(getViewOrViewSetProperty(D2Exporter.D2_ANIMATION), D2)

val View.includeSoftwareSystemBoundaries
    get() = properties[D2Exporter.STRUCTURIZR_INCLUDE_SOFTWARE_SYSTEM_BOUNDARIES].toBoolean()

val ModelView.d2Direction get() = automaticLayout?.d2Direction

val AutomaticLayout.d2Direction: D2Direction?
    get() = when (this.rankDirection) {
        AutomaticLayout.RankDirection.TopBottom -> D2Direction.TOP_TO_BOTTOM
        AutomaticLayout.RankDirection.BottomTop -> D2Direction.BOTTOM_TO_TOP
        AutomaticLayout.RankDirection.LeftRight -> D2Direction.LEFT_TO_RIGHT
        AutomaticLayout.RankDirection.RightLeft -> D2Direction.RIGHT_TO_LEFT
        else -> null
    }
