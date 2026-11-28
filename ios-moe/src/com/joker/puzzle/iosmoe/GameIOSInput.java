package com.joker.puzzle.iosmoe;

import apple.uikit.UITextField;
import apple.uikit.UIView;
import com.badlogic.gdx.Input.OnscreenKeyboardType;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.backends.iosmoe.DefaultIOSInput;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;

import java.util.List;

public final class GameIOSInput extends DefaultIOSInput {
    // 原生键盘回调会直接调用处理器；游戏禁用输入时，让这些事件被忽略。
    private static final InputProcessor NO_INPUT = new InputAdapter();

    // 游戏的 iOS 应用实例，用于访问后端创建的屏幕外输入框。
    private final IOSApplication application;

    // 创建能够持续向 LibGDX 发送退格事件的 iOS 输入实现。
    public GameIOSInput(IOSApplication application) {
        super(application);
        this.application = application;
        super.setInputProcessor(NO_INPUT);
    }

    @Override
    public void setInputProcessor(InputProcessor processor) {
        super.setInputProcessor(processor == null ? NO_INPUT : processor);
    }

    @Override
    public InputProcessor getInputProcessor() {
        InputProcessor processor = super.getInputProcessor();
        return processor == NO_INPUT ? null : processor;
    }

    // 在软键盘打开后保留一个原生占位字符，避免空输入框吞掉删除键。
    @Override
    public void setOnscreenKeyboardVisible(boolean visible, OnscreenKeyboardType type) {
        super.setOnscreenKeyboardVisible(visible, type);
        if (!visible || application.getUIViewController() == null) return;
        List<? extends UIView> subviews = application.getUIViewController().view().subviews();
        for (int i = subviews.size() - 1; i >= 0; i--) {
            UIView view = subviews.get(i);
            if (view instanceof UITextField && view.isFirstResponder()) {
                UITextField field = (UITextField) view;
                if (field.text() == null || field.text().isEmpty()) field.setText("x");
                break;
            }
        }
    }
}
