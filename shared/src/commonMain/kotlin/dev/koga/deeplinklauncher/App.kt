package dev.koga.deeplinklauncher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import dev.koga.deeplinklauncher.designsystem.theme.DLLTheme
import dev.koga.deeplinklauncher.designsystem.theme.Theme
import dev.koga.deeplinklauncher.home.ui.screen.HomeScreen
import dev.koga.deeplinklauncher.preferences.api.model.AppTheme
import dev.koga.deeplinklauncher.preferences.api.repository.PreferencesRepository
import kotlinx.coroutines.flow.map
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun App(
    preferencesRepository: PreferencesRepository = koinInject()
) {
    val preferences by preferencesRepository.preferencesStream.collectAsState(
        initial = preferencesRepository.preferences,
    )

    DLLTheme(
        theme = when (preferences.appTheme) {
            AppTheme.DARK -> Theme.DARK
            AppTheme.LIGHT -> Theme.LIGHT
            AppTheme.AUTO -> Theme.AUTO
        },
    ) {
        Navigator(HomeScreen()) { navigator ->
            CompositionLocalProvider(LocalRootNavigator provides navigator) {
                BottomSheetNavigator(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .imePadding(),
                    sheetBackgroundColor = MaterialTheme.colorScheme.surface,
                    sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
                ) { bottomSheetNavigator ->
                    bottomSheetNavigator.closeKeyboardOnBottomSheetDismiss()

                    SlideTransition(navigator) {
                        it.Content()
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomSheetNavigator.closeKeyboardOnBottomSheetDismiss() {
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        snapshotFlow { isVisible }
            .map { isVisible -> !isVisible }
            .collect { keyboardController?.hide() }
    }
}
