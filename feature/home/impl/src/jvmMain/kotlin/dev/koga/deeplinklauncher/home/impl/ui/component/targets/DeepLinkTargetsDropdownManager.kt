package dev.koga.deeplinklauncher.home.impl.ui.component.targets

import dev.koga.deeplinklauncher.analytics.api.AnalyticsTracker
import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkTargetStateManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkTarget
import dev.koga.deeplinklauncher.home.impl.analytics.LaunchTargetSelected
import dev.koga.deeplinklauncher.home.impl.analytics.track
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DeepLinkTargetsDropdownManager(
    private val stateManager: DeepLinkTargetStateManager,
    private val analyticsTracker: AnalyticsTracker,
    coroutineScope: CoroutineScope,
) {

    val uiState = combine(
        stateManager.current,
        stateManager.targets,
    ) { current, targets ->
        targets.toUiState(current)
    }.stateIn(
        scope = coroutineScope,
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
