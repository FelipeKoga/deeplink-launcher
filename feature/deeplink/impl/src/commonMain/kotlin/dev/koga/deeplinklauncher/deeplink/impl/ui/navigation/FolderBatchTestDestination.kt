package dev.koga.deeplinklauncher.deeplink.impl.ui.navigation

import androidx.navigation.NavGraphBuilder
import dev.koga.deeplinklauncher.navigation.AppNavigator

internal expect fun NavGraphBuilder.registerFolderBatchTestDestination(
    appNavigator: AppNavigator,
)
