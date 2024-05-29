package dev.koga.deeplinklauncher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
import dev.koga.deeplinklauncher.datasource.PreferencesDataSource
import dev.koga.deeplinklauncher.model.AppTheme
import dev.koga.deeplinklauncher.screen.HomeScreen
import dev.koga.deeplinklauncher.theme.DLLTheme
import dev.koga.deeplinklauncher.theme.Theme
import kotlinx.coroutines.flow.map
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainApp() {
    val preferencesDataSource: PreferencesDataSource = koinInject()

    val preferences by preferencesDataSource.preferencesStream.collectAsState(
        initial = preferencesDataSource.preferences,
    )

    DLLTheme(
        theme = when (preferences.appTheme) {
            AppTheme.DARK -> Theme.DARK
            AppTheme.LIGHT -> Theme.LIGHT
            AppTheme.AUTO -> Theme.AUTO
        },
    ) {
        BottomSheetNavigator(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .imePadding(),
            sheetBackgroundColor = MaterialTheme.colorScheme.surface,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        ) {
            it.closeKeyboardOnBottomSheetDismiss()

            Navigator(HomeScreen()) { navigator ->
                SlideTransition(navigator)
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
