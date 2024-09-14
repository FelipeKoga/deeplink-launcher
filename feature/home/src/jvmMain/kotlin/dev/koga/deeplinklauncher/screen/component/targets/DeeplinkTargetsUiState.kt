package dev.koga.deeplinklauncher.screen.component.targets

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Smartphone
import androidx.compose.ui.graphics.vector.ImageVector
import dev.koga.deeplinklauncher.model.Target

data class DeeplinkTargetsUiState(
    val selected: Option = Target.Browser.toUiState(),
    val targets: List<Option> = listOf(selected),
) {

    data class Option(
        val target: Target,
        val selected: Boolean,
        val name: String,
        val icon: ImageVector,
    )
}

fun List<Target>.toUiState(
    selected: Target,
) = DeeplinkTargetsUiState(
    targets = map {
        it.toUiState(selected = selected == it)
    },
    selected = selected.toUiState(),
)

fun Target.toUiState(
    selected: Boolean = true,
) = when (this) {
    Target.Browser -> {
        DeeplinkTargetsUiState.Option(
            target = this,
            selected = selected,
            name = "Default Browser",
            icon = Icons.Rounded.Public,
        )
    }

    is Target.Device -> DeeplinkTargetsUiState.Option(
        target = this,
        selected = selected,
        name = name,
        icon = when (platform) {
            Target.Device.Platform.ANDROID -> Icons.Rounded.Smartphone
            Target.Device.Platform.IOS -> Icons.Rounded.Smartphone
        }
    )
}
