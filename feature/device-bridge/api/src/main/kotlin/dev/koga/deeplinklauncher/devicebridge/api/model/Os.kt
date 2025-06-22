package dev.koga.deeplinklauncher.devicebridge.api.model

enum class Os {
    LINUX,
    WINDOWS,
    MAC,
    ;

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
                    it.contains("mac") -> MAC
                    else -> error("Unsupported OS: $it")
                }
            }
        }
    }
}
