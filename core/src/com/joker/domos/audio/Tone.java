package com.joker.domos.audio;

public class Tone {
    private final float frequency;
    private final float duration;
    private final float volume;
    private final float sweep;
    private final float noiseMix;

    public Tone(float frequency, float duration, float volume, float sweep, float noiseMix) {
        this.frequency = frequency;
        this.duration = duration;
        this.volume = volume;
        this.sweep = sweep;
        this.noiseMix = noiseMix;
    }

    public float getFrequency() {
        return frequency;
    }

    public float getDuration() {
        return duration;
    }

    public float getVolume() {
        return volume;
    }

    public float getSweep() {
        return sweep;
    }

    public float getNoiseMix() {
        return noiseMix;
    }
}
