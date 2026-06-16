package com.joker.puzzle.demo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.tony.puzzle.R;

/**
 * Runnable Android demo for keyboard-height behavior.
 * It visualizes the same branches used by DefaultAndroidInput#onKeyboardHeightChanged.
 */
public class KeyboardHeightCaseDemoActivity extends Activity {

    private LinearLayout root;
    private LinearLayout inputContainer;
    private AutoCompleteTextView editText;
    private TextView status;
    private Button btnToggleMode;

    private final KeyboardHeightCaseEngine engine = new KeyboardHeightCaseEngine();
    private boolean useAdjustNothing = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyboard_case_demo);

        root = findViewById(R.id.root);
        inputContainer = findViewById(R.id.inputContainer);
        editText = findViewById(R.id.editText);
        status = findViewById(R.id.status);
        btnToggleMode = findViewById(R.id.btnToggleMode);
        Button btnShowKeyboard = findViewById(R.id.btnShowKeyboard);
        Button btnHideKeyboard = findViewById(R.id.btnHideKeyboard);

        setSoftInputMode();

        btnShowKeyboard.setOnClickListener(v -> showKeyboard());
        btnHideKeyboard.setOnClickListener(v -> hideKeyboard());
        btnToggleMode.setOnClickListener(v -> {
            useAdjustNothing = !useAdjustNothing;
            setSoftInputMode();
            applyCurrentInsets();
        });

        ViewCompat.setOnApplyWindowInsetsListener(root, (v, insets) -> {
            applyInsetsToEngine(insets);
            return insets;
        });

        root.post(this::applyCurrentInsets);
    }

    private void setSoftInputMode() {
        int mode = useAdjustNothing
            ? WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
            : WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
        getWindow().setSoftInputMode(mode);
        btnToggleMode.setText(useAdjustNothing
            ? R.string.keyboard_case_mode_adjust_nothing
            : R.string.keyboard_case_mode_adjust_resize);
    }

    private void applyCurrentInsets() {
        WindowInsetsCompat insets = ViewCompat.getRootWindowInsets(root);
        if (insets != null) {
            applyInsetsToEngine(insets);
        }
    }

    private void applyInsetsToEngine(WindowInsetsCompat insets) {
        Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());
        Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
        Insets cutout = insets.getInsets(WindowInsetsCompat.Type.displayCutout());

        int leftInset = Math.max(systemBars.left, cutout.left);
        int rightInset = Math.max(systemBars.right, cutout.right);
        boolean imeVisible = insets.isVisible(WindowInsetsCompat.Type.ime());
        int height = imeVisible ? imeInsets.bottom : 0;

        KeyboardHeightCaseEngine.Input input = new KeyboardHeightCaseEngine.Input();
        input.height = height;
        input.leftInset = leftInset;
        input.rightInset = rightInset;
        input.nativeInputOpen = true;
        input.standardHeightProvider = true;
        input.popupShowing = editText.isPopupShowing();
        input.useImmersiveMode = false;
        input.softButtonsBarHeight = 0;
        input.adjustNothing = useAdjustNothing;
        input.keyboardLandscapeHeight = 0;
        input.keyboardPortraitHeight = 0;
        input.editTextHeight = editText.getHeight();
        input.screenWidth = root.getWidth();

        KeyboardHeightCaseEngine.Output out = engine.apply(input);

        if (!out.skipLayoutUpdate) {
            inputContainer.animate()
                .y(out.y)
                .x(out.x)
                .scaleX(out.scaleX)
                .setDuration(100)
                .start();

            if (out.popupRefreshed) {
                editText.showDropDown();
            }
        }

        String text = getString(
            R.string.keyboard_case_status_template,
            height,
            leftInset,
            rightInset,
            out.y,
            out.x,
            out.scaleX,
            out.observerHeight,
            useAdjustNothing ? "ADJUST_NOTHING" : "ADJUST_RESIZE"
        );
        status.setText(text);
    }

    private void showKeyboard() {
        editText.requestFocus();
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
        editText.clearFocus();
    }
}

