package dev.koga.deeplinklauncher.navigation

import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

public interface AppNavigator {
    public val destination: Flow<AppNavigationRoute>
    public fun navigate(route: AppNavigationRoute)
}

internal class AppNavigatorImpl(
    private val appCoroutineScope: AppCoroutineScope,
) : AppNavigator {
    private val dispatcher = Channel<AppNavigationRoute>(Channel.UNLIMITED)
    override val destination: Flow<AppNavigationRoute> = dispatcher.receiveAsFlow()

    override fun navigate(route: AppNavigationRoute) {
        appCoroutineScope.launch {
            dispatcher.send(route)
        }
    }
}