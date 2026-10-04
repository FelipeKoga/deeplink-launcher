package dev.koga.deeplinklauncher.navigation

import androidx.navigation.NavGraphBuilder
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@Inject
public class AppNavGraph(
    private val graphs: Set<NavigationGraph>,
) {
    public val appGraphBuilder: NavGraphBuilder.() -> Unit = {
        graphs.forEach { graph ->
            graph.register(this)
        }
    }
}
