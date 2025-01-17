package dev.koga.deeplinklauncher.home.ui.screen.component.targets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.DesktopMac
import androidx.compose.runtime.Composable
import dev.koga.deeplinklauncher.devicebridge.DeviceBridge
import dev.koga.deeplinklauncher.devicebridge.model.DeeplinkTarget
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

    enum class Platform {
        ANDROID,
        IOS,
        DESKTOP,
    }
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
            platform = DeeplinkTargetsUiState.Platform.DESKTOP,
        )
    }

    is DeeplinkTarget.Device -> DeeplinkTargetsUiState.Option(
        deeplinkTarget = this,
        selected = selected,
        name = name,
        platform = when (platform) {
            DeviceBridge.Platform.ANDROID -> DeeplinkTargetsUiState.Platform.ANDROID
            DeviceBridge.Platform.IOS -> DeeplinkTargetsUiState.Platform.IOS
        },
    )
}

val DeeplinkTargetsUiState.Option.icon
    @Composable get() = when (platform) {
        DeeplinkTargetsUiState.Platform.ANDROID -> Icons.Rounded.Android
        DeeplinkTargetsUiState.Platform.IOS -> vectorResource(Res.drawable.ic_apple_24dp)
        DeeplinkTargetsUiState.Platform.DESKTOP -> Icons.Rounded.DesktopMac
    }
