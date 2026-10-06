package dev.koga.deeplinklauncher.navigation

import dev.koga.deeplinklauncher.coroutines.AppCoroutineScope
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

public sealed interface NavigationCommand {
    public data class Navigate(val route: AppRoute) : NavigationCommand

    public data object Back : NavigationCommand
}

public interface AppNavigator {
    public val commands: Flow<NavigationCommand>
    public fun navigate(route: AppRoute)
    public fun popBackStack()
}

@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
internal class AppNavigatorImpl(
    private val appCoroutineScope: AppCoroutineScope,
) : AppNavigator {
    private val dispatcher = Channel<NavigationCommand>(Channel.UNLIMITED)
    override val commands: Flow<NavigationCommand> = dispatcher.receiveAsFlow()

    override fun navigate(route: AppRoute) {
        send(NavigationCommand.Navigate(route))
    }

    override fun popBackStack() {
        send(NavigationCommand.Back)
    }

    private fun send(command: NavigationCommand) {
        appCoroutineScope.launch {
            dispatcher.send(command)
        }
    }
}
