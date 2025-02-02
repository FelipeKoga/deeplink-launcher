package dev.koga.deeplinklauncher.settings.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import cafe.adriel.voyager.core.screen.Screen
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.rememberLibraries
import dev.koga.deeplinklauncher.LocalRootNavigator
import dev.koga.deeplinklauncher.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.designsystem.DLLTopBarDefaults
import dev.koga.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

object OpenSourceLicenses : Screen {
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalRootNavigator.current
        val libraries by rememberLibraries {
            Res.readBytes("files/aboutlibraries.json").decodeToString()
        }

        val uriHandler = LocalUriHandler.current

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
                libraries = libraries,
                modifier = Modifier.padding(contentPadding),
                onLibraryClick = { library ->
                    library.scm?.url?.let {
                        uriHandler.openUri(it)
                    }
                },
            )
        }
    }
}
