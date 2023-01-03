# D2

The [D2Exporter](D2Exporter.java) class provides a way to export views to
diagram definitions that are compatible with [D2](https://d2lang.com).

## Example usage

You can either export all views in a workspace:

```
Workspace workspace = ...
D2Exporter exporter = new D2Exporter();
Collection<Diagram> diagrams = exporter.export(workspace);
```

Or just a single view:

```
Workspace workspace = ...
SystemLandscape view = ...
D2Exporter exporter = new D2Exporter();
Diagram diagram = exporter.export(view);
```
