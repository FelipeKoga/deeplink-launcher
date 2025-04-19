package dev.koga.deeplinklauncher.coroutines

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class AppCoroutineScope : CoroutineScope {
    private val context = SupervisorJob() + Dispatchers.Main

    override val coroutineContext: CoroutineContext
        get() = context
}