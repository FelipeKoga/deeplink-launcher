package dev.koga.deeplinklauncher.cli.suite

import dev.koga.deeplinklauncher.cli.device.Platform
import dev.koga.deeplinklauncher.cli.output.OpenReport
import dev.koga.deeplinklauncher.cli.output.OpenStatus
import dev.koga.deeplinklauncher.cli.output.TestStatus

internal data class Verdict(val status: TestStatus, val reason: String?)

internal object Verdicts {

    fun judge(case: SuiteCase, platform: Platform, open: OpenReport): Verdict {
        if (!case.expect.opens) {
            return if (open.status == OpenStatus.UNHANDLED) {
                Verdict(TestStatus.PASSED, null)
            } else {
                Verdict(TestStatus.FAILED, "expected no app to handle it, but ${open.handler?.id ?: "an app"} did")
            }
        }

        return when (open.status) {
            OpenStatus.UNHANDLED -> Verdict(TestStatus.FAILED, "no app handled it")
            OpenStatus.CRASHED -> Verdict(TestStatus.FAILED, "${open.handler?.id ?: "the app"} crashed")
            OpenStatus.OPENED -> judgeHandler(expected(case, platform), open.handler?.id, platform)
        }
    }

    private fun judgeHandler(expected: String?, actual: String?, platform: Platform): Verdict = when {
        expected == null -> Verdict(TestStatus.PASSED, null)
        actual == null -> Verdict(TestStatus.UNVERIFIED, "opened, but the app that handled it can't be identified")
        matches(expected, actual, platform) -> Verdict(TestStatus.PASSED, null)
        else -> Verdict(TestStatus.FAILED, "expected $expected, opened $actual")
    }

    private fun expected(case: SuiteCase, platform: Platform): String? = when (platform) {
        Platform.ANDROID -> case.expect.android
        Platform.IOS -> case.expect.ios
    }

    private fun matches(expected: String, actual: String, platform: Platform): Boolean {
        if (platform == Platform.IOS) return expected == actual
        val actualComponent = normalize(actual)
        return if ('/' in expected) normalize(expected) == actualComponent else expected == actualComponent.substringBefore('/')
    }

    private fun normalize(component: String): String {
        val packageName = component.substringBefore('/')
        val className = component.substringAfter('/', "")
        val fullClass = if (className.startsWith(".")) packageName + className else className
        return "$packageName/$fullClass"
    }
}
