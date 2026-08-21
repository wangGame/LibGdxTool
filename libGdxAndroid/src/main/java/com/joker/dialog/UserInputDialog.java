package com.joker.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;

import com.badlogic.gdx.backends.android.R;


public class UserInputDialog extends DialogFragment {
    private static final long DISMISS_DELAY_MS = 180L;

    public interface OnSubmitListener {
        void onSubmit(String text);
    }

    public interface OnCancelListener {
        void onCancel();
    }

    private OnSubmitListener listener;
    private OnCancelListener onCancelListener;
    private String hintText = "";
    private boolean submitted;
    private boolean closing;

    public UserInputDialog setOnSubmitListener(OnSubmitListener l) {
        this.listener = l;
        return this;
    }

    public UserInputDialog setOnCancelListener(OnCancelListener l) {
        this.onCancelListener = l;
        return this;
    }

    public UserInputDialog setHint(String hint) {
        this.hintText = hint == null ? "" : hint;
        return this;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.FullScreenDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.setCanceledOnTouchOutside(true);

        Window w = dialog.getWindow();
        if (w != null) {
            // 底部对齐、宽度全屏
            w.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            w.setGravity(Gravity.BOTTOM);
            // 关键：让窗口为键盘让位，并在弹出时直接弹出键盘（可选）
            w.setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
                            | WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
            );
            // 背景透明，保留内容圆角
            w.setBackgroundDrawableResource(android.R.color.transparent);
        }


        EditText et = dialog.findViewById(R.id.etInput);
        Button btn = dialog.findViewById(R.id.btnOk);

        if (et != null) {
            et.setHint(hintText);
            et.requestFocus();
            et.post(() -> {
                if (getActivity() == null) return;
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
                }
            });
        }

        // IME 的完成键直接提交
        if (et != null) {
            et.setOnEditorActionListener((v, actionId, event) -> {
                submitAndDismiss(et);
                return true;
            });
            et.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (listener != null && et != null) {
                        listener.onSubmit(et.getText() != null ? et.getText().toString() : "");
                    }
                }
            });
        }


        if (btn != null) {
            btn.setOnClickListener(v -> submitAndDismiss(et));
        }

        return dialog;
    }

    private void submitAndDismiss(EditText et) {
        if (closing) {
            return;
        }
        closing = true;
        submitted = true;
        if (listener != null && et != null) {
            listener.onSubmit(et.getText() != null ? et.getText().toString() : "");
        }
        hideKeyboard(et);
        if (et != null) {
            et.postDelayed(() -> dismissAllowingStateLoss(), DISMISS_DELAY_MS);
        } else {
            dismissAllowingStateLoss();
        }
    }

    @Override
    public void onCancel(android.content.DialogInterface dialog) {
        super.onCancel(dialog);
        if (!submitted && onCancelListener != null) {
            onCancelListener.onCancel();
        }
    }

    @Override
    public void onDismiss(android.content.DialogInterface dialog) {
        EditText et = getDialog() == null ? null : getDialog().findViewById(R.id.etInput);
        hideKeyboard(et);
        super.onDismiss(dialog);
    }

    private void hideKeyboard(EditText et) {
        if (getActivity() == null) return;
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && et != null) {
            imm.hideSoftInputFromWindow(et.getWindowToken(), 0);
        }
    }
}