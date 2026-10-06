package dev.koga.deeplinklauncher.shared.navigation

import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.runComposeUiTest
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.repository.DeepLinkRepository
import dev.koga.deeplinklauncher.shared.App
import dev.koga.deeplinklauncher.shared.AppInitializer
import dev.koga.deeplinklauncher.shared.di.AppGraph
import dev.koga.deeplinklauncher.shared.start
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.createGraph
import java.nio.file.Files
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

@DependencyGraph(AppScope::class)
internal interface NavigationTourGraph : AppGraph {
    val deepLinkRepository: DeepLinkRepository
}

@OptIn(ExperimentalTestApi::class)
class NavigationTourTest {
    private val home = Files.createTempDirectory("dll-navigation-tour").toFile()
    private val originalHome = System.getProperty("user.home")

    @BeforeTest
    fun setUp() {
        System.setProperty("user.home", home.absolutePath)
    }

    @AfterTest
    fun tearDown() {
        System.setProperty("user.home", originalHome)
        home.deleteRecursively()
    }

    @Test
    fun visitsEveryDestinationAndReturnsHome() = runComposeUiTest {
        val graph = createGraph<NavigationTourGraph>()
        graph.deepLinkRepository.upsertDeepLink(
            DeepLink(
                id = "tour",
                link = "tour://details",
                name = "Tour link",
                description = null,
                isFavorite = false,
            ),
        )
        AppInitializer.start(graph)
        setContent { App() }

        clickText("Got it!")
        awaitGone(hasText("Got it!"))

        clickText("Tour link")
        awaitNode(hasContentDescription("Copy deep link"))
        graph.appNavigator.popBackStack()
        awaitGone(hasContentDescription("Copy deep link"))

        clickText("Folders")
        clickText("Create new folder")
        awaitText("Add folder")
        onNodeWithText("Name").performTextInput("Tour folder")
        clickText("Save")
        clickText("Tour folder")
        awaitText("Link deeplinks")
        clickText("Link deeplinks")
        awaitText("Link deeplink")
        clickBack()
        awaitGone(hasText("Link deeplink"))
        awaitText("Link deeplinks")
        clickBack()
        awaitGone(hasText("Link deeplinks"))

        onNodeWithContentDescription("Settings").performClick()
        awaitText("Theme")
        clickText("Theme")
        awaitText("Choose an option and press to confirm.")
        graph.appNavigator.popBackStack()
        awaitGone(hasText("Choose an option and press to confirm."))

        clickText("Export")
        awaitText("Export DeepLinks")
        clickBack()
        awaitGone(hasText("Export DeepLinks"))
        clickText("Import")
        awaitText("Import DeepLinks")
        clickBack()
        awaitGone(hasText("Import DeepLinks"))
        clickText("Open-source licenses")
        awaitGone(hasText("Theme"))
        clickBack()
        awaitText("Theme")
        clickBack()
        awaitGone(hasText("Theme"))
        awaitText("History")
    }

    private fun ComposeUiTest.clickText(text: String) {
        awaitText(text)
        onNodeWithText(text).performClick()
        waitForIdle()
    }

    private fun ComposeUiTest.clickBack() {
        onNodeWithContentDescription("Back").performClick()
        waitForIdle()
    }

    private fun ComposeUiTest.awaitText(text: String) = awaitNode(hasText(text))

    private fun ComposeUiTest.awaitNode(matcher: SemanticsMatcher) {
        waitUntil(timeoutMillis = 5_000) { onAllNodes(matcher).fetchSemanticsNodes().isNotEmpty() }
    }

    private fun ComposeUiTest.awaitGone(matcher: SemanticsMatcher) {
        waitUntil(timeoutMillis = 5_000) { onAllNodes(matcher).fetchSemanticsNodes().isEmpty() }
    }
}
