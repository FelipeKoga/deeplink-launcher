# Google Play publishing

Android releases go to Google Play through [fastlane supply](https://docs.fastlane.tools/actions/upload_to_play_store/), driven by two GitHub Actions workflows. A version is built and uploaded once; every later track gets the same build through promotion.

## Tracks

| Track | Play Console name | Typical use |
|-------|-------------------|-------------|
| `internal` | Internal testing | Every release lands here first |
| `alpha` | Closed testing | Testers |
| `beta` | Open testing | Public beta |
| `production` | Production | Everyone, optionally as a staged rollout |

## Workflows

### Release (`release.yml`)

Run it from **Actions → Release → Run workflow**.

| Input | Default | Meaning |
|-------|---------|---------|
| `tagname` | | The Git tag and GitHub release name |
| `play-track` | `internal` | The track the AAB is uploaded to. `none` skips Google Play |
| `play-rollout` | `1` | `1` makes the release available to the whole track. A value below 1 (for example `0.1`) starts a staged rollout |

The `build-apk` job builds both the APK (for the GitHub release) and the AAB (for Google Play) with a freshly generated baseline profile. After that, `publish-google-play` uploads the AAB and the R8 `mapping.txt` with `fastlane android deploy`.

### Google Play (`google-play.yml`)

This workflow changes builds that are already on Google Play and never builds anything.

| `action` | Uses | Effect |
|----------|------|--------|
| `promote` | `from-track`, `to-track`, `rollout` | Copies the newest build of `from-track` to `to-track` |
| `rollout` | `to-track`, `rollout` | Changes the staged rollout of the newest build on `to-track`. `1` completes it |

The usual path: Release to `internal`, then promote `internal → alpha`, `alpha → beta` and `beta → production` (with `rollout: 0.1`, for example). Raise the rollout step by step with `action: rollout`.

## Setup

1. In Google Cloud, create a service account and a JSON key for it.
2. In Play Console, open **Users and permissions** and invite the service account's email with release permissions for this app.
3. In GitHub, add the repository secret `PLAY_SERVICE_ACCOUNT_JSON` with the raw JSON key.

## Constraints

- **Version codes:** `versionCode` is derived from the version in `gradle/libs.versions.toml` (`android-majorVersion`, `android-minorVersion`, `android-patchVersion`). Google Play rejects a second upload with the same code, so bump the version before each Release that uploads, and use promotion to move an existing build to another track.
- **Upload key:** the AAB is signed with the release keystore from the `KEYSTORE_BASE64` secret. Google Play accepts it only if that key is the app's upload key in Play App Signing.
- **Staged rollouts:** the internal testing track has no staged rollouts, so keep `rollout` at `1` there. Use a value below `1` only on tracks where Play Console offers staged rollouts, typically `production`.
- **Release notes:** metadata, changelogs, images and screenshots are not uploaded. Edit them in Play Console.

## Running fastlane locally

```bash
bundle install
export PLAY_SERVICE_ACCOUNT_JSON="$(cat path/to/key.json)"
bundle exec fastlane android promote from:internal to:alpha
bundle exec fastlane android rollout track:production rollout:0.5
bundle exec fastlane android deploy track:internal aab:androidApp/build/outputs/bundle/release/androidApp-release.aab
```
