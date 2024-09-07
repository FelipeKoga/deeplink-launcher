package dev.koga.deeplinklauncher

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
import com.mikepenz.aboutlibraries.ui.compose.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.LibraryDefaults
import com.mikepenz.aboutlibraries.ui.compose.rememberLibraries
import dev.koga.deeplinklauncher.platform.Platform
import dev.koga.deeplinklauncher.platform.platform
import dev.koga.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi


object OpenSourceLicensesScreen : Screen {

    @OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalRootNavigator.current

        val libraries by rememberLibraries {
            Res.readBytes("files/aboutlibraries.json").decodeToString()
        }

        Scaffold(
            topBar = {
                DLLTopBar(
                    title = {
                        DLLTopBarDefaults.title("Open Source Licenses")
                    },
                    navigationIcon = {
                        DLLTopBarDefaults.navigationIcon(onClicked = navigator::pop)
                    },
                )
            },
        ) { contentPadding ->
            LibrariesContainer(
                libraries,
                modifier = Modifier.padding(contentPadding),
                colors = LibraryDefaults .libraryColors(
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