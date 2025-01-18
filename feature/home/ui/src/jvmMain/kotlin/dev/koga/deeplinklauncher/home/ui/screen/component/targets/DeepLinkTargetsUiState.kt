package dev.koga.deeplinklauncher.home.ui.screen.component.targets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Android
import androidx.compose.material.icons.rounded.DesktopMac
import androidx.compose.runtime.Composable
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkTarget
import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge
import dev.koga.resources.Res
import dev.koga.resources.ic_apple_24dp
import org.jetbrains.compose.resources.vectorResource

data class DeepLinkTargetsUiState(
    val selected: Option = DeepLinkTarget.Desktop.toUiState(),
    val targets: List<Option> = listOf(selected),
) {

    data class Option(
        val deeplinkTarget: DeepLinkTarget,
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

fun List<DeepLinkTarget>.toUiState(
    selected: DeepLinkTarget,
) = DeepLinkTargetsUiState(
    targets = map {
        it.toUiState(selected = selected == it)
    },
    selected = selected.toUiState(),
)

fun DeepLinkTarget.toUiState(
    selected: Boolean = true,
) = when (this) {
    DeepLinkTarget.Desktop -> {
        DeepLinkTargetsUiState.Option(
            deeplinkTarget = this,
            selected = selected,
            name = "Desktop",
            platform = DeepLinkTargetsUiState.Platform.DESKTOP,
        )
    }

    is DeepLinkTarget.Device -> DeepLinkTargetsUiState.Option(
        deeplinkTarget = this,
        selected = selected,
        name = name,
        platform = when (platform) {
            DeviceBridge.Platform.ANDROID -> DeepLinkTargetsUiState.Platform.ANDROID
            DeviceBridge.Platform.IOS -> DeepLinkTargetsUiState.Platform.IOS
        },
    )
}

val DeepLinkTargetsUiState.Option.icon
    @Composable get() = when (platform) {
        DeepLinkTargetsUiState.Platform.ANDROID -> Icons.Rounded.Android
        DeepLinkTargetsUiState.Platform.IOS -> vectorResource(Res.drawable.ic_apple_24dp)
        DeepLinkTargetsUiState.Platform.DESKTOP -> Icons.Rounded.DesktopMac
    }
