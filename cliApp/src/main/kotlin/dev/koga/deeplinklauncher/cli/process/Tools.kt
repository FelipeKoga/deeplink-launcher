package dev.koga.deeplinklauncher.cli.process

import java.io.File

internal object Tools {

    val isMac: Boolean = System.getProperty("os.name").lowercase().contains("mac")

    fun findAdb(env: Map<String, String> = System.getenv()): String? {
        val home = System.getProperty("user.home")
        val candidates = buildList {
            env["ANDROID_HOME"]?.let { add("$it/platform-tools/adb") }
            env["ANDROID_SDK_ROOT"]?.let { add("$it/platform-tools/adb") }
            addAll(onPath("adb", env))
            add("$home/Library/Android/sdk/platform-tools/adb")
            add("$home/Android/Sdk/platform-tools/adb")
        }
        return candidates.firstOrNull { File(it).canExecute() }
    }

    fun findXcrun(env: Map<String, String> = System.getenv()): String? {
        if (!isMac) return null
        return (onPath("xcrun", env) + "/usr/bin/xcrun").firstOrNull { File(it).canExecute() }
    }

    private fun onPath(name: String, env: Map<String, String>): List<String> =
        env["PATH"].orEmpty().split(File.pathSeparator).filter(String::isNotBlank).map { "$it/$name" }
}
