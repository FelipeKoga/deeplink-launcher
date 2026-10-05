# deeplink CLI

`deeplink` opens and inspects deeplinks on Android devices and emulators, booted iOS simulators and connected iPhones. It is built for scripts and AI agents:

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

AI agents can load the usage guide as a skill: `npx skills add FelipeKoga/deeplink-launcher --skill deeplink` (source: `skills/deeplink/SKILL.md`).

## Commands

| Command | What it does |
|---------|--------------|
| `deeplink doctor` | Checks adb, xcrun and the running devices, and prints the command that fixes each failed check |
| `deeplink devices` | Lists the running Android devices and emulators, booted iOS simulators and connected iPhones |
| `deeplink open <url>` | Opens the link the way a browser tap would, then reports the handler, the launch time (Android), whether the app is still alive after `--watch` ms, and the crash log if it died |
| `deeplink resolve <url>` | Lists the apps that would handle the link without opening it |
| `deeplink test <suite>` | Opens every link of a suite and checks each one against its expectation |
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

## Test suites

`deeplink test` reads a DeepLink Launcher export, so a collection built in the app can be run as a suite. It can also read a plain file with one link per line (`#` starts a comment). Pass `-` to read the suite from stdin.

```json
{
  "folders": [{ "id": "f1", "name": "Checkout" }],
  "deepLinks": [
    { "link": "myapp://cart", "name": "Cart", "folderId": "f1", "targetPackage": "com.acme.app" },
    { "link": "myapp://product/42", "name": "Product", "expect": { "android": "com.acme.app/.ProductActivity", "ios": "com.acme.ios" } },
    { "link": "myapp://legacy", "name": "Removed route", "expect": { "opens": false } }
  ]
}
```

- **What a link must do to pass:** without expectations, some app opens it and is still running after `--watch` ms.
- **`targetPackage`** is the expected Android app. It is the field the app already exports.
- **`expect.android`** takes a package or a `package/activity` (the short `.Activity` form works too). **`expect.ios`** takes a bundle id. **`expect.opens: false`** means no app may handle the link.
- **`unverified`:** the iOS expectation can't be checked because the opening app can't be identified (web links). These results don't fail the run.
- **`--folder <name>`** runs only one folder of the export.
- **Exit code:** 1 if any link fails.

The app's importer ignores the `expect` field, so a suite file can still be imported into the app.

```
$ deeplink test links.json --platform android
Ran 3 links on Medium_Phone_API_35 (emulator-5554, Android 15)
✓ Cart            com.acme.app/.CartActivity · 412 ms
✗ Product         expected com.acme.app/.ProductActivity, opened com.android.chrome/…
    myapp://product/42
✓ Removed route   not handled, as expected

2 passed, 1 failed
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
- **Android:** emulators and physical devices (USB or wireless debugging) work the same way through adb.
- **iOS simulators:** custom schemes are resolved from each installed app's `Info.plist`. Universal Links (`https`) cannot be resolved from the host, so `resolve` reports `unknown`. `open` still opens them, but can't name the app. Crash detection checks that the app is still running after `--watch` ms.
- **Physical iPhones** (Xcode 15+, paired, Developer Mode on) go through `xcrun devicectl`. It opens a link inside a given app instead of routing it through the system, so:
  - `open` needs `--app <bundle id>`, and `test` needs `expect.ios` per link or a default `--app`;
  - the app is relaunched with the link as an open-URL request (`--terminate-existing --payload-url`), so `continueUserActivity`-based Universal Link handling is not exercised;
  - `resolve` reports `unknown`, because installed apps' URL schemes aren't visible from the host;
  - a crash is detected when the launched process is gone after `--watch` ms. The log is in Xcode › Devices and Simulators.

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
