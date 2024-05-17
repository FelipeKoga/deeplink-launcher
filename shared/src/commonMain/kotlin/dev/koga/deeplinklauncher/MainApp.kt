package dev.koga.deeplinklauncher

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import dev.koga.deeplinklauncher.screen.HomeScreen
import dev.koga.deeplinklauncher.theme.DLLTheme
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainApp() {
    DLLTheme {
        BottomSheetNavigator(
            modifier = Modifier
                .imePadding()
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            sheetBackgroundColor = MaterialTheme.colorScheme.surface,
            sheetElevation = 12.dp,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        ) {
            it.closeKeyboardOnBottomSheetDismiss()

            Navigator(HomeScreen) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun BottomSheetNavigator.closeKeyboardOnBottomSheetDismiss() {
    val keyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(Unit) {
        snapshotFlow { isVisible }
            .map { isVisible -> !isVisible }
            .collect { keyboardController?.hide() }
    }
}
