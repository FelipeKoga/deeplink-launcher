package dev.koga.deeplinklauncher.home.component.targets

import androidx.compose.runtime.Composable
import compose.icons.TablerIcons
import compose.icons.tablericons.BrandAndroid
import compose.icons.tablericons.BrandApple
import compose.icons.tablericons.DeviceDesktop
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLinkTarget
import dev.koga.deeplinklauncher.devicebridge.api.DeviceBridge

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
        DeepLinkTargetsUiState.Platform.ANDROID -> TablerIcons.BrandAndroid
        DeepLinkTargetsUiState.Platform.IOS -> TablerIcons.BrandApple
        DeepLinkTargetsUiState.Platform.DESKTOP -> TablerIcons.DeviceDesktop
    }
