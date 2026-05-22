package dev.koga.deeplinklauncher.deeplink.impl.usecase

import android.content.Context
import android.content.Intent
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.net.toUri
import androidx.core.os.persistableBundleOf
import dev.koga.deeplinklauncher.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.api.usecase.AddDeepLinkToShortcuts

internal class AddDeepLinkToShortcutsImpl(
    private val context: Context,
) : AddDeepLinkToShortcuts {
    override fun invoke(deepLink: DeepLink): AddDeepLinkToShortcuts.Result {
        val shortLabel = (deepLink.name?.takeIf { it.isNotBlank() } ?: deepLink.link)
            .take(MAX_SHORT_LABEL_LENGTH)
        val longLabel = (deepLink.description?.takeIf { it.isNotBlank() } ?: shortLabel)
            .take(MAX_LONG_LABEL_LENGTH)

        val intent = Intent(Intent.ACTION_VIEW, deepLink.link.trim().toUri())
        val shortcutIcon = context.resolveShortcutIconCompat(intent)

        val shortcut = ShortcutInfoCompat.Builder(context, deepLink.id)
            .setShortLabel(shortLabel)
            .setLongLabel(longLabel)
            .apply {
                if (shortcutIcon != null) {
                    setIcon(shortcutIcon)
                }
            }
            .setIntent(intent)
            .setExtras(persistableBundleOf("deeplink" to deepLink.link))
            .build()

        val added = ShortcutManagerCompat.pushDynamicShortcut(context, shortcut)

        val a = ShortcutManagerCompat.getShortcuts(context, ShortcutManagerCompat.FLAG_MATCH_DYNAMIC)

        return if (added) {
            AddDeepLinkToShortcuts.Result.Added
        } else {
            AddDeepLinkToShortcuts.Result.NotSupported
        }
    }

    companion object {
        private const val MAX_SHORT_LABEL_LENGTH = 25
        private const val MAX_LONG_LABEL_LENGTH = 256
    }
}
