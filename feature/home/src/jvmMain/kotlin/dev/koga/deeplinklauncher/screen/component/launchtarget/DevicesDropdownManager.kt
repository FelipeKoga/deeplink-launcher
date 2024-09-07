package dev.koga.deeplinklauncher.screen.component.launchtarget

import dev.koga.deeplinklauncher.datasource.TargetDataSource
import dev.koga.deeplinklauncher.model.Target
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class DevicesDropdownManager(
    private val targetDataSource: TargetDataSource,
    coroutineScope: CoroutineScope,
) {
    val uiState = combine(
        targetDataSource.current,
        targetDataSource.track()
    ) { current, targets ->
        targets.toUiState(current)
    }.stateIn(
        scope = coroutineScope,
        initialValue = DevicesUiState(),
        started = SharingStarted.WhileSubscribed()
    )

    fun select(target: Target) {
        targetDataSource.select(target)
    }

    fun next() {
        targetDataSource.next()
    }

    fun prev() {
        targetDataSource.prev()
    }
}
