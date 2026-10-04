package dev.koga.deeplinklauncher.home.impl.ui.component.targets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkTargetStateManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkTarget
import dev.koga.deeplinklauncher.home.impl.analytics.LaunchTargetSelected
import dev.koga.deeplinklauncher.home.impl.analytics.track
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

@ViewModelKey
@ContributesIntoMap(AppScope::class)
class DeepLinkTargetsDropdownViewModel(
    private val stateManager: DeepLinkTargetStateManager,
    private val analyticsTracker: AnalyticsTracker,
) : ViewModel() {

    val uiState = combine(
        stateManager.current,
        stateManager.targets,
    ) { current, targets ->
        targets.toUiState(current)
    }.stateIn(
        scope = viewModelScope,
        initialValue = DeepLinkTargetsUiState(),
        started = SharingStarted.WhileSubscribed(),
    )

    fun select(target: DeepLinkTarget) {
        analyticsTracker.track(
            LaunchTargetSelected(
                targetType = when (target) {
                    DeepLinkTarget.Desktop -> "desktop"
                    is DeepLinkTarget.Device -> "device"
                },
            ),
        )
        stateManager.select(target)
    }

    fun next() {
        stateManager.next()
    }

    fun prev() {
        stateManager.prev()
    }
}
