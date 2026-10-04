package dev.koga.deeplinklauncher.database.di

import dev.koga.deeplinklauncher.database.DatabaseProvider
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
import dev.koga.deeplinklauncher.database.JvmDriverFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.BindingContainer
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
@BindingContainer
object DatabaseBindings {
    @Provides
    @SingleIn(AppScope::class)
    fun database(): DeepLinkLauncherDatabase = DatabaseProvider(JvmDriverFactory()).create()
}
