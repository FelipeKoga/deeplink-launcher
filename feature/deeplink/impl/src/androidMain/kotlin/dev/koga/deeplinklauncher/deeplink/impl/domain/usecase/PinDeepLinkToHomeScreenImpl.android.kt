package dev.koga.deeplinklauncher.deeplink.impl.domain.usecase

import android.content.Context
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.domain.usecase.PinDeepLinkToHomeScreen
import dev.koga.deeplinklauncher.deeplink.impl.platform.android.createDeepLinkViewIntent
import dev.koga.deeplinklauncher.deeplink.impl.platform.android.resolveShortcutIcon
internal class PinDeepLinkToHomeScreenImpl(
    private val context: Context,
) : PinDeepLinkToHomeScreen {
    override fun invoke(deepLink: DeepLink): PinDeepLinkToHomeScreen.Result {
        val shortcutManager = context.getSystemService(ShortcutManager::class.java)

        if (!shortcutManager.isRequestPinShortcutSupported) {
            return PinDeepLinkToHomeScreen.Result.NotSupported
        }

        val label = (deepLink.name?.takeIf { it.isNotBlank() } ?: deepLink.link)
            .take(MAX_SHORT_LABEL_LENGTH)

        val intent = context.createDeepLinkViewIntent(deepLink.link, deepLink.targetPackage)
        val shortcutIcon = context.resolveShortcutIcon(intent)

        val shortcut = ShortcutInfo.Builder(context, deepLink.id)
            .setShortLabel(label)
            .apply {
                if (shortcutIcon != null) {
                    setIcon(shortcutIcon)
                }
            }
            .setIntent(intent)
            .build()

        shortcutManager.requestPinShortcut(shortcut, null)

        return PinDeepLinkToHomeScreen.Result.Requested
    }

    companion object {
        private const val MAX_SHORT_LABEL_LENGTH = 25
    }
}
