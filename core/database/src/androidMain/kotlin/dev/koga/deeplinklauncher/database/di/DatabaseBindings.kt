package dev.koga.deeplinklauncher.database.di

import android.content.Context
import dev.koga.deeplinklauncher.database.AndroidDriverFactory
import dev.koga.deeplinklauncher.database.DatabaseProvider
import dev.koga.deeplinklauncher.database.DeepLinkLauncherDatabase
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
    fun database(context: Context): DeepLinkLauncherDatabase = DatabaseProvider(AndroidDriverFactory(context)).create()
}
