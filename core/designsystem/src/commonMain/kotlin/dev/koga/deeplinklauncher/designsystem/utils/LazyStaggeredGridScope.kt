package dev.koga.deeplinklauncher.designsystem.utils

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridItemScope
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridScope
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp


fun LazyStaggeredGridScope.fullLineItem(
    content: @Composable LazyStaggeredGridItemScope.() -> Unit,
) {
    item(span = StaggeredGridItemSpan.FullLine) {
        content()
    }
}

fun LazyStaggeredGridScope.spacer(height: Dp) {
    item(span = StaggeredGridItemSpan.FullLine) {
        Spacer(modifier = Modifier.height(height))
    }
}
