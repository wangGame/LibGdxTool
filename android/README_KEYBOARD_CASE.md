# Keyboard Height Callback Demo (android module)

This runnable case lives in the `android` module and demonstrates the core logic of `DefaultAndroidInput#onKeyboardHeightChanged`.

## Files

- `android/src/com/joker/puzzle/demo/KeyboardHeightCaseDemoActivity.java`
- `android/src/com/joker/puzzle/demo/KeyboardHeightCaseEngine.java`
- `android/src/com/joker/puzzle/demo/KeyboardHeightCaseEngineRunner.java`
- `android/res/layout/activity_keyboard_case_demo.xml`
- `android/res/values/strings.xml` (new demo strings)
- `android/AndroidManifest.xml` (activity registration)
- `android/build.gradle` (`runKeyboardDemo` task)

## What it shows

- Keyboard open/close behavior (`height == 0` vs `height > 0`)
- Left/right inset impact (`x`, `scaleX`)
- `ADJUST_NOTHING` vs `ADJUST_RESIZE`
- Popup refresh branch

## Quick local logic test (JVM)

```powershell
Push-Location "D:\github\LibGdxTool\android\src"
javac .\com\joker\puzzle\demo\KeyboardHeightCaseEngine.java .\com\joker\puzzle\demo\KeyboardHeightCaseEngineRunner.java
java com.joker.puzzle.demo.KeyboardHeightCaseEngineRunner
Pop-Location
```

## Build Android app

```powershell
Push-Location "D:\github\LibGdxTool"
.\gradlew.bat :android:assembleDebug
Pop-Location
```

## Launch demo activity on device/emulator

```powershell
Push-Location "D:\github\LibGdxTool"
.\gradlew.bat :android:runKeyboardDemo
Pop-Location
```

If your device has no IME animation/insets differences, try another keyboard app or another Android version.

