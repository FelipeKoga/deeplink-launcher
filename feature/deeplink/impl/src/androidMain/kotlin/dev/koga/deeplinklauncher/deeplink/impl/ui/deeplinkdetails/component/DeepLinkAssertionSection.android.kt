package dev.koga.deeplinklauncher.deeplink.impl.ui.deeplinkdetails.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLinkAssertion

@Composable
internal actual fun DeepLinkAssertionSection(
    assertion: DeepLinkAssertion?,
    captureMessage: String?,
    onAssertionChanged: (DeepLinkAssertion?) -> Unit,
    onCaptureCurrentState: () -> Unit,
    modifier: Modifier,
) = Unit
