package dev.koga.deeplinklauncher.navigation

import androidx.navigation.NavGraphBuilder

public class AppGraph(
    private val graphs: List<NavigationGraph>,
) {
    public val appGraphBuilder: NavGraphBuilder.() -> Unit = {
        graphs.forEach { graph ->
            println(graph)
            graph.register(this)
        }
    }
}
