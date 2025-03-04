package io.github.goto1134.structurizr.export.d2

import com.structurizr.Workspace
import com.structurizr.dsl.StructurizrDslParser
import com.structurizr.export.Diagram
import com.structurizr.view.ThemeUtils
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertNotNull


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
        val workspace = parseWorkspace(testFile("bank/workspace.dsl"))
        useFramesAnimation(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(7, diagrams.size)
        assertAllDiagramsMatch("bank/default", diagrams)
    }

    @Test
    fun test_Animated_BigBankPlcExample() {
        val workspace = parseWorkspace(testFile("bank/workspace.dsl"))
        val diagrams = D2Exporter().export(workspace).filterNot { it.key == "SignIn" }
        assertEquals(6, diagrams.size)
        assertAllDiagramsMatch("bank/animated", diagrams)
    }

    @Test
    fun test_AmazonWebServicesExample() {
        val workspace = parseWorkspace(testFile("amazon/workspace.dsl"))
        ThemeUtils.loadThemes(workspace)
        useFramesAnimation(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(1, diagrams.size)
        assertEquals(testFileText("amazon/default/AmazonWebServicesDeployment.d2"), diagrams.first().definition)
    }

    @Test
    fun test_Animated_AmazonWebServicesExample() {
        val workspace = parseWorkspace(testFile("amazon/workspace.dsl"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(1, diagrams.size)
        assertEquals(testFileText("amazon/animated/AmazonWebServicesDeployment.d2"), diagrams.first().definition)
    }

    @Test
    fun test_GroupsExample() {
        val workspace = parseWorkspace(testFile("groups/workspace.dsl"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(3, diagrams.size)
        assertAllDiagramsMatch("groups/default", diagrams)
    }

    @Test
    fun test_NestedGroupsExample() {
        val workspace = parseWorkspace(testFile("groups-nested/workspace.dsl"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(1, diagrams.size)
        assertAllDiagramsMatch("groups-nested/default", diagrams)
    }

    @Test
    fun test_AnimatedRelation() {
        val workspace = parseWorkspace(testFile("relation/workspace.dsl"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(1, diagrams.size)
        assertAllDiagramsMatch("relation/animated", diagrams)
    }

    @Test
    fun test_TitlePosition() {
        val workspace = parseWorkspace(testFile("title-position/workspace.dsl"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(3, diagrams.size)
        assertAllDiagramsMatch("title-position/default", diagrams)
    }

    private fun parseWorkspace(testFile: File): Workspace {
        val parser = StructurizrDslParser()
        parser.parse(testFile)
        val workspace = parser.workspace
        assertNotNull(workspace)
        return workspace
    }

    @Test
    fun test_FillPattern() {
        val workspace = parseWorkspace(testFile("fill-pattern/workspace.dsl"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(1, diagrams.size)
        assertAllDiagramsMatch("fill-pattern/default", diagrams)
    }

    @Test
    fun test_SoftwareSystemGroups() {
        val workspace = parseWorkspace(testFile("software-system-groups/workspace.dsl"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(2, diagrams.size)
        assertAllDiagramsMatch("software-system-groups/default", diagrams)
    }

    @Test
    fun test_C4Person() {
        val workspace = parseWorkspace(testFile("c4-person/workspace.dsl"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(2, diagrams.size)
        assertAllDiagramsMatch("c4-person/default", diagrams)
    }

    @Test
    fun test_C4PersonViewSet() {
        val workspace = parseWorkspace(testFile("c4-person-view-set/workspace.dsl"))
        ThemeUtils.loadThemes(workspace)
        val diagrams = D2Exporter().export(workspace)
        assertEquals(2, diagrams.size)
        assertAllDiagramsMatch("c4-person-view-set/default", diagrams)
    }
}
