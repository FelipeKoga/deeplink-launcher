package dev.koga.deeplinklauncher.cli.suite

import dev.koga.deeplinklauncher.cli.device.Device
import dev.koga.deeplinklauncher.cli.device.Platform
import dev.koga.deeplinklauncher.cli.output.Handler
import dev.koga.deeplinklauncher.cli.output.OpenReport
import dev.koga.deeplinklauncher.cli.output.OpenStatus
import dev.koga.deeplinklauncher.cli.output.TestStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class VerdictsTest {

    private val pixel = Device("emulator-5554", "Pixel", Platform.ANDROID, virtual = true)

    private fun opened(handler: String?, status: OpenStatus = OpenStatus.OPENED) =
        OpenReport(url = "myapp://x", device = pixel, status = status, handler = handler?.let(::Handler), watchMs = 0)

    private fun case(expect: Expectation) = SuiteCase(name = null, url = "myapp://x", folder = null, expect = expect)

    @Test
    fun passesWhenAnyAppOpensALinkWithoutExpectations() {
        assertEquals(TestStatus.PASSED, Verdicts.judge(case(Expectation()), Platform.ANDROID, opened("com.acme/.Main")).status)
    }

    @Test
    fun matchesTheExpectedAndroidPackage() {
        val verdict = Verdicts.judge(case(Expectation(android = "com.acme")), Platform.ANDROID, opened("com.other/.Main"))

        assertEquals(Verdict(TestStatus.FAILED, "expected com.acme, opened com.other/.Main"), verdict)
    }

    @Test
    fun matchesShortAndFullActivityNames() {
        val expect = Expectation(android = "com.acme/com.acme.ProductActivity")

        assertEquals(TestStatus.PASSED, Verdicts.judge(case(expect), Platform.ANDROID, opened("com.acme/.ProductActivity")).status)
    }

    @Test
    fun failsWhenTheAppCrashes() {
        val verdict = Verdicts.judge(case(Expectation()), Platform.ANDROID, opened("com.acme/.Main", OpenStatus.CRASHED))

        assertEquals(Verdict(TestStatus.FAILED, "com.acme/.Main crashed"), verdict)
    }

    @Test
    fun passesALinkThatMustNotOpenWhenNoAppHandlesIt() {
        val expect = Expectation(opens = false)

        assertEquals(TestStatus.PASSED, Verdicts.judge(case(expect), Platform.ANDROID, opened(null, OpenStatus.UNHANDLED)).status)
        assertEquals(TestStatus.FAILED, Verdicts.judge(case(expect), Platform.ANDROID, opened("com.acme/.Main")).status)
    }

    @Test
    fun cannotVerifyAnIosExpectationWhenTheHandlerIsUnknown() {
        val verdict = Verdicts.judge(case(Expectation(ios = "com.acme")), Platform.IOS, opened(null))

        assertEquals(TestStatus.UNVERIFIED, verdict.status)
    }
}
