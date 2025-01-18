@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalResourceApi::class)

package dev.koga.deeplinklauncher.settings.ui.sheets

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.rememberLibraries
import dev.koga.deeplinklauncher.designsystem.DLLModalBottomSheet
import dev.koga.deeplinklauncher.designsystem.DLLTopBar
import dev.koga.deeplinklauncher.designsystem.DLLTopBarDefaults
import dev.koga.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun OpenSourceLicensesSheet(modifier: Modifier = Modifier, onDismiss: () -> Unit) {
    val libraries by rememberLibraries {
        Res.readBytes("files/aboutlibraries.json").decodeToString()
    }

    DLLModalBottomSheet(modifier = modifier, onDismiss = onDismiss) {
        Scaffold(
            topBar = {
                DLLTopBar(
                    title = {
                        DLLTopBarDefaults.title("Open Source Licenses")
                    },
                )
            },
        ) { contentPadding ->
            LibrariesContainer(
                libraries = libraries,
                modifier = Modifier.padding(contentPadding),
            )
        }
    }
}
