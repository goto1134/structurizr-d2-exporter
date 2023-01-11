package io.github.goto1134.structurizr.export.d2;

import com.structurizr.export.AbstractDiagramExporter;
import com.structurizr.export.Diagram;
import com.structurizr.export.IndentingWriter;
import com.structurizr.model.*;
import com.structurizr.util.StringUtils;
import com.structurizr.view.*;
import io.github.goto1134.structurizr.export.d2.model.*;

import java.util.*;
import java.util.stream.Collectors;


public class D2Exporter extends AbstractDiagramExporter {

    public static final String D2_IGNORE_RELATION_FONT_SIZE = "d2.ignore_relation_font_size";
    public static final String D2_TITLE_POSITION = "d2.title_position";

    @Override
    protected void writeHeader(View view, IndentingWriter writer) {
        String title = getTitleString(view);
        String titlePosition = view.getProperties().getOrDefault(D2_TITLE_POSITION, D2NearConstant.TOP_CENTER.toString());
        D2TextObject.builder("title", "md", String.format("# %s", title))
                .near(titlePosition)
                .build()
                .writeObject(writer);

        Optional.ofNullable(view.getAutomaticLayout())
                .map(AutomaticLayout::getRankDirection)
                .map(it -> {
                    switch (it) {
                        case TopBottom:
                            return D2Direction.TOP_TO_BOTTOM;
                        case BottomTop:
                            return D2Direction.BOTTOM_TO_TOP;
                        case LeftRight:
                            return D2Direction.LEFT_TO_RIGHT;
                        case RightLeft:
                            return D2Direction.RIGHT_TO_LEFT;
                        default:
                            throw new IllegalStateException("Unexpected value: " + it);
                    }
                })
                .map(it -> new D2Property<>(D2Keyword.DIRECTION, it))
                .ifPresent(it -> it.write(writer));
    }

    private String getTitleString(View view) {
        String title = view.getTitle();
        if (StringUtils.isNullOrEmpty(title)) {
            title = view.getName();
        }
        return title;
    }

    @Override
    protected void writeFooter(View view, IndentingWriter writer) {
    }

    @Override
    protected void startEnterpriseBoundary(View view, String enterpriseName, IndentingWriter writer) {
        D2Object.builder(enterpriseId(enterpriseName))
                .label(enterpriseName)
                .withGroupStyle()
                .build()
                .startObject(writer);
    }

    @Override
    protected void endEnterpriseBoundary(View view, IndentingWriter writer) {
        D2Object.endObject(writer);
    }

    @Override
    protected void startGroupBoundary(View view, String group, IndentingWriter writer) {
        D2Object.builder(groupId(group))
                .label(group)
                .withGroupStyle()
                .build()
                .startObject(writer);
    }

    @Override
    protected void endGroupBoundary(View view, IndentingWriter writer) {
        D2Object.endObject(writer);
    }

    @Override
    protected void startSoftwareSystemBoundary(View view, SoftwareSystem softwareSystem, IndentingWriter writer) {
        getD2Object(view, softwareSystem).startObject(writer);
    }

    @Override
    protected void endSoftwareSystemBoundary(View view, IndentingWriter writer) {
        D2Object.endObject(writer);
    }

    @Override
    protected void startContainerBoundary(View view, Container container, IndentingWriter writer) {
        getD2Object(view, container).startObject(writer);
    }

    @Override
    protected void endContainerBoundary(View view, IndentingWriter writer) {
        D2Object.endObject(writer);
    }

    @Override
    protected void startDeploymentNodeBoundary(DeploymentView view, DeploymentNode deploymentNode, IndentingWriter writer) {
        getD2Object(view, deploymentNode).startObject(writer);
    }

    @Override
    protected void endDeploymentNodeBoundary(View view, IndentingWriter writer) {
        D2Object.endObject(writer);
    }

    @Override
    protected void writeElement(View view, Element element, IndentingWriter writer) {
        getD2Object(view, element).writeObject(writer);
    }

