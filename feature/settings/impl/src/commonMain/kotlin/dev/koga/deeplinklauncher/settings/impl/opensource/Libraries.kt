package dev.koga.deeplinklauncher.settings.impl.opensource

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.mikepenz.aboutlibraries.Libs

@Composable
expect fun rememberLibraries(): State<Libs?>
