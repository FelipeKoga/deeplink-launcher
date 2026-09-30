package dev.koga.deeplinklauncher.deeplink.impl.domain.manager

import android.content.Context
import android.content.pm.ShortcutManager
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
        if (deepLink.id.isEmpty()) return@withContext DeepLinkShortcutManager.AddResult.Failed

        runCatching { ShortcutManagerCompat.pushDynamicShortcut(context, buildShortcut(deepLink)) }.fold(
            onSuccess = { added ->
                if (added) {
                    DeepLinkShortcutManager.AddResult.Added
                } else {
                    DeepLinkShortcutManager.AddResult.NotSupported
                }
            },
            onFailure = { DeepLinkShortcutManager.AddResult.Failed },
        )
    }

    override suspend fun update(deepLink: DeepLink) {
        if (deepLink.id.isEmpty()) return
        withContext(Dispatchers.IO) {
            runCatching {
                ShortcutManagerCompat.updateShortcuts(context, listOf(buildShortcut(deepLink)))
            }
        }
    }

    override suspend fun remove(deepLinkId: String) {
        if (deepLinkId.isEmpty()) return
        withContext(Dispatchers.IO) {
            runCatching { ShortcutManagerCompat.removeDynamicShortcuts(context, listOf(deepLinkId)) }
        }
    }

    private fun buildShortcut(deepLink: DeepLink): ShortcutInfoCompat {
        val shortLabel = (deepLink.name?.takeIf { it.isNotBlank() } ?: deepLink.link)
            .take(MAX_SHORT_LABEL_LENGTH)
        val longLabel = (deepLink.description?.takeIf { it.isNotBlank() } ?: shortLabel)
            .take(MAX_LONG_LABEL_LENGTH)

        val intent = context.createDeepLinkViewIntent(deepLink.link, deepLink.targetPackage)
        val shortcutIcon = context.resolveShortcutIconCompat(intent)

        return ShortcutInfoCompat.Builder(context, deepLink.id)
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
    }

    override suspend fun enable(deepLinkIds: List<String>) {
        val ids = deepLinkIds.filter { it.isNotEmpty() }
        if (ids.isEmpty()) return
        withContext(Dispatchers.IO) {
            runCatching { context.getSystemService(ShortcutManager::class.java).enableShortcuts(ids) }
        }
    }

    override suspend fun disable(deepLinkIds: List<String>) {
        val ids = deepLinkIds.filter { it.isNotEmpty() }
        if (ids.isEmpty()) return
        withContext(Dispatchers.IO) {
            runCatching { ShortcutManagerCompat.disableShortcuts(context, ids, DELETED_MESSAGE) }
        }
    }

    companion object {
        private const val MAX_SHORT_LABEL_LENGTH = 25
        private const val MAX_LONG_LABEL_LENGTH = 256
        private const val DELETED_MESSAGE = "This deeplink was deleted"
    }
}
