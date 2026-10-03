package dev.koga.deeplinklauncher.shared.spike

import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.koga.deeplinklauncher.coroutines.AppDispatchers
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

interface SpikeGraph : ViewModelGraph {
    val analyticsTracker: AnalyticsTracker
    val appDispatchers: AppDispatchers
    val appCoroutineScope: AppCoroutineScope
    val platformName: SpikePlatformName
}
