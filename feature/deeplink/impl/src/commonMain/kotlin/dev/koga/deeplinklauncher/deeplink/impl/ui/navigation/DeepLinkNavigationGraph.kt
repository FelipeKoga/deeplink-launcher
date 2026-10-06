package dev.koga.deeplinklauncher.deeplink.impl.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.DialogSceneStrategy
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.deeplink.impl.ui.addfolder.AddFolderBottomSheet
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.DeepLinkDetailsBottomSheet
import dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.DeepLinkDetailsViewModel
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.FolderDetailsScreen
import dev.koga.deeplinklauncher.deeplink.impl.ui.folderdetails.FolderDetailsViewModel
import dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder.LinkDeepLinkForFolderScreen
import dev.koga.deeplinklauncher.deeplink.impl.ui.linkdeeplinkforfolder.LinkDeepLinkForFolderViewModel
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metrox.viewmodel.assistedMetroViewModel
import kotlinx.serialization.modules.PolymorphicModuleBuilder

@ContributesIntoSet(AppScope::class)
internal class DeepLinkNavigationGraph(
    private val appNavigator: AppNavigator,
) : NavigationGraph {
    override fun EntryProviderScope<NavKey>.entries() {
        entry<DeepLinkRouteEntryPoint.DeepLinkDetails>(metadata = DialogSceneStrategy.dialog()) { route ->
            DeepLinkDetailsBottomSheet(
                viewModel = assistedMetroViewModel<DeepLinkDetailsViewModel, DeepLinkDetailsViewModel.Factory> {
                    create(route)
                },
            )
        }

        entry<DeepLinkRouteEntryPoint.FolderDetails> { route ->
            FolderDetailsScreen(
                viewModel = assistedMetroViewModel<FolderDetailsViewModel, FolderDetailsViewModel.Factory> {
                    create(route)
                },
                appNavigator = appNavigator,
            )
        }

        entry<DeepLinkRouteEntryPoint.PickDeepLinkForFolder> { route ->
            LinkDeepLinkForFolderScreen(
                viewModel = assistedMetroViewModel<LinkDeepLinkForFolderViewModel, LinkDeepLinkForFolderViewModel.Factory> {
                    create(route)
                },
                appNavigator = appNavigator,
            )
        }

        entry<DeepLinkRouteEntryPoint.AddFolder>(metadata = DialogSceneStrategy.dialog()) {
            AddFolderBottomSheet(
                onDismiss = appNavigator::popBackStack,
                viewModel = assistedMetroViewModel(),
            )
        }
    }

    override fun PolymorphicModuleBuilder<NavKey>.routes() {
        subclass(DeepLinkRouteEntryPoint.DeepLinkDetails::class, DeepLinkRouteEntryPoint.DeepLinkDetails.serializer())
        subclass(DeepLinkRouteEntryPoint.FolderDetails::class, DeepLinkRouteEntryPoint.FolderDetails.serializer())
        subclass(
            DeepLinkRouteEntryPoint.PickDeepLinkForFolder::class,
            DeepLinkRouteEntryPoint.PickDeepLinkForFolder.serializer(),
        )
        subclass(DeepLinkRouteEntryPoint.AddFolder::class, DeepLinkRouteEntryPoint.AddFolder.serializer())
    }
}
