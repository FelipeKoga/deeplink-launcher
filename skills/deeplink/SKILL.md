---
name: deeplink
description: Open, resolve and test deeplinks on Android devices, Android emulators and iOS simulators with the `deeplink` CLI, and read the result as JSON. Use when the user wants to check that a deeplink, app link, universal link or custom URL scheme opens the right app or screen, debug why a link opens the browser or nothing, verify deeplink routing after changing intent filters, AndroidManifest, Info.plist or a router, or run a list of links as a regression suite.
---

# deeplink CLI

`deeplink` sends a link to a running device the way a tap in a browser would, and reports which app handled it, how long it took on Android, and whether the app crashed. Every command takes `--json`, never prompts, and exits with a meaningful code.

Get it from the CLI Build workflow artifacts or build it with `./gradlew :cliApp:installDist` (see docs/CLI.md). Needs adb for Android and Xcode for iOS.

## Workflow

1. `deeplink doctor --json`. When `ready` is false, run the `fix` command of each failed check, or tell the user.
2. `deeplink devices --json`. Several running devices make `open`, `resolve` and `test` fail with exit 3. Pass `--platform android|ios` or `--device <id>`.
3. `deeplink resolve <url> --json` shows who would handle the link without opening it. Use it first when a link "opens the browser instead of the app".
4. `deeplink open <url> --json` opens it and reports the result.
5. `deeplink test <file> --json` runs many links at once (see Suites).

Always quote the URL in the shell: `deeplink open "myapp://cart?id=1&ref=mail"`.

## Reading results

`open` → `status`:

- `opened`: `handler.id` is the app (`package/activity` on Android, bundle id on iOS). `launch` and `timeMs` give the Android cold/warm start and its time.
- `unhandled`: no app accepts the link. On Android this means no activity has a matching `VIEW` + `BROWSABLE` intent filter, so check the scheme, host and path in AndroidManifest. On iOS, no installed app declares the scheme in `CFBundleURLSchemes`.
- `crashed`: the handler died within `--watch` ms (default 1500). `crash` holds the Android crash log; on iOS read `~/Library/Logs/DiagnosticReports`.

`resolve` → `status`: `handled` with `handlers` and `defaultHandler` (`null` plus a note when Android would show a chooser), `unhandled`, or `unknown` for `https` links on iOS (Universal Links can't be checked from the host).

| Exit code | Meaning |
|-----------|---------|
| 0 | Success |
| 1 | The link wasn't handled, the app crashed, or a suite case failed |
| 2 | Bad arguments, text that isn't a link, or a missing suite file |
| 3 | No usable device, several devices without `--device`, or a device timeout |
| 4 | adb or xcrun is missing |

With `--json`, errors are also JSON: `{"error": {"exitCode", "message", "hint"}}`. Follow `hint`.

## Suites

`deeplink test` reads a DeepLink Launcher export, or one link per line (`#` comments). Use `-` for stdin:

```
printf 'myapp://home\nmyapp://cart\n' | deeplink test - --platform android --json
```

To assert the handler, write an export with expectations:

```json
{ "deepLinks": [
  { "link": "myapp://cart", "name": "Cart", "expect": { "android": "com.acme.app/.CartActivity", "ios": "com.acme.ios" } },
  { "link": "myapp://legacy", "name": "Removed", "expect": { "opens": false } }
] }
```

`summary.failed > 0` means exit 1. Each result has `status` (`passed`, `failed` or `unverified`), `reason`, `expected` and `actual`. `unverified` means an iOS web link opened but its app can't be identified; it doesn't fail the run.

## Limits

- **Android:** links go out with the `BROWSABLE` category, as from a browser. An activity that is only reachable internally reports `unhandled`; that is the expected behavior for an external link.
- **iOS:** simulators only. Physical iPhones aren't supported.
- **Shared state:** `open` doesn't reset app state. Rerun with a fresh install when the result depends on login or onboarding.
