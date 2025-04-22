package dev.koga.deeplinklauncher.shared.anim

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut

private const val ANIM_DURATION_LONG = 500
private const val ANIM_DURATION_SHORT = 300

fun scaleInEnterTransition() = scaleIn(
    initialScale = .9f,
    animationSpec = tween(ANIM_DURATION_LONG),
) + fadeIn(
    animationSpec = tween(ANIM_DURATION_SHORT),
)

fun scaleOutExitTransition() = scaleOut(
    targetScale = 1.1f,
    animationSpec = tween(ANIM_DURATION_SHORT),
) + fadeOut(
    animationSpec = tween(ANIM_DURATION_SHORT),
)

fun scaleInPopEnterTransition() = scaleIn(
    initialScale = 1.1f,
    animationSpec = tween(ANIM_DURATION_LONG),
) + fadeIn(
    animationSpec = tween(ANIM_DURATION_SHORT),
)

fun scaleOutPopExitTransition() = scaleOut(
    targetScale = .9f,
    animationSpec = tween(ANIM_DURATION_SHORT),
) + fadeOut(
    animationSpec = tween(ANIM_DURATION_SHORT),
)