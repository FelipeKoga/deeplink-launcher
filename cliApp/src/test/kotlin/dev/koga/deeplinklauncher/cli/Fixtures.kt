package dev.koga.deeplinklauncher.cli

import dev.koga.deeplinklauncher.cli.android.Adb
import dev.koga.deeplinklauncher.cli.ios.Simctl
import dev.koga.deeplinklauncher.cli.process.CommandResult
import dev.koga.deeplinklauncher.cli.process.CommandRunner

internal fun fixture(path: String): String =
    requireNotNull(object {}.javaClass.getResource("/fixtures/$path")) { "missing fixture $path" }.readText()

internal fun success(stdout: String = "") = CommandResult(0, stdout, "")

internal class FakeRunner(private val respond: (String) -> CommandResult) : CommandRunner {
    val calls = mutableListOf<List<String>>()

    override fun run(command: List<String>, input: String?): CommandResult {
        calls += command
        return respond(command.joinToString(" "))
    }
}

internal fun androidOnly(runner: FakeRunner) = Toolchain(adb = Adb("adb", runner), simctl = null, sleep = {})

internal fun iosOnly(runner: FakeRunner) = Toolchain(adb = null, simctl = Simctl("xcrun", runner), sleep = {})

internal fun androidEmulator(extra: (String) -> CommandResult?): FakeRunner = FakeRunner { command ->
    extra(command) ?: when {
        command == "adb devices -l" -> success(fixture("android/devices-l.txt"))
        command.endsWith("emu avd name") -> success("Medium_Phone_API_35\nOK\n")
        command.endsWith("getprop ro.build.version.release") -> success("15\n")
        command.contains("date +") -> success("10-04 18:00:00.000\n")
        else -> success()
    }
}
