package dev.koga.deeplinklauncher.navigation

import androidx.navigation3.runtime.NavKey
import kotlin.test.Test
import kotlin.test.assertEquals

class BackStackTest {

    private data class Route(val name: String) : AppRoute

    private val home = Route("home")
    private val details = Route("details")

    @Test
    fun navigatePushesTheRoute() {
        val backStack = mutableListOf<NavKey>(home)

        backStack.handle(NavigationCommand.Navigate(details))

        assertEquals<List<NavKey>>(listOf(home, details), backStack)
    }

    @Test
    fun navigatingToTheCurrentRouteKeepsASingleCopy() {
        val backStack = mutableListOf<NavKey>(home, details)

        backStack.handle(NavigationCommand.Navigate(details.copy()))

        assertEquals<List<NavKey>>(listOf(home, details), backStack)
    }

    @Test
    fun navigatingToAnEqualRouteDeeperInTheStackStillPushesIt() {
        val backStack = mutableListOf<NavKey>(home, details)

        backStack.handle(NavigationCommand.Navigate(home))

        assertEquals<List<NavKey>>(listOf(home, details, home), backStack)
    }

    @Test
    fun backPopsTheTopRouteButNeverTheRoot() {
        val backStack = mutableListOf<NavKey>(home, details)

        backStack.handle(NavigationCommand.Back)
        backStack.handle(NavigationCommand.Back)

        assertEquals(listOf<NavKey>(home), backStack)
    }
}
