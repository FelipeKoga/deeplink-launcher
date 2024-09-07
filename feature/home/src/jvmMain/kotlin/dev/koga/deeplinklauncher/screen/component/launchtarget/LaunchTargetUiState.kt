package dev.koga.deeplinklauncher.screen.component.launchtarget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Smartphone
import androidx.compose.ui.graphics.vector.ImageVector
import dev.koga.deeplinklauncher.model.Target

data class LaunchTargetUiState(
    val selected: Option = Target.Browser.toUiState(),
    val targets: List<Option> = listOf(selected)
) {

    data class Option(
        val target: Target,
        val selected: Boolean,
        val name: String,
        val icon: ImageVector,
    )
}

fun List<Target>.toUiState(
    selected: Target
) = LaunchTargetUiState(
    targets = map {
        it.toUiState(
            selected = selected == it
        )
    },
    selected = selected.toUiState()
)

fun Target.toUiState(
    selected: Boolean = true
) = when (this) {
    Target.Browser -> {
        LaunchTargetUiState.Option(
            target = this,
            selected = selected,
            name = "Default Browser",
            icon = Icons.Rounded.Public
        )
    }

    is Target.Device.Emulator -> {
        LaunchTargetUiState.Option(
            target = this,
            selected = selected,
            name = name,
            icon = Icons.Rounded.Devices
        )
    }

    is Target.Device.Physical -> {
        LaunchTargetUiState.Option(
            target = this,
            selected = selected,
            name = name,
            icon = Icons.Rounded.Smartphone
        )
    }
}
