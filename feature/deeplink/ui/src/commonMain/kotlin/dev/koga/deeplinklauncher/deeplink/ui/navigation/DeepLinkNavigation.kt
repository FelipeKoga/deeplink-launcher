package dev.koga.deeplinklauncher.deeplink.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.DeepLinkDetailsBottomSheet
import dev.koga.deeplinklauncher.deeplink.ui.deeplink.screen.details.addfolder.AddFolderBottomSheet
import dev.koga.deeplinklauncher.deeplink.ui.folder.screen.details.FolderDetailsScreen
import dev.koga.deeplinklauncher.navigation.AppNavigationRoute
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.koga.deeplinklauncher.navigation.back
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

class DeepLinkNavigation(
    private val appNavigator: AppNavigator,
) : NavigationGraph {
    override fun register(navGraphBuilder: NavGraphBuilder) = with(navGraphBuilder) {
        dialog<AppNavigationRoute.DeepLinkDetails> {
            DeepLinkDetailsBottomSheet(
                viewModel = koinViewModel(),
            )
        }

        composable<AppNavigationRoute.FolderDetails> {
            FolderDetailsScreen(
                viewModel = koinViewModel(),
                appNavigator = koinInject(),
            )
        }

        dialog<AppNavigationRoute.AddFolder> {
            AddFolderBottomSheet(
                onDismiss =  appNavigator::back,
                viewModel = koinViewModel()
            )
        }

        dialog<AppNavigationRoute.AddFolder> {
            AddFolderBottomSheet(
                onDismiss =  appNavigator::back,
                viewModel = koinViewModel()
            )
        }
    }
}
