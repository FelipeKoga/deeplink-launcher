# deeplink CLI

`deeplink` opens and inspects deeplinks on Android devices, Android emulators and booted iOS simulators. It is built for scripts and AI agents:

- every command takes `--json`;
- it never prompts;
- its exit code tells what happened.

## Install

The CLI isn't published to a package manager yet. Get it from the **CLI Build** workflow (Actions → CLI Build → Run workflow), which attaches these artifacts to the run:

| Artifact | Contents |
|----------|----------|
| `native-macos-arm64`, `native-macos-x64`, `native-linux-x64` | `deeplink-<version>-<os>-<arch>.tar.gz` with the native binary, no Java needed. A download from a browser on macOS is quarantined by Gatekeeper; clear it with `xattr -d com.apple.quarantine deeplink` |
| `jvm` | `deeplink-<version>-jvm.tar.gz` with `bin/deeplink` plus jars, for any platform with Java 17+ |

Or build it from source (see below).

## Commands

| Command | What it does |
|---------|--------------|
| `deeplink doctor` | Checks adb, xcrun and the running devices, and prints the command that fixes each failed check |
| `deeplink devices` | Lists the running Android devices and emulators and the booted iOS simulators |
| `deeplink open <url>` | Opens the link the way a browser tap would, then reports the handler, the launch time (Android), whether the app is still alive after `--watch` ms, and the crash log if it died |
| `deeplink resolve <url>` | Lists the apps that would handle the link without opening it |
| `deeplink parse <url>` | Splits the link into scheme, host, path, query and fragment, offline |

`open` and `resolve` pick the only running device. When several devices are running, pass `--device <id>` or narrow with `--platform android|ios`.

```
$ deeplink open "myapp://product/42?ref=email" --platform android
✓ Opened on Medium_Phone_API_35 (emulator-5554, Android 15)
  url       myapp://product/42?ref=email
  handler   com.acme.app/com.acme.ProductActivity
  launch    warm · 412 ms
  alive     yes, after 1500 ms
```

## JSON output

With `--json`, stdout is exactly one JSON object, even on failure. Every object starts with `schemaVersion` (currently `1`) and `command`, and keeps every key, with `null` when a value is unknown. On failure the object is `{"schemaVersion", "command", "error": {"exitCode", "message", "hint"}}`.

## Exit codes

| Code | Meaning |
|------|---------|
| 0 | Success |
| 1 | The link was not handled, the app crashed, or a check failed |
| 2 | Invalid arguments, or text that is not a link |
| 3 | No usable device, an ambiguous device, or a device that did not respond |
| 4 | adb or xcrun is missing |

## Platform notes

- **Android:** the link is sent as a `VIEW` intent with the `BROWSABLE` category, like a tap in a browser. Activities without `BROWSABLE` in their intent filter are reported as not handling the link.
- **iOS:** custom schemes are resolved from each installed app's `Info.plist`. Universal Links (`https`) cannot be resolved from the host, so `resolve` reports `unknown`. `open` still opens them, but can't name the app. Crash detection checks that the app is still running after `--watch` ms.

## Versioning

The CLI has its own version, `version` in `cliApp/build.gradle.kts`, independent of the app. The CLI Build workflow:
- builds and smoke-tests the native binary on macOS arm64, macOS Intel and Linux x64 with GraalVM 21;
- packages the JVM build.

## Building from source

```
./gradlew :cliApp:installDist          # JVM build: cliApp/build/install/deeplink/bin/deeplink
GRAALVM_HOME=/path/to/graalvm-21 ./gradlew :cliApp:nativeImage   # native: cliApp/build/native/deeplink
```

The native binary starts in about 10 ms, against about 0.5 s on the JVM. Supported hosts are macOS and Linux; iOS simulators need macOS.
