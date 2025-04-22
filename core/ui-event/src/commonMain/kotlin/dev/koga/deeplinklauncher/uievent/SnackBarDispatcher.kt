package dev.koga.deeplinklauncher.uievent

import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

interface SnackBarDispatcher {
    val messages: Flow<SnackBar>
    fun show(message: SnackBar)
    fun show(message: String)
}

data class SnackBar(
    val message: String,
    val variant: Variant = Variant.INFO,
) {
    enum class Variant {
        SUCCESS,
        ERROR,
        INFO
    }
}


internal class SnackBarDispatcherImpl(
    private val appCoroutineScope: AppCoroutineScope
) : SnackBarDispatcher {
    private val _dispatcher = Channel<SnackBar>(Channel.UNLIMITED)
    override val messages: Flow<SnackBar> = _dispatcher.receiveAsFlow()

    override fun show(message: SnackBar) {
        appCoroutineScope.launch {
            _dispatcher.send(message)
        }
    }

    override fun show(message: String) {
        show(SnackBar(message))
    }
}