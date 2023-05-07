package io.github.goto1134.structurizr.export.d2

import com.structurizr.export.Diagram
import com.structurizr.util.WorkspaceUtils
import com.structurizr.view.AutomaticLayout
import com.structurizr.view.ThemeUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.io.File
import kotlin.test.assertEquals


internal class D2ExporterTest {

    private val testDataPath = "./src/test/resources/"
    private fun testFile(name: String) = File("$testDataPath$name")
    private fun testFileText(name: String) = testFile(name).readText().trimEnd()
    private fun assertAllDiagramsMatch(
        heading: String?,
        diagrams: Collection<Diagram>,
        expectedFileNames: (Diagram) -> String
    ) = assertAll(heading, diagrams.map { { assertDiagramTextEquals(expectedFileNames(it), it) } })

    private fun assertDiagramTextEquals(
        expectedFileName: String,
        diagram: Diagram,
        message: String = "${diagram.key} diagram does not match $expectedFileName"
    ) = assertEquals(testFileText(expectedFileName), diagram.definition.trimEnd(), message)

    @Test
    fun test_BigBankPlcExample() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("structurizr-36141-workspace.json"))
        workspace.views.views.forEach { it.addProperty(D2Exporter.D2_ANIMATION, AnimationType.FRAMES.name) }
        val diagrams = D2Exporter().export(workspace)
        assertEquals(7, diagrams.size)
        assertAllDiagramsMatch("BigBankPlc", diagrams) { "bank/${it.key}.d2" }
    }

    @Test
    fun test_Animated_BigBankPlcExample() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("structurizr-36141-workspace.json"))
        workspace.views.views.forEach { it.addProperty(D2Exporter.D2_ANIMATION, AnimationType.D2.name) }
        val diagrams = D2Exporter().export(workspace).filter { it.key != "SignIn" }
        assertEquals(6, diagrams.size)
        assertAllDiagramsMatch("BigBankPlc animated", diagrams) { "bank-animated/${it.key}.d2" }
    }

    @Test
    fun test_AmazonWebServicesExample() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("structurizr-54915-workspace.json"))
        ThemeUtils.loadThemes(workspace)
        workspace.views.deploymentViews.first().apply {
            enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 300, 300)
            addProperty(D2Exporter.D2_ANIMATION, AnimationType.FRAMES.name)
        }
        val diagrams = D2Exporter().export(workspace)
        assertEquals(1, diagrams.size)
        assertEquals(testFileText("amazon/AmazonWebServicesDeployment.d2"), diagrams.first().definition)
    }

    @Test
    fun test_Animated_AmazonWebServicesExample() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("structurizr-54915-workspace.json"))
        ThemeUtils.loadThemes(workspace)
        workspace.views.deploymentViews.first().apply {
            enableAutomaticLayout(AutomaticLayout.RankDirection.LeftRight, 300, 300)
            addProperty(D2Exporter.D2_ANIMATION, AnimationType.D2.name)
        }
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
        assertAllDiagramsMatch("groups", diagrams) { "groups/groups-${it.key}.d2" }
    }

    @Test
    fun test_AnimatedRelation() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("animated-relation/workspace.json"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(1, diagrams.size)
        assertAllDiagramsMatch("animated relation", diagrams) { "animated-relation/animated-relation-${it.key}.d2" }
    }

    @Test
    fun test_TitlePosition() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("title-position/workspace.json"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(3, diagrams.size)
        assertAllDiagramsMatch("title position", diagrams) { "title-position/title-position-${it.key}.d2" }
    }

    @Test
    fun test_FillPattern() {
        val workspace = WorkspaceUtils.loadWorkspaceFromJson(testFile("fill-pattern/workspace.json"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(1, diagrams.size)
        assertAllDiagramsMatch("fill pattern", diagrams) { "fill-pattern/${it.key}.d2" }
    }
}
