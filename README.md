<p align="center">
  <img src="androidApp/src/main/res/mipmap-xxxhdpi/ic_launcher_round.webp" alt="DeepLink Launcher Logo" />
</p>

<h1 align="center">DeepLink Launcher</h1>

<p align="center">
  Simplify deeplink management with DeepLink Launcher. Built on Compose Multiplatform and Material3, this tool offers developers and QA teams an intuitive interface for executing, organizing, tracking, and sharing deeplinks.
</p>

<p align="center">
  <a href="https://github.com/FelipeKoga/deeplink-launcher/stargazers">
    <img src="https://img.shields.io/github/stars/FelipeKoga/deeplink-launcher" alt="Stars" />
  </a>
  <a href="https://github.com/FelipeKoga/deeplink-launcher/actions/workflows/run-tests.yml">
    <img src="https://github.com/FelipeKoga/deeplink-launcher/actions/workflows/run-tests.yml/badge.svg" alt="Run Unit Tests" />
  </a>
  <a href="https://github.com/FelipeKoga/deeplink-launcher/actions/workflows/deploy-release.yml">
    <img src="https://github.com/FelipeKoga/deeplink-launcher/actions/workflows/deploy-release.yml/badge.svg" alt="Deploy Release" />
  </a>
</p>

<p align="center">
  <a href='https://play.google.com/store/apps/details?id=dev.koga.deeplinklauncher.android'><img alt='Get it on Google Play' src='https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png' width=200/></a>
</p>


## Platforms

<div>
  <table>
    <tr>
      <th>Platform</th>
      <th>Status</th>
    </tr>
    <tr>
      <td>Android</td>
      <td>✅</td>
    </tr>
    <tr>
      <td>Desktop</td>
      <td>✅</td>
    </tr>
    <tr>
      <td>iOS</td>
      <td>Planned</td>
    </tr>
    <tr>
      <td>MacOS</td>
      <td>Planned</td>
    </tr>
  </table>

  <i>Check out the <a href="https://github.com/FelipeKoga/deeplink-launcher/releases">releases page</a> for downloading the desktop executables and Android APK.</i>
</div>

## Features
- **Execute Deeplinks**: Quickly and easily execute deeplinks. In the desktop version, ADB is used to automatically trigger the deeplink inside the emulator/device, while on Android, the app utilizes Uri for the same purpose.
- **Deeplink History**: Track all executed deeplinks.
- **Favorites**: Mark deeplinks as favorites for quick access.
- **Folders**: Organize deeplinks into folders for efficient management.
- **Export/Import**: Share or backup deeplinks and folders in JSON or TXT formats.
- **Duplicate DeepLink**: Easily duplicate existing deeplinks, saving time when creating similar entries or variations.
- **Theme Support**: Customize your experience by switching between Light and Dark modes.
- **Search**: Quickly find deeplinks by searching for keywords.

## Built With
- MVVM
- [Compose Multiplatform](https://github.com/JetBrains/compose-jb)
- [Voyager](https://github.com/adrielcafe/voyager)
- [Koin](https://insert-koin.io/)
- [SQLDelight](https://cashapp.github.io/sqldelight/)
- [DataStore](https://developer.android.com/jetpack/androidx/releases/datastore)
- [Material3](https://m3.material.io/)
- [Kotlinx Immutable](https://github.com/Kotlin/kotlinx.collections.immutable)
- [Kotlinx DateTime](https://github.com/Kotlin/kotlinx-datetime)
- [AboutLibraries](https://github.com/mikepenz/AboutLibraries)
- [Detekt](https://github.com/detekt/detekt)
- [Ktlint](https://github.com/pinterest/ktlint)

## Screenshots

### Android
![Screenshot 1](screenshots/deeplink_launcher_1.png)
![Screenshot 2](screenshots/deeplink_launcher_2.png)

### Desktop
TODO
