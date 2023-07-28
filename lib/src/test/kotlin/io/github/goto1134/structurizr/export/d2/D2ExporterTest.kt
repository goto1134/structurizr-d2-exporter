package io.github.goto1134.structurizr.export.d2

import com.structurizr.Workspace
import com.structurizr.export.Diagram
import com.structurizr.util.WorkspaceUtils
import com.structurizr.view.ThemeUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.io.File
import kotlin.test.assertEquals


internal class D2ExporterTest {

    private val testDataPath = "./src/test/resources/"
    private fun testFile(name: String) = File("$testDataPath$name")
    private fun testFileText(name: String) = testFile(name).readText().trimEnd()
    private fun assertAllDiagramsMatch(folderName: String, diagrams: Collection<Diagram>) =
        assertAll(folderName, diagrams.map { { assertDiagramTextEquals("$folderName/${it.key}.d2", it) } })

    private fun assertDiagramTextEquals(
        expectedFileName: String,
        diagram: Diagram,
        message: String = "${diagram.key} diagram does not match $expectedFileName"
    ) = assertEquals(testFileText(expectedFileName), diagram.definition.trimEnd(), message)

    private fun useFramesAnimation(workspace: Workspace) {
        workspace.views.views.forEach { it.addProperty(D2Exporter.D2_ANIMATION, AnimationType.FRAMES.name) }
    }

    @Test
    fun test_BigBankPlcExample() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("bank.json"))
        useFramesAnimation(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(7, diagrams.size)
        assertAllDiagramsMatch("bank", diagrams)
    }

    @Test
    fun test_Animated_BigBankPlcExample() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("bank.json"))
        val diagrams = D2Exporter().export(workspace).filterNot { it.key == "SignIn" }
        assertEquals(6, diagrams.size)
        assertAllDiagramsMatch("bank-animated", diagrams)
    }

    @Test
    fun test_AmazonWebServicesExample() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("amazon.json"))
        ThemeUtils.loadThemes(workspace)
        useFramesAnimation(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(1, diagrams.size)
        assertEquals(testFileText("amazon/AmazonWebServicesDeployment.d2"), diagrams.first().definition)
    }

    @Test
    fun test_Animated_AmazonWebServicesExample() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("amazon.json"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(1, diagrams.size)
        assertEquals(testFileText("amazon-animated/AmazonWebServicesDeployment.d2"), diagrams.first().definition)
    }

    @Test
    fun test_GroupsExample() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("groups.json"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(3, diagrams.size)
        assertAllDiagramsMatch("groups", diagrams)
    }

    @Test
    fun test_NestedGroupsExample() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("groups-nested/groups-nested.json"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(1, diagrams.size)
        assertAllDiagramsMatch("groups-nested", diagrams)
    }

    @Test
    fun test_AnimatedRelation() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("animated-relation/workspace.json"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(1, diagrams.size)
        assertAllDiagramsMatch("animated-relation", diagrams)
    }

    @Test
    fun test_TitlePosition() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("title-position/workspace.json"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(3, diagrams.size)
        assertAllDiagramsMatch("title-position", diagrams)
    }

    @Test
    fun test_FillPattern() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("fill-pattern/workspace.json"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(1, diagrams.size)
        assertAllDiagramsMatch("fill-pattern", diagrams)
    }

    @Test
    fun test_SoftwareSystemGroups() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("software-system-groups/workspace.json"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(2, diagrams.size)
        assertAllDiagramsMatch("software-system-groups", diagrams)
    }
}
