# ProfitCalc TJ («Ҳисобкунаки Фоида» / «Калькулятор Прибыли»)

Офлайн Android-барнома барои фурӯшандаҳо ва менеджерони маркетплейс: ҳисоб
кардани нархи фурӯш, комиссия, скидка, логистика, упаковка, реклама, хароҷот
ва фоидаи ҳақиқӣ — **100% бе интернет**.

An offline-first Android app for marketplace sellers and managers to
calculate real profit after commission, discount, logistics, packaging,
advertising, tax and other costs — **no internet connection required, ever**.

---

## 1. Offline guarantee

- No `INTERNET` permission in `AndroidManifest.xml` (check it — it's simply
  not there).
- No Firebase, no Supabase, no REST API, no analytics, no tracking SDK, no
  login.
- All data (history, saved products, settings) lives in a local Room database
  and local DataStore preferences on the device. Nothing ever leaves the
  phone.

## 2. Features

- **Product Profit Calculator** — purchase price, sale price, quantity,
  commission %, logistics, packaging, advertising (as a total budget or a
  per-item cost), discount %, tax %, other costs. Computes profit per item,
  total profit, revenue, total cost, total commission, margin %, and ROI %.
- **Break-even** — minimum sale price for zero profit, and profit at the
  current price.
- **Target profit** — required sale price to reach a chosen total profit.
- **Discount calculator** — quick original-price → discounted-price tool.
- **Maximum discount** — largest discount you can offer while keeping a
  minimum desired profit.
- **ROAS** — revenue ÷ ad spend, with a plain-language explanation.
- **Quick calculator** — a simple +, −, ×, ÷, % calculator, like a phone's
  built-in one.
- **History** — every calculation is saved locally with delete, duplicate,
  edit-by-recalculating, and details view. "Clear all" asks for confirmation.
- **Saved products** — store reusable cost templates (e.g. "iPhone Case") and
  load them straight into the calculator.
- **Export & share** — CSV/TXT export of history, and "Share result" for a
  single calculation, both via Android's native Share sheet.
- **Settings** — language (Tajik / Russian, switches instantly, no restart),
  light/dark/system theme, currency symbol, decimal places, clear history,
  about screen.
- **Dashboard** — today's profit, today's calculation count, saved product
  count, average margin, and quick-action shortcuts.

All monetary results are shown to 2 decimal places by default (configurable
in Settings), e.g. `500.00 сомонӣ`.

## 3. Architecture

```
UI (Jetpack Compose)
   ↓
ViewModel (androidx.lifecycle)
   ↓
CalculatorEngine  (pure Kotlin — no Android dependency, 100% unit-testable)
   ↓
Repository        (HistoryRepository, SavedProductRepository)
   ↓
Local Storage     (Room database + DataStore preferences)
```

`CalculatorEngine.kt` is the single source of truth for every formula in this
app (profit, margin, ROI, break-even, target profit, maximum discount,
ROAS). It is a plain Kotlin `object` with no Android imports, so it can be
tested with fast JVM unit tests — see `app/src/test/.../CalculatorEngineTest.kt`,
which covers all 16 required cases including the exact worked example from
the specification (purchase 280, sale 500, commission 15%, logistics 50,
packaging 10 → profit **85 сомонӣ**, scaling to **850 сомонӣ** at quantity 10).

### Why in-app language switching instead of `values-ru/strings.xml` alone?

Android's resource-qualifier locale system normally needs an Activity
recreation (or the per-app-language API, API 33+) to switch languages at
runtime. Since this app must switch instantly on minSdk 24 devices, UI text
is driven by a small typed model (`i18n/AppStrings.kt`, two instances —
`TjStrings` and `RuStrings`) provided through a `CompositionLocal`. The
`values-ru/strings.xml` file still exists and is used for the OS-level app
label (so the app name shows correctly in the launcher/recent-apps on a
Russian-language phone even before the app opens).

## 4. Tech stack

