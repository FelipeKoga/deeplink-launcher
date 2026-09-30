package dev.koga.deeplinklauncher.deeplink.impl.domain.manager

import android.content.pm.ShortcutManager
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.shadows.ShadowShortcutManager

@Implements(ShortcutManager::class)
class StrictShadowShortcutManager : ShadowShortcutManager() {

    @Implementation
    override fun enableShortcuts(shortcutIds: List<String>) {
        record("enable", shortcutIds)
        super.enableShortcuts(shortcutIds)
    }

    @Implementation
    override fun disableShortcuts(shortcutIds: List<String>, disabledMessage: CharSequence?) {
        record("disable", shortcutIds)
        super.disableShortcuts(shortcutIds, disabledMessage)
    }

    @Implementation
    override fun removeDynamicShortcuts(shortcutIds: List<String>) {
        record("remove", shortcutIds)
        super.removeDynamicShortcuts(shortcutIds)
    }

    private fun record(call: String, shortcutIds: List<String>) {
        require(shortcutIds.none { it.isEmpty() }) { "shortcut id cannot be empty" }
        calls += call to shortcutIds.toList()
    }

    companion object {
        val calls = mutableListOf<Pair<String, List<String>>>()
    }
}
