package dev.koga.deeplinklauncher.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

data class AppDispatchers(
    val io: CoroutineDispatcher = Dispatchers.IO,
)