- Kotlin, Jetpack Compose, Material 3
- Navigation Compose
- Room (local database) + Jetpack DataStore (local preferences)
- Gradle Kotlin DSL, Gradle Wrapper
- JUnit 4 for unit tests
- minSdk 24, targetSdk 35, compileSdk 35, Java 17 toolchain

## 5. Building

### Using GitHub Actions (recommended — this is the intended workflow)

1. Push this repository to GitHub.
2. Open the **Actions** tab — the `Build ProfitCalc TJ` workflow runs
   automatically on every push (or trigger it manually with
   "Run workflow").
3. The workflow: checks out the code, installs JDK 17 and the Android SDK,
   **generates the Gradle wrapper** (see note below), runs all unit tests,
   then builds the debug APK.
4. Download the APK from the run's **Artifacts** section — look for
   **`ProfitCalc-TJ-APK`**. An unsigned release-build artifact is uploaded
   too, for reference.

#### A note on `gradle-wrapper.jar`

This repository intentionally does **not** commit a pre-built
`gradle-wrapper.jar` binary — it's a compiled binary, not source code, and
committing one blindly is a common source of "trust the binary" security
problems. Instead, `gradle/wrapper/gradle-wrapper.properties` is committed
(pinning Gradle 8.9), and the very first CI step provisions Gradle directly
via `gradle/actions/setup-gradle` and runs `gradle wrapper` to (re)generate
`gradlew`, `gradlew.bat`, and `gradle-wrapper.jar` fresh, matching that
properties file exactly. Every step after that uses the regenerated
`./gradlew` as normal. This is a standard, widely used pattern and requires
no action from you.

### Building locally in Termux

Termux has real internet access (unlike the environment this project was
generated in), so you have two options:

```sh
pkg install openjdk-17 gradle
cd ProfitCalcTJ
gradle wrapper --gradle-version 8.9   # one-time: generates gradlew + the jar
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

The APK will be at `app/build/outputs/apk/debug/app-debug.apk`.

### Building in Android Studio

Just open the project folder — Android Studio will offer to generate the
wrapper automatically if it's missing, or you can run `gradle wrapper` from
its embedded terminal first.

## 6. Project structure

```
ProfitCalcTJ/
├── .github/workflows/build.yml       — CI: test + build + upload APK
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml   — no INTERNET permission
│       │   ├── java/com/profitcalc/tj/
│       │   │   ├── MainActivity.kt
│       │   │   ├── ProfitCalcApp.kt          — Application: wires singletons
│       │   │   ├── engine/                   — CalculatorEngine (pure math)
│       │   │   ├── data/
│       │   │   │   ├── local/                — Room entities, DAOs, DB
│       │   │   │   ├── prefs/                — DataStore settings
│       │   │   │   └── repository/           — Engine ↔ storage bridge
│       │   │   ├── viewmodel/                — One ViewModel per screen/tool
│       │   │   ├── i18n/                     — Tajik/Russian string model
│       │   │   ├── navigation/               — Screen routes + NavHost
│       │   │   ├── ui/
│       │   │   │   ├── theme/                — Material3 color/type/theme
│       │   │   │   ├── components/           — Reusable input/result widgets
│       │   │   │   └── screens/              — One file per screen
│       │   │   └── util/                     — Formatters, CSV/TXT export
│       │   └── res/                          — strings, themes, icon, xml
│       └── test/java/com/profitcalc/tj/engine/
│           └── CalculatorEngineTest.kt        — 20+ unit tests
├── gradle/wrapper/gradle-wrapper.properties
├── settings.gradle.kts
├── build.gradle.kts
├── gradle.properties
└── README.md
```

## 7. Installing the APK

1. Download `app-debug.apk` from the workflow's Artifacts (it comes zipped —
   unzip it first).
2. Transfer it to your Android phone (or download directly on the phone).
3. Open the file. If prompted, allow "install from this source" for your
   file manager/browser — this is required by Android for any APK not
   installed from the Play Store, offline apps included.
4. Install and open. No sign-up, no internet, no permissions beyond storage
   access for CSV/TXT export.

## 8. License

MIT — see `LICENSE`.
