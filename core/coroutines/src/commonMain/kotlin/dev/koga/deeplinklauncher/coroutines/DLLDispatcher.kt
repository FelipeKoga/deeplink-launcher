package dev.koga.deeplinklauncher.coroutines

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class DLLDispatcher {
    val io = Dispatchers.IO

    val main = Dispatchers.Main

    val default = Dispatchers.Default
}