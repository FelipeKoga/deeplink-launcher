package dev.koga.deeplinklauncher.screen.component.targets

import androidx.compose.ui.graphics.vector.ImageVector
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.brands.Android
import compose.icons.fontawesomeicons.brands.Apple
import compose.icons.fontawesomeicons.solid.Desktop
import dev.koga.deeplinklauncher.devicebridge.DeviceBridge
import dev.koga.deeplinklauncher.model.DeeplinkTarget

data class DeeplinkTargetsUiState(
    val selected: Option = DeeplinkTarget.Desktop.toUiState(),
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
    DeeplinkTarget.Desktop -> {
        DeeplinkTargetsUiState.Option(
            deeplinkTarget = this,
            selected = selected,
            name = "Desktop",
            icon = FontAwesomeIcons.Solid.Desktop,
        )
    }

    is DeeplinkTarget.Device -> DeeplinkTargetsUiState.Option(
        deeplinkTarget = this,
        selected = selected,
        name = name,
        icon = when (platform) {
            DeviceBridge.Platform.ANDROID -> FontAwesomeIcons.Brands.Android
            DeviceBridge.Platform.IOS -> FontAwesomeIcons.Brands.Apple
        },
    )
}
