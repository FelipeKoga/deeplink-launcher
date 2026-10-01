package dev.koga.deeplinklauncher.deeplink.uicomponent

import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.UIKitInteropInteractionMode
import androidx.compose.ui.viewinterop.UIKitInteropProperties
import androidx.compose.ui.viewinterop.UIKitView
import dev.koga.deeplinklauncher.designsystem.theme.DeepLinkTheme
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
import platform.CoreGraphics.CGRectZero
import platform.Foundation.NSData
import platform.Foundation.NSItemProvider
import platform.Foundation.NSString
import platform.Foundation.NSUTF8StringEncoding
import platform.Foundation.create
import platform.UIKit.NSLayoutConstraint
import platform.UIKit.UIButtonConfigurationCornerStyleCapsule
import platform.UIKit.UIColor
import platform.UIKit.UIPasteConfiguration
import platform.UIKit.UIPasteConfigurationSupportingProtocol
import platform.UIKit.UIPasteControl
import platform.UIKit.UIPasteControlConfiguration
import platform.UIKit.UIPasteControlDisplayMode
import platform.UIKit.UIView
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal actual fun ClipboardPasteButton(
    onPaste: (String) -> Unit,
    modifier: Modifier,
) {
    val colors = DeepLinkTheme.colors
    val background = colors.button.primaryBackground
    val foreground = colors.button.primaryContent
    val currentOnPaste by rememberUpdatedState(onPaste)

    key(background, foreground) {
        UIKitView(
            factory = {
                PasteButtonView(
                    background = background.toUIColor(),
                    foreground = foreground.toUIColor(),
                    onPaste = { currentOnPaste(it) },
                )
            },
            modifier = modifier.size(width = 104.dp, height = 40.dp),
            properties = UIKitInteropProperties(
                interactionMode = UIKitInteropInteractionMode.NonCooperative,
                isNativeAccessibilityEnabled = true,
                placedAsOverlay = true,
            ),
        )
    }
}

private const val PLAIN_TEXT_TYPE = "public.plain-text"
private const val UTF8_PLAIN_TEXT_TYPE = "public.utf8-plain-text"

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
private class PasteButtonView(
    background: UIColor,
    foreground: UIColor,
    private val onPaste: (String) -> Unit,
) : UIView(frame = CGRectZero.readValue()), UIPasteConfigurationSupportingProtocol {

    private var acceptedTypes: UIPasteConfiguration? =
        UIPasteConfiguration(acceptableTypeIdentifiers = listOf(PLAIN_TEXT_TYPE))

    init {
        val configuration = UIPasteControlConfiguration().apply {
            displayMode = UIPasteControlDisplayMode.UIPasteControlDisplayModeIconAndLabel
            cornerStyle = UIButtonConfigurationCornerStyleCapsule
            baseBackgroundColor = background
            baseForegroundColor = foreground
        }

        val control = UIPasteControl(configuration = configuration)
        control.target = this
        control.translatesAutoresizingMaskIntoConstraints = false
        addSubview(control)

        NSLayoutConstraint.activateConstraints(
            listOf(
                control.leadingAnchor.constraintEqualToAnchor(leadingAnchor),
                control.trailingAnchor.constraintEqualToAnchor(trailingAnchor),
                control.topAnchor.constraintEqualToAnchor(topAnchor),
                control.bottomAnchor.constraintEqualToAnchor(bottomAnchor),
            ),
        )
    }

    override fun pasteConfiguration(): UIPasteConfiguration? = acceptedTypes

    override fun setPasteConfiguration(pasteConfiguration: UIPasteConfiguration?) {
        acceptedTypes = pasteConfiguration
    }

    override fun pasteItemProviders(itemProviders: List<*>) {
        val provider = itemProviders.firstOrNull() as? NSItemProvider ?: return

        provider.loadDataRepresentationForTypeIdentifier(UTF8_PLAIN_TEXT_TYPE) { data: NSData?, _ ->
            val text = data?.let { NSString.create(data = it, encoding = NSUTF8StringEncoding)?.toString() }
                ?: return@loadDataRepresentationForTypeIdentifier

            dispatch_async(dispatch_get_main_queue()) { onPaste(text) }
        }
    }
}

private fun Color.toUIColor(): UIColor = UIColor.colorWithRed(
    red = red.toDouble(),
    green = green.toDouble(),
    blue = blue.toDouble(),
    alpha = alpha.toDouble(),
)
