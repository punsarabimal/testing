# Quick Tally

![Version](https://img.shields.io/badge/version-v1.0.0-3B82F6?style=flat-square)
![Platform](https://img.shields.io/badge/platform-Android%207.0%2B-10B981?style=flat-square)
![Kotlin](https://img.shields.io/badge/Kotlin-Jetpack%20Compose-1E212B?style=flat-square)
![Material 3](https://img.shields.io/badge/Design-Material%203-3B82F6?style=flat-square)

Quick Tally is a fast, minimalist native Android tally counter and click tracker built for everyday counting tasks. Designed around a clean Obsidian Dark Mode interface, it runs completely on-device with instant tactile feedback and zero external network or database dependencies.

## Key Features

- **Obsidian Dark Mode Design**: Sleek dark interface (`#0D0E12` background, `#16181F` cards, `#1E212B` elevated surfaces) accented with Electric Blue (`#3B82F6`), Emerald Green (`#10B981`), and Crimson Red (`#EF4444`).
- **Tabular Animated Counter**: Displays the primary count in bold `64sp` tabular numerals (`tnum`) with smooth directional animations so digits never shift horizontally during rapid tapping.
- **Customizable Step Increments**: Instantly switch between `+1`, `+5`, and `+10` step sizes via Material 3 filter chips.
- **Tactile Controls & Haptic Feedback**: Large primary `+` button, secondary `-` outlined button, and dedicated reset action with haptic confirmation.
- **Optional Session Labeling**: Attach a quick note or label (such as `Laps`, `Items`, or `Score`) directly inside the counter card.
- **Recent Activity Log**: Tracks the last 10 counter actions (for example, `+5 -> 15` or `Reset to 0`) with timestamps and color-coded status indicators.
- **Adaptive Layout**: Scales cleanly from compact handheld screens to a two-pane layout on tablets and landscape displays.
- **Lightweight Local Architecture**: Runs entirely on-device using a single-activity architecture and lightweight in-memory state with zero third-party network or database overhead.

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Design System**: Material 3 (`androidx.compose.material3`)
- **State Management**: AndroidX Lifecycle `ViewModel` with Compose Snapshot State (`mutableStateOf`, `mutableStateListOf`)
- **Typography**: Locally bundled Plus Jakarta Sans and Inter fonts with OpenType tabular figures (`tnum`)
- **Build System**: Gradle (Kotlin DSL) with Version Catalogs
- **CI/CD**: GitHub Actions automated Debug APK builds and Semantic Versioned GitHub Releases

## Download Pre-Built APK

You can install Quick Tally directly on an Android device without building from source:

1. Open the **Releases** tab on the right side of this GitHub repository (or navigate to `/releases`).
2. Select the latest release (for example, **Release v1.0.0**).
3. Expand the **Assets** section and download the `.apk` file (e.g., `app-debug.apk`).
4. Open the downloaded `.apk` file on your Android device.
5. If prompted, allow installation from unknown sources for your browser or file manager, then tap **Install**.

## Building from Source

Requirements: JDK 17 and the Android SDK.

```bash
chmod +x gradlew
./gradlew assembleDebug
```

The generated debug APK will be located at `app/build/outputs/apk/debug/`.

## Changelog

### v1.0.0
- Initial release of Quick Tally.
- Implemented Obsidian Dark Mode Material 3 theme and locally bundled typography with `64sp` tabular numerals.
- Added animated primary counter card with optional session label input.
- Added `+1`, `+5`, and `+10` step selector chips, tactile `+` / `-` / `Reset` controls, and haptic feedback.
- Added live 10-entry Recent Activity session log with timestamps and clear control.
- Added custom geometric vector adaptive launcher icon and density fallback assets.
- Configured Semantic Versioning and GitHub Actions CI/CD release pipeline.

<!--
Maintenance Policy:
On every future update to Quick Tally, automatically update this README.md alongside versionCode and versionName in app/build.gradle.kts:
1. Update the version badge at the top of this file to match versionName.
2. Update the Key Features list and Tech Stack if new capabilities or libraries are added.
3. Prepend a new version entry under the Changelog section summarizing the changes.
-->
