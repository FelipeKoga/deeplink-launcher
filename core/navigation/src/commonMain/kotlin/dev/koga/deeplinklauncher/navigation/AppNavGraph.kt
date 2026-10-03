package dev.koga.deeplinklauncher.navigation

import androidx.navigation.NavGraphBuilder

public class AppNavGraph(
    private val graphs: Set<NavigationGraph>,
) {
    public val appGraphBuilder: NavGraphBuilder.() -> Unit = {
        graphs.forEach { graph ->
            println(graph)
            graph.register(this)
        }
    }
}
