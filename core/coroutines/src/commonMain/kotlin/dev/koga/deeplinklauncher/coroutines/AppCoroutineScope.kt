package dev.koga.deeplinklauncher.coroutines

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

@SingleIn(AppScope::class)
@Inject
class AppCoroutineScope : CoroutineScope {
    private val context = SupervisorJob() + Dispatchers.Main

    override val coroutineContext: CoroutineContext
        get() = context
}
