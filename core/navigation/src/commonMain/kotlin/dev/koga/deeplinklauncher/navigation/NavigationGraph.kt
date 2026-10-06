package dev.koga.deeplinklauncher.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.modules.PolymorphicModuleBuilder

public interface NavigationGraph {
    public fun EntryProviderScope<NavKey>.entries()

    public fun PolymorphicModuleBuilder<NavKey>.routes()
}
