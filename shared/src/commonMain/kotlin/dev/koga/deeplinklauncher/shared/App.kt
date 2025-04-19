package dev.koga.deeplinklauncher.shared

import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import dev.koga.deeplinklauncher.DLLNavigator
import dev.koga.deeplinklauncher.designsystem.theme.DLLTheme
import dev.koga.deeplinklauncher.designsystem.theme.Theme
import dev.koga.deeplinklauncher.home.ui.navigation.homeNavGraph
import dev.koga.deeplinklauncher.navigation.Route
import dev.koga.deeplinklauncher.preferences.api.model.AppTheme
import dev.koga.deeplinklauncher.preferences.api.repository.PreferencesRepository
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun App(
    preferencesRepository: PreferencesRepository = koinInject(),
) {
    val preferences by preferencesRepository.preferencesStream.collectAsState(
        initial = preferencesRepository.preferences,
    )

    val navController = rememberNavController()
    val navigator = koinInject<DLLNavigator>()

    LaunchedEffect(Unit) {
        navigator.destinationStream.collect(navController::navigate)
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
            startDestination = Route.Home
        ) {
            homeNavGraph()
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
