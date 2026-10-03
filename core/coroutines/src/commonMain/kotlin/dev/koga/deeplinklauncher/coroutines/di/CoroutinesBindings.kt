package dev.koga.deeplinklauncher.coroutines.di

import dev.koga.deeplinklauncher.coroutines.AppDispatchers
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
@BindingContainer
object CoroutinesBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun appDispatchers(): AppDispatchers = AppDispatchers()
}
