package io.github.goto1134.structurizr.export.d2

import com.structurizr.view.Shape
import io.github.goto1134.structurizr.export.d2.model.D2Shape

fun Shape.d2Shape(): D2Shape {
    return when (this) {
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
}