package dev.koga.deeplinklauncher.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CoroutineDebouncer {
    private val jobs = mutableMapOf<String, Job>()

    fun debounce(
        coroutineScope: CoroutineScope,
        key: String,
        delayMillis: Long = 300L,
        action: suspend () -> Unit,
    ) {
        jobs[key]?.cancel()
        jobs[key] = coroutineScope.launch {
            delay(delayMillis)
            action()
        }
    }
}