    @Override
    protected void writeRelationship(View view, RelationshipView relationshipView, IndentingWriter writer) {
        Relationship relationship = relationshipView.getRelationship();
        RelationshipStyle relationshipStyle = findRelationshipStyle(view, relationshipView.getRelationship());

        D2Connection connection = Optional.ofNullable(relationshipView.isResponse())
                .filter(it -> it)
                .map(it -> D2Connection.REVERSE)
                .orElse(D2Connection.DIRECT);
        String sourceKey = getAbsolutePath(view, relationship.getSource());
        String destinationKey = getAbsolutePath(view, relationship.getDestination());
        D2Object.Builder builder = D2Object.builder(String.format("%s %s %s", sourceKey, connection, destinationKey))
                .label(getLabel(view, relationshipView))
                .opacity(relationshipStyle.getOpacity().doubleValue() / 100)
                .stroke(relationshipStyle.getColor());

        if (!Boolean.parseBoolean(view.getProperties().getOrDefault(D2_IGNORE_RELATION_FONT_SIZE, "true"))) {
            builder.fontSize(relationshipStyle.getFontSize());
        }

        Optional.ofNullable(relationshipStyle.getStyle())
                .ifPresent(lineStyle -> {
                    if (lineStyle == LineStyle.Dashed) {
                        builder.dashed();
                    } else if (lineStyle == LineStyle.Dotted) {
                        builder.dotted();
                    }
                });
        D2Object d2Object = builder.strokeWidth(relationshipStyle.getThickness())
                .build();
        d2Object.writeObject(writer);
    }

    @Override
    protected Diagram createDiagram(View view, String definition) {
        return new D2Diagram(view, definition);
    }


    private D2Object getD2Object(View view, Element element) {
        ElementStyle elementStyle = findElementStyle(view, element);
        D2Object.Builder builder = D2Object.builder(idWithPrefix(element))
                .label(getLabel(view, element))
                .shape(d2ShapeOf(elementStyle))
                .icon(Optional.ofNullable(elementStyle.getIcon()))
                .link(Optional.ofNullable(element.getUrl()))
                .fill(elementStyle.getBackground())
                .stroke(elementStyle.getStroke())

                .strokeWidth(Optional.ofNullable(elementStyle.getStrokeWidth()))
                .opacity(elementStyle.getOpacity().doubleValue() / 100);
        switch (elementStyle.getBorder()) {
            case Dashed:
                builder.dashed();
                break;
            case Dotted:
                builder.dotted();
                break;
            default:
                break;
        }
        return builder
                .multiple(element instanceof DeploymentNode && ((DeploymentNode) element).getInstances() > 1)
                .fontColor(elementStyle.getColor())
                .fontSize(elementStyle.getFontSize())
                .build();
    }

    D2Shape d2ShapeOf(ElementStyle style) {
        return d2ShapeOf(style.getShape());
    }

    D2Shape d2ShapeOf(Shape shape) {
        switch (shape) {
            case Person:
            case Robot:
                return D2Shape.PERSON;
            case Cylinder:
                return D2Shape.CYLINDER;
            case Folder:
                return D2Shape.PACKAGE;
            case Ellipse:
                return D2Shape.OVAL;
            case Circle:
                return D2Shape.CIRCLE;
            case Hexagon:
                return D2Shape.HEXAGON;
            case Pipe:
                return D2Shape.QUEUE;
            case Diamond:
                return D2Shape.DIAMOND;
            default:
                return D2Shape.RECTANGLE;
        }
    }

