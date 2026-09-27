package dev.koga.deeplinklauncher.coroutines

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingCommand
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart

fun SharingStarted.Companion.startNowThenWhileSubscribed(
    stopTimeoutMillis: Long = 5_000,
): SharingStarted = object : SharingStarted {
    private val whileSubscribed = SharingStarted.WhileSubscribed(stopTimeoutMillis)

    override fun command(subscriptionCount: StateFlow<Int>): Flow<SharingCommand> =
        whileSubscribed.command(subscriptionCount).onStart { emit(SharingCommand.START) }
}
