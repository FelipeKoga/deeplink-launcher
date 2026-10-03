package dev.koga.deeplinklauncher.shared

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import dev.koga.deeplinklauncher.designsystem.DLLSnackbarHost
import dev.koga.deeplinklauncher.designsystem.theme.DLLTheme
import dev.koga.deeplinklauncher.home.impl.ui.navigation.HomeRoute
import dev.koga.deeplinklauncher.navigation.AppRoute
import dev.koga.deeplinklauncher.preferences.model.AppTheme
import dev.koga.deeplinklauncher.shared.analytics.ScreenViewed
import dev.koga.deeplinklauncher.shared.analytics.resolveAnalyticsScreenName
import dev.koga.deeplinklauncher.shared.analytics.track
import dev.koga.deeplinklauncher.shared.anim.scaleInEnterTransition
import dev.koga.deeplinklauncher.shared.anim.scaleInPopEnterTransition
import dev.koga.deeplinklauncher.shared.anim.scaleOutExitTransition
import dev.koga.deeplinklauncher.shared.anim.scaleOutPopExitTransition
import dev.zacsweers.metrox.viewmodel.LocalMetroViewModelFactory

@Composable
fun App() {
    val navController = rememberNavController()
    val appNavigator = appGraph.appNavigator
    val appNavGraph = appGraph.appNavGraph
    val snackBarDispatcher = appGraph.snackBarDispatcher
    val analyticsTracker = appGraph.analyticsTracker
    val isDarkTheme = isAppThemeInDarkTheme()
    val snackBarHostState = remember { SnackbarHostState() }
    val currentBackStackEntry by navController.currentBackStackEntryAsState()

    LaunchedEffect(currentBackStackEntry) {
        currentBackStackEntry.resolveAnalyticsScreenName()?.let { screenName ->
            analyticsTracker.track(ScreenViewed(screenName))
        }
    }

    LaunchedEffect(Unit) {
        appNavigator.destination.collect { route ->
            when (route) {
                AppRoute.PopBackStack -> navController.popBackStack()
                else -> navController.navigate(route) {
                    launchSingleTop = true
                }
            }
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
                NavHost(
                    modifier = Modifier.fillMaxSize().imePadding(),
                    navController = navController,
                    startDestination = HomeRoute.Home,
                    enterTransition = { scaleInEnterTransition() },
                    popEnterTransition = { scaleInPopEnterTransition() },
                    exitTransition = { scaleOutExitTransition() },
                    popExitTransition = { scaleOutPopExitTransition() },
                ) {
                    appNavGraph.appGraphBuilder(this)
                }
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
