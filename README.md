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
  <a href="https://github.com/FelipeKoga/deeplink-launcher/actions/workflows/quality.yml.yml">
    <img src="https://github.com/FelipeKoga/deeplink-launcher/actions/workflows/quality.yml/badge.svg" alt="Lint and unit tests" />
  </a>
  <a href="https://github.com/FelipeKoga/deeplink-launcher/actions/workflows/deploy-release.yml">
    <img src="https://github.com/FelipeKoga/deeplink-launcher/actions/workflows/deploy-release.yml/badge.svg" alt="Deploy Release" />
  </a>
</p>

<p align="center">
  <a href='https://play.google.com/store/apps/details?id=dev.koga.deeplinklauncher.android'><img alt='Get it on Google Play' src='https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white'/></a>
  <a href='https://github.com/FelipeKoga/deeplink-launcher/releases/latest'><img alt="Linux" src='https://img.shields.io/badge/Linux-FCC624?style=for-the-badge&logo=linux&logoColor=black'/></a>
  <a href='https://github.com/FelipeKoga/deeplink-launcher/releases/latest'><img alt='Windows' src='https://img.shields.io/badge/Windows-0078D6?style=for-the-badge&logo=windows&logoColor=white'/></a>
</p>

## Screenshots

#### Android
![Screenshot 1](docs/screenshots/android-screenshot.png)

#### Desktop
![Screenshot 2](docs/screenshots/desktop-screenshot.png)

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
      <td>iOS</td>
      <td>✅</td>
    </tr>
    <tr>
      <td>Desktop</td>
      <td>✅</td>
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
<div>
  <table>
    <tr>
      <th>Technology</th>
      <th>Description</th>
    </tr>
    <tr>
      <td><a href="https://github.com/JetBrains/compose-jb">Compose Multiplatform</a></td>
      <td>Modern UI toolkit for building native UIs across all platforms.</td>
    </tr>
    <tr>
      <td><a href="https://github.com/adrielcafe/voyager">Voyager</a></td>
      <td>A multiplatform navigation library.</td>
    </tr>
    <tr>
      <td><a href="https://insert-koin.io/">Koin</a></td>
      <td>Dependency injection framework for Kotlin.</td>
    </tr>
    <tr>
      <td><a href="https://cashapp.github.io/sqldelight/">SQLDelight</a></td>
      <td>Multiplatform database library generating typesafe Kotlin APIs.</td>
    </tr>
    <tr>
      <td><a href="https://developer.android.com/jetpack/androidx/releases/datastore">DataStore</a></td>
      <td>Modern data storage solution for storing key-value pairs or protocol buffers with Kotlin coroutines.</td>
    </tr>
    <tr>
      <td><a href="https://m3.material.io/">Material3</a></td>
      <td>Design system that provides a consistent, intuitive set of user experiences across platforms.</td>
    </tr>
    <tr>
      <td><a href="https://github.com/Kotlin/kotlinx.collections.immutable">Kotlinx Immutable</a></td>
      <td>Immutable collections for Kotlin.</td>
    </tr>
    <tr>
      <td><a href="https://github.com/Kotlin/kotlinx-datetime">Kotlinx DateTime</a></td>
      <td>Library for working with date and time in Kotlin.</td>
    </tr>
    <tr>
      <td><a href="https://github.com/mikepenz/AboutLibraries">AboutLibraries</a></td>
      <td>Library for displaying open-source libraries in Android apps.</td>
    </tr>
    <tr>
      <td><a href="https://github.com/detekt/detekt">Detekt</a></td>
      <td>Kotlin static code analysis tool.</td>
    </tr>
    <tr>
      <td><a href="https://github.com/pinterest/ktlint">Ktlint</a></td>
      <td>Kotlin linter for code style.</td>
    </tr>
    <tr>
      <td><a href="https://github.com/Wavesonics/compose-multiplatform-file-picker">MPFilePicker</td>
      <td>Compose multiplatform file picker</td>
    </tr>
    <tr>
      <td><a href="https://github.com/chrisbanes/material3-windowsizeclass-multiplatform">Material 3 Window Size Class</td>
      <td>Library for detecting device window size</td>
    </tr>
  </table>
</div>

## Architecture diagram

![Architecture Diagram](docs/diagram/architecture_diagram.png)
