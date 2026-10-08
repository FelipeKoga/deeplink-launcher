package dev.koga.deeplinklauncher.shared

import androidx.compose.animation.togetherWith
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.scene.DialogSceneStrategy
import androidx.navigation3.ui.NavDisplay
import dev.koga.deeplinklauncher.designsystem.DLLSnackbarHost
import dev.koga.deeplinklauncher.designsystem.theme.DLLTheme
import dev.koga.deeplinklauncher.home.impl.ui.navigation.HomeRoute
import dev.koga.deeplinklauncher.navigation.AppRoute
import dev.koga.deeplinklauncher.navigation.handle
import dev.koga.deeplinklauncher.navigation.pop
import dev.koga.deeplinklauncher.preferences.model.AppTheme
import dev.koga.deeplinklauncher.shared.analytics.ScreenViewed
import dev.koga.deeplinklauncher.shared.analytics.track
import dev.koga.deeplinklauncher.shared.anim.scaleInEnterTransition
import dev.koga.deeplinklauncher.shared.anim.scaleInPopEnterTransition
import dev.koga.deeplinklauncher.shared.anim.scaleOutExitTransition
import dev.koga.deeplinklauncher.shared.anim.scaleOutPopExitTransition
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory

@Composable
fun App() {
    val appNavigator = appGraph.appNavigator
    val appNavGraph = appGraph.appNavGraph
    val snackBarDispatcher = appGraph.snackBarDispatcher
    val analyticsTracker = appGraph.analyticsTracker
    val isDarkTheme = isAppThemeInDarkTheme()
    val snackBarHostState = remember { SnackbarHostState() }
    val backStack = rememberNavBackStack(appNavGraph.savedStateConfiguration, HomeRoute.Home)
    val currentRoute = backStack.lastOrNull()
    val lifecycle = LocalLifecycleOwner.current.lifecycle

    LaunchedEffect(currentRoute) {
        (currentRoute as? AppRoute)?.analyticsScreenName?.let { screenName ->
            analyticsTracker.track(ScreenViewed(screenName))
        }
    }

    LaunchedEffect(lifecycle) {
        lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
            appNavigator.commands.collect { command -> backStack.handle(command) }
        }
    }

    LaunchedEffect(Unit) {
        snackBarDispatcher.messages.collect { snackBar ->
            snackBarHostState.showSnackbar(
                message = snackBar.message,
            )
        }
    }

    CompositionLocalProvider(LocalMetroViewModelFactory provides appGraph.metroViewModelFactory) {
        DLLTheme(
            isDarkTheme = isDarkTheme,
        ) {
            Scaffold(
                snackbarHost = {
                    DLLSnackbarHost(snackBarHostState)
                },
            ) {
                NavDisplay(
                    backStack = backStack,
                    modifier = Modifier.fillMaxSize().imePadding(),
                    onBack = { backStack.pop() },
                    entryDecorators = listOf(
                        rememberSaveableStateHolderNavEntryDecorator(),
                        rememberViewModelStoreNavEntryDecorator(),
                    ),
                    sceneStrategies = listOf(DialogSceneStrategy()),
                    transitionSpec = { scaleInEnterTransition() togetherWith scaleOutExitTransition() },
                    popTransitionSpec = { scaleInPopEnterTransition() togetherWith scaleOutPopExitTransition() },
                    predictivePopTransitionSpec = { scaleInPopEnterTransition() togetherWith scaleOutPopExitTransition() },
                    entryProvider = entryProvider { with(appNavGraph) { entries() } },
                )
            }
        }
    }
}

@Composable
fun isAppThemeInDarkTheme(): Boolean {
    val preferencesDataSource = appGraph.preferencesDataSource
    val isSystemDarkTheme = isSystemInDarkTheme()

    val preferences by preferencesDataSource.preferencesStream.collectAsStateWithLifecycle(initialValue = null)

    return when (preferences?.appTheme ?: AppTheme.AUTO) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.AUTO -> isSystemDarkTheme
    }
}
