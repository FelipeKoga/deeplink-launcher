package dev.koga.deeplinklauncher.shared.di

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph

@DependencyGraph(AppScope::class)
internal interface IosAppGraph : AppGraph
