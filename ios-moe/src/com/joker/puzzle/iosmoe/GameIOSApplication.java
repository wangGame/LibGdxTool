package com.joker.puzzle.iosmoe;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.backends.iosmoe.IOSAudio;
import com.badlogic.gdx.backends.iosmoe.IOSApplication;
import com.badlogic.gdx.backends.iosmoe.IOSApplicationConfiguration;
import com.badlogic.gdx.backends.iosmoe.IOSGraphics;
import com.badlogic.gdx.backends.iosmoe.IOSInput;
import com.badlogic.gdx.backends.iosmoe.IOSUIViewController;
import com.badlogic.gdx.backends.iosmoe.OALIOSAudio;
import com.badlogic.gdx.files.FileHandle;

public class GameIOSApplication extends IOSApplication {
    private final IOSApplicationConfiguration configuration;

    public GameIOSApplication(ApplicationListener listener, IOSApplicationConfiguration config) {
        super(listener, config);
        this.configuration = config;
    }

    @Override
    protected IOSGraphics createGraphics() {
        return new GameIOSGraphics(this, configuration, (IOSInput) getInput());
    }

    // 使用保留原生占位字符的输入实现，保证软键盘退格事件不会丢失。
    @Override
    protected IOSInput createInput() {
        return new GameIOSInput(this);
    }

    @Override
    protected IOSUIViewController createUIViewController(IOSGraphics graphics) {
        return GameIOSUIViewController.alloc().init(this, graphics);
    }

    @Override
    protected IOSAudio createAudio(IOSApplicationConfiguration config) {
        if (!config.useAudio) return super.createAudio(config);
        return new OALIOSAudio(config) {
            @Override public Sound newSound(FileHandle file) { return super.newSound(iosAudioFile(file)); }
            @Override public Music newMusic(FileHandle file) { return super.newMusic(iosAudioFile(file)); }
        };
    }

    private static FileHandle iosAudioFile(FileHandle file) {
        return "ogg".equalsIgnoreCase(file.extension()) ? file.sibling(file.nameWithoutExtension() + ".mp3") : file;
    }
}
