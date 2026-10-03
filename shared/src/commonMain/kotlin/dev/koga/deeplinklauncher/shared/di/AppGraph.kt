package dev.koga.deeplinklauncher.shared.di

import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.coroutines.AppDispatchers
import dev.koga.deeplinklauncher.coroutines.CoroutineDebouncer
import dev.koga.deeplinklauncher.navigation.AppNavGraph
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.preferences.repository.PreferencesDataSource
import dev.koga.deeplinklauncher.purchase.api.PurchaseApi
import dev.koga.deeplinklauncher.uievent.SnackBarDispatcher
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

internal interface AppGraph : ViewModelGraph {
    val appCoroutineScope: AppCoroutineScope
    val appDispatchers: AppDispatchers
    val coroutineDebouncer: CoroutineDebouncer
    val appNavigator: AppNavigator
    val appNavGraph: AppNavGraph
    val snackBarDispatcher: SnackBarDispatcher
    val analyticsTracker: AnalyticsTracker
    val preferencesDataSource: PreferencesDataSource
    val purchaseApi: PurchaseApi
}
