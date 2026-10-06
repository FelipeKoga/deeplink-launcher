package dev.koga.deeplinklauncher.deeplink.impl.domain.manager

import android.content.Context
import androidx.core.content.pm.ShortcutManagerCompat
import dev.koga.deeplinklauncher.deeplink.api.domain.manager.DeepLinkShortcutManager
import dev.koga.deeplinklauncher.deeplink.api.domain.model.DeepLink
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35], shadows = [StrictShadowShortcutManager::class])
class DeepLinkShortcutManagerImplTest {

    private lateinit var context: Context
    private lateinit var manager: DeepLinkShortcutManagerImpl

    @Before
    fun setUp() {
        context = RuntimeEnvironment.getApplication()
        manager = DeepLinkShortcutManagerImpl(context)
        StrictShadowShortcutManager.calls.clear()
    }

    @Test
    fun addsAValidDeepLink() = runBlocking {
        assertEquals(DeepLinkShortcutManager.AddResult.Added, manager.add(deepLink(id = "id", link = "myapp://home")))
        assertTrue(manager.isAdded("id"))
    }

    @Test
    fun failsInsteadOfCrashingForAnEmptyId() = runBlocking {
        assertEquals(DeepLinkShortcutManager.AddResult.Failed, manager.add(deepLink(id = "", link = "myapp://home")))
    }

    @Test
    fun failsInsteadOfCrashingForAnEmptyLinkWithoutName() = runBlocking {
        assertEquals(DeepLinkShortcutManager.AddResult.Failed, manager.add(deepLink(id = "id", link = "")))
        assertFalse(manager.isAdded("id"))
    }

    @Test
    fun disablesOnlyTheNonEmptyIds() = runBlocking {
        manager.disable(listOf("", "id"))

        assertEquals(listOf("disable" to listOf("id")), StrictShadowShortcutManager.calls)
    }

    @Test
    fun enablesOnlyTheNonEmptyIds() = runBlocking {
        manager.enable(listOf("", "id"))

        assertEquals(listOf("enable" to listOf("id")), StrictShadowShortcutManager.calls)
    }

    @Test
    fun skipsAnEmptyIdWhenRemovingOrUpdating() = runBlocking {
        manager.remove("")
        manager.update(deepLink(id = "", link = "myapp://home"))

        assertEquals(emptyList<Pair<String, List<String>>>(), StrictShadowShortcutManager.calls)
    }

    @Test
    fun doesNotThrowWhenAnUpdatedShortcutCannotBeBuilt() = runBlocking {
        manager.add(deepLink(id = "id", link = "myapp://home"))

        manager.update(deepLink(id = "id", link = ""))

        assertTrue(manager.isAdded("id"))
    }

    @Test
    fun updatesAnExistingShortcutWithTheNewLabel() = runBlocking {
        manager.add(deepLink(id = "id", link = "myapp://home"))
        StrictShadowShortcutManager.calls.clear()

        manager.update(deepLink(id = "id", link = "myapp://home").copy(name = "Home"))

        assertEquals(listOf("update" to listOf("id")), StrictShadowShortcutManager.calls)
        assertEquals(
            "Home",
            ShortcutManagerCompat.getShortcuts(context, ShortcutManagerCompat.FLAG_MATCH_DYNAMIC).single().shortLabel,
        )
    }

    @Test
    fun skipsTheUpdateWhenTheDeepLinkHasNoShortcut() = runBlocking {
        manager.update(deepLink(id = "never-added", link = "myapp://home"))

        assertEquals(emptyList<Pair<String, List<String>>>(), StrictShadowShortcutManager.calls)
    }

    private fun deepLink(id: String, link: String) = DeepLink(
        id = id,
        link = link,
        name = null,
        description = null,
        isFavorite = false,
    )
}
