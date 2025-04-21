package dev.koga.deeplinklauncher.shared

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
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
    val     appNavigator: AppNavigator = koinInject()
    val appGraph: AppGraph = koinInject()

    LaunchedEffect(Unit) {
        appNavigator.destination.collect {
            when (it) {
                AppNavigationRoute.Back -> navController.navigate(it)
                else -> navController.popBackStack()
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
        NavHost(
            navController = navController,
            startDestination = AppNavigationRoute.Home
        ) {
            appGraph.appGraphBuilder(this)
        }

//        Navigator(HomeScreen()) { navigator ->
//            CompositionLocalProvider(LocalRootNavigator provides navigator) {
//                BottomSheetNavigator(
//                    modifier = Modifier
//                        .fillMaxSize()
//                        .imePadding(),
//                    sheetBackgroundColor = MaterialTheme.colorScheme.surface,
//                    sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
//                ) { bottomSheetNavigator ->
//                    bottomSheetNavigator.closeKeyboardOnBottomSheetDismiss()
//
//                    SlideTransition(navigator) {
//                        it.Content()
//                    }
//                }
//            }
//        }
    }
}

//@Composable
//private fun NavController.closeKeyboardOnBottomSheetDismiss() {
//    val keyboardController = LocalSoftwareKeyboardController.current
//    LaunchedEffect(Unit) {
//        snapshotFlow {currentDestination?. }
//            .map { isVisible -> !isVisible }
//            .collect { keyboardController?.hide() }
//    }
//}
