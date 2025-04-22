package dev.koga.deeplinklauncher.shared

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.koga.deeplinklauncher.designsystem.theme.DLLTheme
import dev.koga.deeplinklauncher.navigation.AppGraph
import dev.koga.deeplinklauncher.navigation.AppNavigationRoute
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.preferences.api.model.AppTheme
import dev.koga.deeplinklauncher.preferences.api.repository.PreferencesRepository
import dev.koga.deeplinklauncher.shared.anim.scaleInEnterTransition
import dev.koga.deeplinklauncher.shared.anim.scaleInPopEnterTransition
import dev.koga.deeplinklauncher.shared.anim.scaleOutExitTransition
import dev.koga.deeplinklauncher.shared.anim.scaleOutPopExitTransition
import dev.koga.deeplinklauncher.uievent.SnackBarDispatcher
import org.koin.compose.koinInject

@Composable
fun App() {
    val navController = rememberNavController()
    val appNavigator = koinInject<AppNavigator>()
    val appGraph = koinInject<AppGraph>()
    val snackBarDispatcher = koinInject<SnackBarDispatcher>()
    val isDarkTheme = rememberAppDarkMode()
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        appNavigator.destination.collect { route ->
            when (route) {
                AppNavigationRoute.Back -> navController.popBackStack()
                else -> navController.navigate(route) {
                    launchSingleTop = true
                }
            }
        }

        snackBarDispatcher.messages.collect { snackBar ->
            snackBarHostState.showSnackbar(
                message = snackBar.message,
            )
        }
    }

    DLLTheme(
        isDarkTheme = isDarkTheme,
    ) {
        Scaffold(
            snackbarHost = {
                SnackbarHost(snackBarHostState)
            }
        ) {
            NavHost(
                modifier = Modifier.fillMaxSize().imePadding(),
                navController = navController,
                startDestination = AppNavigationRoute.Home,
                enterTransition = { scaleInEnterTransition() },
                popEnterTransition = { scaleInPopEnterTransition() },
                exitTransition = { scaleOutExitTransition() },
                popExitTransition = { scaleOutPopExitTransition() },
            ) {
                appGraph.appGraphBuilder(this)
            }
        }

    }
}

@Composable
private fun rememberAppDarkMode(
    preferencesRepository: PreferencesRepository = koinInject(),
): Boolean {
    val isSystemDarkTheme = isSystemInDarkTheme()

    val preferences by remember(preferencesRepository) {
        preferencesRepository.preferencesStream
    }.collectAsStateWithLifecycle(preferencesRepository.preferences)

    return when (preferences.appTheme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.AUTO -> isSystemDarkTheme
    }
}