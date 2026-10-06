package dev.koga.deeplinklauncher.settings.impl.ui.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.scene.DialogSceneStrategy
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.navigation.NavigationGraph
import dev.koga.deeplinklauncher.settings.api.ui.navigation.SettingsRouteEntryPoint
import dev.koga.deeplinklauncher.settings.impl.ui.SettingsScreen
import dev.koga.deeplinklauncher.settings.impl.ui.apptheme.AppThemeBottomSheet
import dev.koga.deeplinklauncher.settings.impl.ui.deletedata.DeleteDataBottomSheet
import dev.koga.deeplinklauncher.settings.impl.ui.opensource.OpenSourceLicensesScreen
import dev.koga.deeplinklauncher.settings.impl.ui.products.ProductsBottomSheet
import dev.koga.deeplinklauncher.settings.impl.ui.suggestions.SuggestionsOptionBottomSheet
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metrox.viewmodel.metroViewModel
import kotlinx.serialization.modules.PolymorphicModuleBuilder

@ContributesIntoSet(AppScope::class)
internal class SettingsNavigationGraph(
    private val appNavigator: AppNavigator,
) : NavigationGraph {
    override fun EntryProviderScope<NavKey>.entries() {
        entry<SettingsRouteEntryPoint> {
            SettingsScreen(viewmodel = metroViewModel())
        }

        entry<SettingsRoute.OpenSourceLicenses> {
            OpenSourceLicensesScreen(
                onBack = appNavigator::popBackStack,
            )
        }

        entry<SettingsRoute.AppThemeBottomSheet>(metadata = DialogSceneStrategy.dialog()) {
            AppThemeBottomSheet(
                viewModel = metroViewModel(),
                onDismissRequest = appNavigator::popBackStack,
            )
        }

        entry<SettingsRoute.SuggestionsOptionBottomSheet>(metadata = DialogSceneStrategy.dialog()) {
            SuggestionsOptionBottomSheet(
                viewModel = metroViewModel(),
                onDismissRequest = appNavigator::popBackStack,
            )
        }

        entry<SettingsRoute.DeleteDataBottomSheet>(metadata = DialogSceneStrategy.dialog()) {
            DeleteDataBottomSheet(
                viewModel = metroViewModel(),
                onDismissRequest = appNavigator::popBackStack,
            )
        }

        entry<SettingsRoute.ProductsBottomSheet>(metadata = DialogSceneStrategy.dialog()) {
            ProductsBottomSheet(
                viewModel = metroViewModel(),
                onDismissRequest = appNavigator::popBackStack,
            )
        }
    }

    override fun PolymorphicModuleBuilder<NavKey>.routes() {
        subclass(SettingsRouteEntryPoint::class, SettingsRouteEntryPoint.serializer())
        subclass(SettingsRoute.OpenSourceLicenses::class, SettingsRoute.OpenSourceLicenses.serializer())
        subclass(SettingsRoute.AppThemeBottomSheet::class, SettingsRoute.AppThemeBottomSheet.serializer())
        subclass(SettingsRoute.SuggestionsOptionBottomSheet::class, SettingsRoute.SuggestionsOptionBottomSheet.serializer())
        subclass(SettingsRoute.DeleteDataBottomSheet::class, SettingsRoute.DeleteDataBottomSheet.serializer())
        subclass(SettingsRoute.ProductsBottomSheet::class, SettingsRoute.ProductsBottomSheet.serializer())
    }
}
