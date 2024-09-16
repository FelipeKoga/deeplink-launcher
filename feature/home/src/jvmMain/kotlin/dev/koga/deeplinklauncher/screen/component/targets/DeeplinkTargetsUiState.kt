package dev.koga.deeplinklauncher.screen.component.targets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Smartphone
import androidx.compose.ui.graphics.vector.ImageVector
import dev.koga.deeplinklauncher.devicebridge.DeviceBridge
import dev.koga.deeplinklauncher.model.DeeplinkTarget

data class DeeplinkTargetsUiState(
    val selected: Option = DeeplinkTarget.Browser.toUiState(),
    val targets: List<Option> = listOf(selected),
) {

    data class Option(
        val deeplinkTarget: DeeplinkTarget,
        val selected: Boolean,
        val name: String,
        val icon: ImageVector,
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
    DeeplinkTarget.Browser -> {
        DeeplinkTargetsUiState.Option(
            deeplinkTarget = this,
            selected = selected,
            name = "Default Browser",
            icon = Icons.Rounded.Public,
        )
    }

    is DeeplinkTarget.Device -> DeeplinkTargetsUiState.Option(
        deeplinkTarget = this,
        selected = selected,
        name = name,
        icon = when (platform) {
            DeviceBridge.Platform.ANDROID -> Icons.Rounded.Smartphone
            DeviceBridge.Platform.IOS -> Icons.Rounded.Smartphone
        },
    )
}
