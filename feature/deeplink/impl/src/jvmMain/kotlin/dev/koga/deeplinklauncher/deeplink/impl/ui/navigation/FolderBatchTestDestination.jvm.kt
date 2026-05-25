package dev.koga.deeplinklauncher.deeplink.impl.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderbatchtest.FolderBatchTestScreen
import dev.koga.deeplinklauncher.navigation.AppNavigator
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

internal actual fun NavGraphBuilder.registerFolderBatchTestDestination(
    appNavigator: AppNavigator,
) {
    composable<DeepLinkRouteEntryPoint.FolderBatchTest> {
        FolderBatchTestScreen(
            viewModel = koinViewModel(),
            appNavigator = koinInject(),
        )
    }
}
