package com.github.goto1134.structurizr.export.d2;

import com.structurizr.Workspace;
import com.structurizr.export.Diagram;
import com.structurizr.util.WorkspaceUtils;
import com.structurizr.view.AutomaticLayout;
import com.structurizr.view.ThemeUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class D2ExporterTest {
    @Test
    public void test_BigBankPlcExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/structurizr-36141-workspace.json"));
        D2Exporter dotWriter = new D2Exporter();

        Collection<Diagram> diagrams = dotWriter.export(workspace);
        assertEquals(7, diagrams.size());

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        String expected = readFile(new File("./src/test/java/com/github/goto1134/structurizr/export/d2/36141-SystemLandscape.d2"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemContext")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/github/goto1134/structurizr/export/d2/36141-SystemContext.d2"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Containers")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/github/goto1134/structurizr/export/d2/36141-Containers.d2"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Components")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/github/goto1134/structurizr/export/d2/36141-Components.d2"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("SignIn")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/github/goto1134/structurizr/export/d2/36141-SignIn.d2"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("DevelopmentDeployment")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/github/goto1134/structurizr/export/d2/36141-DevelopmentDeployment.d2"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("LiveDeployment")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/github/goto1134/structurizr/export/d2/36141-LiveDeployment.d2"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_AmazonWebServicesExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/structurizr-54915-workspace.json"));
        ThemeUtils.loadThemes(workspace);
        workspace.getViews().getDeploymentViews().iterator().next().enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 300, 300);

        D2Exporter exporter = new D2Exporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(1, diagrams.size());

        Diagram diagram = diagrams.stream().findFirst().get();
        String expected = readFile(new File("./src/test/java/com/github/goto1134/structurizr/export/d2/54915-AmazonWebServicesDeployment.d2"));
        assertEquals(expected, diagram.getDefinition());
    }

    @Test
    public void test_GroupsExample() throws Exception {
        Workspace workspace = WorkspaceUtils.loadWorkspaceFromJson(new File("./src/test/groups.json"));
        ThemeUtils.loadThemes(workspace);

        D2Exporter exporter = new D2Exporter();
        Collection<Diagram> diagrams = exporter.export(workspace);
        assertEquals(3, diagrams.size());

        Diagram diagram = diagrams.stream().filter(md -> md.getKey().equals("SystemLandscape")).findFirst().get();
        String expected = readFile(new File("./src/test/java/com/github/goto1134/structurizr/export/d2/groups-SystemLandscape.d2"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Containers")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/github/goto1134/structurizr/export/d2/groups-Containers.d2"));
        assertEquals(expected, diagram.getDefinition());

        diagram = diagrams.stream().filter(md -> md.getKey().equals("Components")).findFirst().get();
        expected = readFile(new File("./src/test/java/com/github/goto1134/structurizr/export/d2/groups-Components.d2"));
        assertEquals(expected, diagram.getDefinition());
    }


    protected String readFile(File file) throws Exception {
        StringBuilder buf = new StringBuilder();

        Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        List<String> lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);

        for (String line : lines) {
            buf.append(line);
            buf.append("\n");
        }

        if (buf.length() > 1) {
            return buf.toString().substring(0, buf.length() - 1);
        } else {
            return buf.toString();
        }
    }
}
