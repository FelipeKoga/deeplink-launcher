package dev.koga.deeplinklauncher.deeplink.impl.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.deeplink.impl.ui.addfolder.AddFolderBottomSheet
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.DeepLinkDetailsBottomSheet
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.FolderDetailsScreen
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.koga.deeplinklauncher.navigation.popBackStack
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

internal class DeepLinkNavigationGraph(
    private val appNavigator: AppNavigator,
) : NavigationGraph {
    override fun register(navGraphBuilder: NavGraphBuilder) = with(navGraphBuilder) {
        dialog<DeepLinkRouteEntryPoint.DeepLinkDetails> {
            DeepLinkDetailsBottomSheet(
                viewModel = koinViewModel(),
            )
        }

        composable<DeepLinkRouteEntryPoint.FolderDetails> {
            FolderDetailsScreen(
                viewModel = koinViewModel(),
                appNavigator = koinInject(),
            )
        }

        dialog<DeepLinkRouteEntryPoint.AddFolder> {
            AddFolderBottomSheet(
                onDismiss = appNavigator::popBackStack,
                viewModel = koinViewModel(),
            )
        }
    }
}
