package dev.koga.deeplinklauncher.deeplink.impl.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.deeplink.impl.ui.addfolder.AddFolderBottomSheet
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.DeepLinkDetailsBottomSheet
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.FolderDetailsScreen
import dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder.LinkDeepLinkForFolderScreen
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel

@ContributesIntoSet(AppScope::class)
internal class DeepLinkNavigationGraph(
    private val appNavigator: AppNavigator,
) : NavigationGraph {
    override fun register(navGraphBuilder: NavGraphBuilder) = with(navGraphBuilder) {
        dialog<DeepLinkRouteEntryPoint.DeepLinkDetails> {
            DeepLinkDetailsBottomSheet(
                viewModel = assistedMetroViewModel(),
            )
        }

        composable<DeepLinkRouteEntryPoint.FolderDetails> {
            FolderDetailsScreen(
                viewModel = assistedMetroViewModel(),
                appNavigator = appNavigator,
            )
        }

        composable<DeepLinkRouteEntryPoint.PickDeepLinkForFolder> {
            LinkDeepLinkForFolderScreen(
                viewModel = assistedMetroViewModel(),
                appNavigator = appNavigator,
            )
        }

        dialog<DeepLinkRouteEntryPoint.AddFolder> {
            AddFolderBottomSheet(
                onDismiss = appNavigator::popBackStack,
                viewModel = assistedMetroViewModel(),
            )
        }
    }
}
