package dev.koga.deeplinklauncher.screen.component.targets

import dev.koga.deeplinklauncher.TargetsTracker
import dev.koga.deeplinklauncher.model.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DeeplinkTargetsDropdownManager(
    private val targetsTracker: TargetsTracker,
    coroutineScope: CoroutineScope,
) {

    val uiState = combine(
        targetsTracker.current,
        targetsTracker.targets,
    ) { current, targets ->
        targets.toUiState(current)
    }.stateIn(
        scope = coroutineScope,
        initialValue = DeeplinkTargetsUiState(),
        started = SharingStarted.WhileSubscribed(),
    )

    fun select(target: Target) {
        targetsTracker.select(target)
    }

    fun next() {
        targetsTracker.next()
    }

    fun prev() {
        targetsTracker.prev()
    }
}
