package com.kw.gdx.sound.soundspeed;


import com.badlogic.gdx.audio.Sound;

/** Implementation of SoundPlayer based on libgdx */
public class DefaultSoundPlayer implements SoundPlayer {
    private final SoundThreadManager mSoundThreadManager;
    private final Sound mSound;
    private long mId = -1;
    private boolean mLooping = false;
    private float mVolume = 1;
    private float mPitch = 1;
    private boolean mMuted = false;

    public DefaultSoundPlayer(SoundThreadManager soundThreadManager, Sound sound) {
        mSoundThreadManager = soundThreadManager;
        mSound = sound;
    }

    @Override
    public void play() {
        if (mMuted) {
            return;
        }
        stop();
        mId = mSoundThreadManager.play(mSound, mVolume, mPitch);
    }

    @Override
    public void loop() {
        if (mMuted) {
            return;
        }
        stop();
        mId = mSoundThreadManager.loop(mSound, mVolume, mPitch);
        mLooping = true;
    }

    @Override
    public void stop() {
        if (mId == -1) {
            return;
        }
        mSoundThreadManager.stop(mId);
        mId = -1;
        mLooping = false;
    }

    @Override
    public float getVolume() {
        return mVolume;
    }

    @Override
    public void setVolume(float volume) {
        mVolume = volume;
        updateVolume();
    }

    @Override
    public float getPitch() {
        return mPitch;
    }

    @Override
    public void setPitch(float pitch) {
        mPitch = pitch;
        if (mId != -1) {
            mSoundThreadManager.setPitch(mId, mPitch);
        }
    }

    @Override
    public boolean isLooping() {
        return mLooping;
    }

    void setMuted(boolean muted) {
        mMuted = muted;
        updateVolume();
        if (mMuted) {
            stop();
        }
    }

    private void updateVolume() {
        if (mId != -1) {
            mSoundThreadManager.setVolume(mId, mMuted ? 0 : mVolume);
        }
    }
}
