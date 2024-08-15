package dev.koga.deeplinklauncher.screen.component.launchtarget

import dev.koga.deeplinklauncher.datasource.TargetDataSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class LaunchTargetManager(
    private val targetDataSource: TargetDataSource
) {

    private val coroutines = CoroutineScope(Dispatchers.IO)

    val uiState = combine(
        targetDataSource.current,
        targetDataSource.track()
    ) { current, targets ->
        LaunchTargetUiState(
            targets = targets.toUiState(current),
            selected = current.toUiState(selected = true)
        )

    }.stateIn(
        scope = coroutines,
        initialValue = LaunchTargetUiState(),
        started = SharingStarted.WhileSubscribed()
    )

    fun select(name: String) {
        targetDataSource.select(name)
    }
}