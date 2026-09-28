package dev.koga.deeplinklauncher.domain.deeplink.impl.manager
import android.content.Context
import android.content.pm.ShortcutManager
import androidx.core.content.pm.ShortcutInfoCompat
import androidx.core.content.pm.ShortcutManagerCompat
import androidx.core.os.persistableBundleOf
import dev.koga.deeplinklauncher.domain.deeplink.api.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.domain.deeplink.api.model.DeepLink
import dev.koga.deeplinklauncher.domain.deeplink.impl.platform.android.createDeepLinkViewIntent
import dev.koga.deeplinklauncher.domain.deeplink.impl.platform.android.resolveShortcutIconCompat
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
        val added = ShortcutManagerCompat.pushDynamicShortcut(context, buildShortcut(deepLink))

        if (added) {
            DeepLinkShortcutManager.AddResult.Added
        } else {
            DeepLinkShortcutManager.AddResult.NotSupported
        }
    }

    override suspend fun update(deepLink: DeepLink) {
        withContext(Dispatchers.IO) {
            ShortcutManagerCompat.updateShortcuts(context, listOf(buildShortcut(deepLink)))
        }
    }

    override suspend fun remove(deepLinkId: String) {
        withContext(Dispatchers.IO) {
            ShortcutManagerCompat.removeDynamicShortcuts(context, listOf(deepLinkId))
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
        if (deepLinkIds.isEmpty()) return
        withContext(Dispatchers.IO) {
            context.getSystemService(ShortcutManager::class.java).enableShortcuts(deepLinkIds)
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