    private String getAbsolutePath(View view, Element element) {
        Deque<Element> pathFromParent = new LinkedList<>();
        do {
            pathFromParent.addFirst(element);
        } while (
                (element = element.getParent()) != null
                        && (
                        view.isElementInView(element)
                                || (view instanceof ComponentView && ((ComponentView) view).getContainer().equals(element))
                                || (view instanceof ContainerView && view.getSoftwareSystem().equals(element))
                                || (view instanceof DynamicView) && ((DynamicView) view).getElement().equals(element)
                )
        );
        String enterprisePrefix = getEnterprisePrefix(view, pathFromParent.peek());
        return pathFromParent.stream().map(it -> view.isElementInView(it) ? idWithGroupAndPrefix(it) : idWithPrefix(it.getId())).collect(Collectors.joining(".", enterprisePrefix, ""));
    }

    private String getEnterprisePrefix(View view, Element parent) {
        boolean enterpriseBoundaryVisible =
                (view instanceof SystemLandscapeView && ((SystemLandscapeView) view).isEnterpriseBoundaryVisible())
                        || (view instanceof SystemContextView && ((SystemContextView) view).isEnterpriseBoundaryVisible());
        if (enterpriseBoundaryVisible
                && (
                parent instanceof Person && ((Person) parent).getLocation() == Location.Internal
                        || parent instanceof SoftwareSystem && ((SoftwareSystem) parent).getLocation() == Location.Internal
        )) {
            return String.format("%s.", enterpriseId(view.getModel().getEnterprise().getName()));
        }
        return "";
    }

    private String idWithGroupAndPrefix(Element element) {
        String idWithPrefix = idWithPrefix(element.getId());
        if (element instanceof GroupableElement && hasValue(((GroupableElement) element).getGroup())) {
            return String.format("%s.%s", groupId(((GroupableElement) element).getGroup()), idWithPrefix);
        } else {
            return idWithPrefix;
        }
    }

    private String idWithPrefix(Element element) {
        return idWithPrefix(element.getId());
    }

    private String idWithPrefix(String id) {
        return String.format("container_%s", id);
    }

    private String groupId(String groupName) {
        return String.format("\"group_%s\"", groupName);
    }

    private String enterpriseId(String enterpriseName) {
        return String.format("\"enterprise_%s\"", enterpriseName);
    }

    private String getLabel(View view, Element element) {
        String typeOf = typeOf(view, element, true);
        if (hasValue(typeOf)) {
            return String.format("%s\n%s", element.getName(), typeOf);
        }
        return element.getName();
    }

    private String getLabel(View view, RelationshipView relationshipView) {
        if (hasValue(relationshipView.getDescription())) {
            if (hasValue(relationshipView.getOrder())) {
                return String.format("%s – %s", relationshipView.getOrder(), relationshipView.getDescription());
            }
            return relationshipView.getDescription();
        }

        Relationship relationship = relationshipView.getRelationship();
        String typeOf = typeOf(view, relationship);
        if (hasValue(typeOf)) {
            return String.format("%s\n%s", relationship.getDescription(), typeOf);
        }
        return relationship.getDescription();
    }

    private String typeOf(View view, Relationship relationship) {
        String typeOf;
        if (hasValue(relationship.getTechnology())) {
            if (relationship.getInteractionStyle() == InteractionStyle.Asynchronous) {
                typeOf = String.format("Async %s", relationship.getTechnology());
            } else {
                typeOf = relationship.getTechnology();
            }
        } else {
            if (relationship.getInteractionStyle() == InteractionStyle.Asynchronous) {
                typeOf = "Async";
            } else {
                typeOf = "";
            }
        }
        if (hasValue(typeOf)) {
            Configuration configuration = view.getViewSet().getConfiguration();
            if (configuration.getMetadataSymbols() == null) {
                configuration.setMetadataSymbols(MetadataSymbols.SquareBrackets);
            }

            switch (configuration.getMetadataSymbols()) {
                case RoundBrackets:
                    return "(" + typeOf + ")";
                case CurlyBrackets:
                    return "{" + typeOf + "}";
                case AngleBrackets:
                    return "<" + typeOf + ">";
                case DoubleAngleBrackets:
                    return "<<" + typeOf + ">>";
                case None:
                    return typeOf;
                default:
                    return "[" + typeOf + "]";
            }
        }
        return typeOf;
    }
}
