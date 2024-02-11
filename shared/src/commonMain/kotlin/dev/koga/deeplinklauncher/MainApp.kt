package dev.koga.deeplinklauncher

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.transitions.SlideTransition
import dev.koga.deeplinklauncher.screen.HomeScreen
import dev.koga.deeplinklauncher.theme.DLLTheme

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainApp() {
    DLLTheme {
        BottomSheetNavigator(
            modifier = Modifier.imePadding(),
            sheetBackgroundColor = MaterialTheme.colorScheme.surface,
            sheetContentColor = Color.White,
            sheetElevation = 12.dp,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        ) {
            Navigator(HomeScreen) { navigator ->
                SlideTransition(navigator)
            }
        }
    }
}