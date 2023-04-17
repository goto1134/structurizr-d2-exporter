package io.github.goto1134.structurizr.export.d2

import com.structurizr.view.AutomaticLayout
import com.structurizr.view.View
import io.github.goto1134.structurizr.export.d2.model.D2Direction
import io.github.goto1134.structurizr.export.d2.model.D2NearConstant

val View.d2Title: String
    get() = title?.takeUnless(String::isBlank) ?: name

val View.d2TitlePosition: D2NearConstant
    get() = D2NearConstant.getOrDefault(properties[D2Exporter.D2_TITLE_POSITION], D2NearConstant.TOP_CENTER)

fun AutomaticLayout.getD2Direction(): D2Direction? {
    return when (this.rankDirection) {
        AutomaticLayout.RankDirection.TopBottom -> D2Direction.TOP_TO_BOTTOM
        AutomaticLayout.RankDirection.BottomTop -> D2Direction.BOTTOM_TO_TOP
        AutomaticLayout.RankDirection.LeftRight -> D2Direction.LEFT_TO_RIGHT
        AutomaticLayout.RankDirection.RightLeft -> D2Direction.RIGHT_TO_LEFT
        else -> null
    }
}