package dev.koga.deeplinklauncher.deeplink.impl.domain.manager

import android.content.Context
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.os.persistableBundleOf
import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import dev.koga.deeplinklauncher.deeplink.impl.platform.android.createDeepLinkViewIntent
import dev.koga.deeplinklauncher.deeplink.impl.platform.android.resolveShortcutIconCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class DeepLinkShortcutManagerImpl(
    private val context: Context,
) : DeepLinkShortcutManager {

    override suspend fun isAdded(deepLinkId: String): Boolean = withContext(Dispatchers.IO) {
        ShortcutManagerCompat.getShortcuts(context, ShortcutManagerCompat.FLAG_MATCH_DYNAMIC)
            .any { it.id == deepLinkId }
    }

    override suspend fun add(
        deepLink: DeepLink,
    ): DeepLinkShortcutManager.AddResult = withContext(Dispatchers.IO) {
        val shortLabel = (deepLink.name?.takeIf { it.isNotBlank() } ?: deepLink.link)
            .take(MAX_SHORT_LABEL_LENGTH)
        val longLabel = (deepLink.description?.takeIf { it.isNotBlank() } ?: shortLabel)
            .take(MAX_LONG_LABEL_LENGTH)

        val intent = context.createDeepLinkViewIntent(deepLink.link, deepLink.targetPackage)
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

        if (added) {
            DeepLinkShortcutManager.AddResult.Added
        } else {
            DeepLinkShortcutManager.AddResult.NotSupported
        }
    }

    override suspend fun remove(deepLinkId: String) {
        withContext(Dispatchers.IO) {
            ShortcutManagerCompat.removeDynamicShortcuts(context, listOf(deepLinkId))
        }
    }

    override suspend fun disable(deepLinkIds: List<String>) {
        if (deepLinkIds.isEmpty()) return
        withContext(Dispatchers.IO) {
            ShortcutManagerCompat.disableShortcuts(context, deepLinkIds, DELETED_MESSAGE)
        }
    }

    companion object {
        private const val MAX_SHORT_LABEL_LENGTH = 25
        private const val MAX_LONG_LABEL_LENGTH = 256
        private const val DELETED_MESSAGE = "This deeplink was deleted"
    }
}
