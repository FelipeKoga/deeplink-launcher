package dev.koga.deeplinklauncher.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@SingleIn(AppScope::class)
@Inject
public class AppNavGraph(
    private val graphs: Set<NavigationGraph>,
) {
    public val savedStateConfiguration: SavedStateConfiguration = SavedStateConfiguration {
        serializersModule = SerializersModule {
            polymorphic(NavKey::class) {
                graphs.forEach { graph -> with(graph) { routes() } }
            }
        }
    }

    public fun EntryProviderScope<NavKey>.entries() {
        graphs.forEach { graph -> with(graph) { entries() } }
    }
}
