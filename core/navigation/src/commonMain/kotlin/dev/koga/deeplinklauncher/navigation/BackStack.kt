package dev.koga.deeplinklauncher.navigation

import androidx.navigation3.runtime.NavKey

public fun MutableList<NavKey>.handle(command: NavigationCommand) {
    when (command) {
        is NavigationCommand.Navigate -> if (lastOrNull() != command.route) add(command.route)
        NavigationCommand.Back -> pop()
    }
}

public fun MutableList<NavKey>.pop() {
    if (size > 1) removeAt(lastIndex)
}
