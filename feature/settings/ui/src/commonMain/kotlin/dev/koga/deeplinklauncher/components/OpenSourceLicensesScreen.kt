package dev.koga.deeplinklauncher.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import dev.icerock.moko.resources.compose.readTextAsState
import dev.koga.deeplinklauncher.DLLTopBar
import dev.koga.resources.MR

class OpenSourceLicensesScreen : Screen {
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val licenses by MR.files.aboutlibraries.readTextAsState()

        Scaffold(
            topBar = {
                DLLTopBar(
                    title = "Open Source Licenses",
                    onNavigationActionClicked = navigator::pop,
                )
            },
        ) { contentPadding ->
            LibrariesContainer(
                modifier = Modifier.padding(contentPadding),
                aboutLibsJson = licenses.orEmpty(),
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
