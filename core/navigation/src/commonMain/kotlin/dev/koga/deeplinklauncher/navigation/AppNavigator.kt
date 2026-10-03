package dev.koga.deeplinklauncher.navigation

import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

public interface AppNavigator {
    public val destination: Flow<AppRoute>
    public fun navigate(route: AppRoute)

    public fun popBackStack() {
        navigate(AppRoute.PopBackStack)
    }
}

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class AppNavigatorImpl(
    private val appCoroutineScope: AppCoroutineScope,
) : AppNavigator {
    private val dispatcher = Channel<AppRoute>(Channel.UNLIMITED)
    override val destination: Flow<AppRoute> =
        dispatcher.receiveAsFlow()

    override fun navigate(route: AppRoute) {
        appCoroutineScope.launch {
            dispatcher.send(route)
        }
    }
}
