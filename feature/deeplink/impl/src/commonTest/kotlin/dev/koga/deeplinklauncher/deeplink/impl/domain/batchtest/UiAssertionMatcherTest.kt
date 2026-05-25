package dev.koga.deeplinklauncher.deeplink.impl.domain.batchtest

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UiAssertionMatcherTest {

    @Test
    fun `extractVisibleTextsFromUiDump returns unique text and content-desc values`() {
        val dump = """
            <node text="Campaign 123" content-desc="Details"/>
            <node text="Campaign 123" content-desc="Promo"/>
        """.trimIndent()

        assertEquals(
            listOf("Campaign 123", "Details", "Promo"),
            extractVisibleTextsFromUiDump(dump),
        )
    }

    @Test
    fun `matchAssertionTexts finds all expected strings case-insensitively`() {
        val dump = "<node text=\"Campaign 123\" content-desc=\"promo details\"/>"
        val (matched, missing) = matchAssertionTexts(
            uiDump = dump,
            expectedTexts = listOf("campaign 123", "Promo"),
        )

        assertEquals(listOf("campaign 123", "Promo"), matched)
        assertTrue(missing.isEmpty())
    }

    @Test
    fun `matchAssertionTexts reports missing strings`() {
        val dump = "<node text=\"Home\"/>"
        val (_, missing) = matchAssertionTexts(
            uiDump = dump,
            expectedTexts = listOf("Campaign 123"),
        )

        assertEquals(listOf("Campaign 123"), missing)
    }

    @Test
    fun `matchesActivityPattern supports regex`() {
        assertTrue(matchesActivityPattern("com.app.CampaignActivity", ".*Campaign.*"))
        assertFalse(matchesActivityPattern("com.app.HomeActivity", ".*Campaign.*"))
    }
}
