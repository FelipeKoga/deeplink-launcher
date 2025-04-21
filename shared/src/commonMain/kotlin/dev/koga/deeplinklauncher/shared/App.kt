package dev.koga.deeplinklauncher.shared

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.koga.deeplinklauncher.designsystem.theme.DLLTheme
import dev.koga.deeplinklauncher.designsystem.theme.Theme
import dev.koga.deeplinklauncher.navigation.AppGraph
import dev.koga.deeplinklauncher.navigation.AppNavigationRoute
import dev.koga.deeplinklauncher.navigation.AppNavigator
import dev.koga.deeplinklauncher.preferences.api.model.AppTheme
import dev.koga.deeplinklauncher.preferences.api.repository.PreferencesRepository
import org.koin.compose.koinInject

@Composable
fun App(
    preferencesRepository: PreferencesRepository = koinInject(),

    ) {
    val preferences by preferencesRepository.preferencesStream.collectAsStateWithLifecycle(
        preferencesRepository.preferences,
    )

    val navController = rememberNavController()
    val appNavigator: AppNavigator = koinInject()
    val appGraph: AppGraph = koinInject()

    LaunchedEffect(Unit) {
        appNavigator.destination.collect { (route, navOptions) ->
            when (route) {
                AppNavigationRoute.Back -> navController.popBackStack()
                else -> navController.navigate(route, navOptions)
            }
        }
    }

    DLLTheme(
        theme = when (preferences.appTheme) {
            AppTheme.DARK -> Theme.DARK
            AppTheme.LIGHT -> Theme.LIGHT
            AppTheme.AUTO -> Theme.AUTO
        },
    ) {
        Surface(
            color = MaterialTheme.colors.background,
        ) {
            NavHost(
                modifier = Modifier.fillMaxSize(),
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

private const val ANIM_DURATION_LONG = 500
private const val ANIM_DURATION_SHORT = 300

fun scaleInEnterTransition() = scaleIn(
    initialScale = .9f,
    animationSpec = tween(ANIM_DURATION_LONG),
) + fadeIn(
    animationSpec = tween(ANIM_DURATION_SHORT),
)

fun scaleOutExitTransition() = scaleOut(
    targetScale = 1.1f,
    animationSpec = tween(ANIM_DURATION_SHORT),
) + fadeOut(
    animationSpec = tween(ANIM_DURATION_SHORT),
)

fun scaleInPopEnterTransition() = scaleIn(
    initialScale = 1.1f,
    animationSpec = tween(ANIM_DURATION_LONG),
) + fadeIn(
    animationSpec = tween(ANIM_DURATION_SHORT),
)

fun scaleOutPopExitTransition() = scaleOut(
    targetScale = .9f,
    animationSpec = tween(ANIM_DURATION_SHORT),
) + fadeOut(
    animationSpec = tween(ANIM_DURATION_SHORT),
)

// @Composable
// private fun NavController.closeKeyboardOnBottomSheetDismiss() {
//    val keyboardController = LocalSoftwareKeyboardController.current
//    LaunchedEffect(Unit) {
//        snapshotFlow {currentDestination?. }
//            .map { isVisible -> !isVisible }
//            .collect { keyboardController?.hide() }
//    }
// }
