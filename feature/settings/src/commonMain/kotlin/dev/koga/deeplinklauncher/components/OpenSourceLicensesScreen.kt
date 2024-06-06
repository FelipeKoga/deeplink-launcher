package dev.koga.deeplinklauncher.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import dev.koga.deeplinklauncher.DLLNavigationIcon
import dev.koga.deeplinklauncher.DLLTopBar
import dev.koga.deeplinklauncher.platform.Platform
import dev.koga.deeplinklauncher.platform.platform
import dev.koga.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

class OpenSourceLicensesScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        var bytes by remember { mutableStateOf(ByteArray(0)) }

        LaunchedEffect(Unit) {
            val filePath = when (platform) {
                Platform.ANDROID -> "files/aboutlibraries.android.json"
                Platform.JVM -> "files/aboutlibraries.desktop.json"
            }

            bytes = Res.readBytes(filePath)
        }

        Scaffold(
            topBar = {
                DLLTopBar(
                    title = "Open Source Licenses",
                    navigationIcon = { DLLNavigationIcon(onClicked = navigator::pop) },
                )
            },
        ) { contentPadding ->
            LibrariesContainer(
                modifier = Modifier.padding(contentPadding),
                aboutLibsJson = bytes.decodeToString(),
                colors = LibraryDefaults.libraryColors(
                    backgroundColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    badgeContentColor = MaterialTheme.colorScheme.onPrimary,
                    badgeBackgroundColor = MaterialTheme.colorScheme.primary,
                    dialogConfirmButtonColor = MaterialTheme.colorScheme.primary,
                ),
            )
        }
    }
}
