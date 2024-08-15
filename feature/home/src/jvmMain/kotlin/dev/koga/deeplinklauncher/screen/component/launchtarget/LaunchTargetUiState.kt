package dev.koga.deeplinklauncher.screen.component.launchtarget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Devices
import androidx.compose.material.icons.rounded.Public
import androidx.compose.ui.graphics.vector.ImageVector
import dev.koga.deeplinklauncher.model.Target

data class LaunchTargetUiState(
    val selected: Target = Target.Browser(selected = true),
    val targets: List<Target> = listOf(selected)
) {

    sealed class Target {

        abstract val name: String
        abstract val icon: ImageVector
        abstract val selected: Boolean

        data class Browser(
            override val selected: Boolean
        ) : Target() {
            override val name = "browser"
            override val icon = Icons.Rounded.Public
        }

        data class Device(
            override val name: String,
            override val selected: Boolean,
            val type: String
        ) : Target() {
            override val icon = Icons.Rounded.Devices
        }
    }
}


fun List<Target>.toUiState(
    selected: Target
) = map {
    it.toUiState(
        selected = selected == it
    )
}

fun Target.toUiState(
    selected: Boolean
) = when (this) {
    Target.Browser -> LaunchTargetUiState.Target.Browser(selected)
    is Target.Device -> LaunchTargetUiState.Target.Device(name, selected, type)
}