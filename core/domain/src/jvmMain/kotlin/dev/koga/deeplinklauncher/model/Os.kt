package dev.koga.deeplinklauncher.model

enum class Os {
    LINUX,
    WINDOWS;

    companion object {
        fun get(
            osName: String = System
                .getProperty("os.name")
                .lowercase(),
        ): Os {
            return osName.let {
                when {
                    it.contains("linux") -> LINUX
                    it.contains("windows") -> WINDOWS
                    else -> throw IllegalStateException("Unsupported OS: $it")
                }
            }
        }
    }
}
