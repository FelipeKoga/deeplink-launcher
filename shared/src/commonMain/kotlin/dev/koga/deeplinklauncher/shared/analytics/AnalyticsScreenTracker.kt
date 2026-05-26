package dev.koga.deeplinklauncher.shared.analytics

import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import dev.koga.deeplinklauncher.datatransfer.api.ui.navigation.DataTransferRoute
import dev.koga.deeplinklauncher.deeplink.api.ui.navigation.DeepLinkRouteEntryPoint
import dev.koga.deeplinklauncher.home.impl.ui.navigation.HomeRoute
import dev.koga.deeplinklauncher.settings.api.ui.navigation.SettingsRouteEntryPoint
import dev.koga.deeplinklauncher.settings.impl.ui.navigation.SettingsRoute

internal fun NavBackStackEntry?.resolveAnalyticsScreenName(): String? {
    val entry = this ?: return null

    return runCatching { entry.toRoute<HomeRoute.Home>().analyticsScreenName }.getOrNull()
        ?: runCatching { entry.toRoute<HomeRoute.Onboarding>().analyticsScreenName }.getOrNull()
        ?: runCatching { entry.toRoute<DeepLinkRouteEntryPoint.AddFolder>().analyticsScreenName }.getOrNull()
        ?: runCatching { entry.toRoute<DeepLinkRouteEntryPoint.FolderDetails>().analyticsScreenName }.getOrNull()
        ?: runCatching { entry.toRoute<DeepLinkRouteEntryPoint.PickDeepLinkForFolder>().analyticsScreenName }.getOrNull()
        ?: runCatching { entry.toRoute<DeepLinkRouteEntryPoint.DeepLinkDetails>().analyticsScreenName }.getOrNull()
        ?: runCatching { entry.toRoute<DataTransferRoute.ImportData>().analyticsScreenName }.getOrNull()
        ?: runCatching { entry.toRoute<DataTransferRoute.ExportData>().analyticsScreenName }.getOrNull()
        ?: runCatching { entry.toRoute<SettingsRouteEntryPoint>().analyticsScreenName }.getOrNull()
        ?: runCatching { entry.toRoute<SettingsRoute.OpenSourceLicenses>().analyticsScreenName }.getOrNull()
        ?: runCatching { entry.toRoute<SettingsRoute.AppThemeBottomSheet>().analyticsScreenName }.getOrNull()
        ?: runCatching { entry.toRoute<SettingsRoute.SuggestionsOptionBottomSheet>().analyticsScreenName }.getOrNull()
        ?: runCatching { entry.toRoute<SettingsRoute.DeleteDataBottomSheet>().analyticsScreenName }.getOrNull()
        ?: runCatching { entry.toRoute<SettingsRoute.ProductsBottomSheet>().analyticsScreenName }.getOrNull()
}
