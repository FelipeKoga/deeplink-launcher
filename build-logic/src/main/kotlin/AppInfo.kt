object Configuration {
    private const val majorVersion = 1
    private const val minorVersion = 0
    private const val patchVersion = 0

    const val versionName = "$majorVersion.$minorVersion.$patchVersion"
    const val versionCode = majorVersion * 10000 + minorVersion * 100 + patchVersion

    const val targetSdk = 34
    const val compileSdk = 34
    const val minSdk = 24
    const val jvmTarget = "1.8"
}