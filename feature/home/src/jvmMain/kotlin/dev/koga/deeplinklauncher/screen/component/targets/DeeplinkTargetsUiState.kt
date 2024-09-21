package dev.koga.deeplinklauncher.screen.component.targets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.DesktopMac
import androidx.compose.runtime.Composable
import dev.koga.deeplinklauncher.devicebridge.DeviceBridge
import dev.koga.deeplinklauncher.model.DeeplinkTarget
import dev.koga.deeplinklauncher.platform.Platform
import dev.koga.deeplinklauncher.platform.platform
import dev.koga.resources.Res
import dev.koga.resources.ic_apple_24dp
import org.jetbrains.compose.resources.vectorResource

data class DeeplinkTargetsUiState(
    val selected: Option = DeeplinkTarget.Desktop.toUiState(),
    val targets: List<Option> = listOf(selected),
) {

    data class Option(
        val deeplinkTarget: DeeplinkTarget,
        val selected: Boolean,
        val name: String,
        val platform: Platform,
    )
}

fun List<DeeplinkTarget>.toUiState(
    selected: DeeplinkTarget,
) = DeeplinkTargetsUiState(
    targets = map {
        it.toUiState(selected = selected == it)
    },
    selected = selected.toUiState(),
)

fun DeeplinkTarget.toUiState(
    selected: Boolean = true,
) = when (this) {
    DeeplinkTarget.Desktop -> {
        DeeplinkTargetsUiState.Option(
            deeplinkTarget = this,
            selected = selected,
            name = "Desktop",
            platform = platform,
        )
    }

    is DeeplinkTarget.Device -> DeeplinkTargetsUiState.Option(
        deeplinkTarget = this,
        selected = selected,
        name = name,
        platform = when (platform) {
            DeviceBridge.Platform.ANDROID -> Platform.ANDROID
            DeviceBridge.Platform.IOS -> Platform.IOS
        },
    )
}

val DeeplinkTargetsUiState.Option.icon
    @Composable get() = when (platform) {
        Platform.ANDROID -> Icons.Rounded.Android
        Platform.IOS -> vectorResource(Res.drawable.ic_apple_24dp)
        Platform.JVM -> Icons.Rounded.DesktopMac
    }
