# deeplink CLI

`deeplink` opens and inspects deeplinks on Android devices, Android emulators and booted iOS simulators. It is built for scripts and AI agents:

- every command takes `--json`;
- it never prompts;
- its exit code tells what happened.

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
| 2 | Invalid arguments or text that is not a link. Running `deeplink` with no command prints the help and also exits with 2 |
| 3 | No usable device, an ambiguous device, or a device that did not respond |
| 4 | adb or xcrun is missing |

## Platform notes

- **Android:** the link is sent as a `VIEW` intent with the `BROWSABLE` category, like a tap in a browser. Activities without `BROWSABLE` in their intent filter are reported as not handling the link.
- **iOS:** custom schemes are resolved from each installed app's `Info.plist`. Universal Links (`https`) cannot be resolved from the host, so `resolve` reports `unknown`. `open` still opens them, but can't name the app. Crash detection checks that the app is still running after `--watch` ms.

## Building from source

```
./gradlew :cliApp:installDist
cliApp/build/install/deeplink/bin/deeplink doctor
```

Requires a JDK 17+ at runtime. Supported hosts are macOS and Linux; iOS simulators need macOS.
