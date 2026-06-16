# KeyboardHeightCallbackCase

This mini case explains what `DefaultAndroidInput.onKeyboardHeightChanged(...)` does, without requiring Android runtime.

## What it simulates

- Keyboard open / close flow
- Insets (`leftInset`, `rightInset`) affecting `x` and `scaleX`
- `SOFT_INPUT_ADJUST_NOTHING` vs non-`ADJUST_NOTHING`
- Popup refresh behavior
- Floating keyboard close branch

## Run

```powershell
Push-Location "D:\github\LibGdxTool\doc\keyboard-demo"
javac .\KeyboardHeightCallbackCase.java
java KeyboardHeightCallbackCase
Pop-Location
```

## Expected output highlights

- Case 1: `y` becomes negative keyboard height (moves input container up)
- Case 2: `scaleX < 1` and `x` offset appears due to insets
- Case 3: `y = 0` when adjust mode is not `ADJUST_NOTHING`
- Case 4: close event returns early if popup is showing on standard provider
- Case 5: floating keyboard branch marks close behavior

