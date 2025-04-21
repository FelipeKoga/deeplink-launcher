package dev.koga.deeplinklauncher.navigation

import androidx.navigation.NavOptions
import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

public interface AppNavigator {
    public val destination: Flow<Pair<AppNavigationRoute, NavOptions>>
    public fun navigate(
        route: AppNavigationRoute,
        navOptions: NavOptions = NavOptions.Builder().build()
    )
}

public fun AppNavigator.back() {
    navigate(AppNavigationRoute.Back)
}

internal class AppNavigatorImpl(
    private val appCoroutineScope: AppCoroutineScope,
) : AppNavigator {
    private val dispatcher = Channel<Pair<AppNavigationRoute, NavOptions>>(Channel.UNLIMITED)
    override val destination: Flow<Pair<AppNavigationRoute, NavOptions>> =
        dispatcher.receiveAsFlow()

    override fun navigate(route: AppNavigationRoute, navOptions: NavOptions) {
        appCoroutineScope.launch {
            dispatcher.send(route to navOptions)
        }
    }
}
