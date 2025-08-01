package dev.koga.deeplinklauncher.settings.impl.opensource

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import dev.koga.deeplinklauncher.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.designsystem.DLLTopBarDefaults
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OpenSourceLicensesScreen(
    onBack: () -> Unit,
) {
    val libraries by rememberLibraries()

    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            DLLTopBar(
                title = {
                    DLLTopBarDefaults.title("Open Source Licenses")
                },
                navigationIcon = {
                    DLLTopBarDefaults.navigationIcon(onClicked = onBack)
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
