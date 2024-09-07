package dev.koga.deeplinklauncher

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import cafe.adriel.voyager.core.screen.Screen
import com.mikepenz.aboutlibraries.ui.compose.m3.LibrariesContainer
import com.mikepenz.aboutlibraries.ui.compose.m3.rememberLibraries
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
                libraries = libraries,
                modifier = Modifier.padding(contentPadding),
            )
        }
    }
}
