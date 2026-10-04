package dev.koga.deeplinklauncher.cli.ios

import dev.koga.deeplinklauncher.cli.fixture
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SimctlOutputTest {

    @Test
    fun readsSimulatorsWithTheirRuntime() {
        val simulators = SimctlOutput.simulators(fixture("ios/simctl-list-devices.json"))

        assertEquals(
            Simulator("564FC45E-8A89-41A7-B88F-5E755E1A6EB6", "iPhone 16 Pro", booted = true, osVersion = "18.2"),
            simulators.single { it.booted },
        )
        assertEquals(3, simulators.size)
    }

    @Test
    fun readsInstalledAppsWithTheirDisplayName() {
        val calendar = SimctlOutput.apps(fixture("ios/listapps.json")).single { it.bundleId == "com.apple.mobilecal" }

        assertEquals("Calendar", calendar.name)
        assertTrue(calendar.path.endsWith("MobileCal.app"))
    }

    @Test
    fun readsTheUrlSchemesAnAppDeclares() {
        assertEquals(
            listOf("calshow", "x-apple-calevent", "calinvitelist", "calinvite", "webcal"),
            SimctlOutput.urlSchemes(fixture("ios/url-types-mobilecal.json")),
        )
    }

    @Test
    fun readsRunningAppsFromLaunchctl() {
        val running = SimctlOutput.runningBundleIds(fixture("ios/launchctl-list.txt"))

        assertTrue("com.apple.mobilecal" in running)
        assertTrue("com.apple.Spotlight" in running)
    }
}
