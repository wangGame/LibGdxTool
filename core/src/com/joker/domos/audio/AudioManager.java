package com.joker.domos.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;

import java.util.concurrent.ConcurrentLinkedQueue;

public class AudioManager {
    private volatile boolean proceduralAudioRunning;
    private final int SAMPLE_RATE = 44100;
    private final ConcurrentLinkedQueue<Tone> tones;
    private Thread proceduralThread;
    public AudioManager(){
        this.tones = new ConcurrentLinkedQueue<>();
        startProceduralAudio();
    }

    private static class Holder {
        private static final AudioManager INSTANCE = new AudioManager();
    }

    public static AudioManager getInstance() {
        return Holder.INSTANCE;
    }

    private void startProceduralAudio() {
        AudioDevice audioDevice = null;
        try {
            audioDevice = Gdx.audio.newAudioDevice(SAMPLE_RATE, true);
            proceduralAudioRunning = true;
            AudioDevice ownedDevice = audioDevice;
            proceduralThread = new Thread(() -> runProceduralAudio(ownedDevice), "bossfight-procedural-audio");
            proceduralThread.setDaemon(true);
            proceduralThread.start();
        } catch (RuntimeException exception) {
            proceduralAudioRunning = false;
            if (audioDevice != null) {
                audioDevice.dispose();
            }
            proceduralThread = null;
            Gdx.app.log("AudioManager", "Procedural audio unavailable: " + exception.getMessage());
        }
    }


    private void runProceduralAudio(AudioDevice audioDevice) {
        try {
            while (proceduralAudioRunning) {
                Tone tone = tones.poll();
                if (tone == null) {
                    sleepQuietly(4L);
                    continue;
                }

                writeTone(audioDevice, tone);
            }
        } finally {
            proceduralAudioRunning = false;
            tones.clear();
            audioDevice.dispose();
        }
    }


    private void writeTone(AudioDevice audioDevice, Tone tone) {
        int sampleCount = Math.max(1, (int) (SAMPLE_RATE * tone.getDuration()));
        float[] samples = new float[sampleCount];
        double phase = 0.0;
        int noiseState = 0x6D2B79F5 ^ Float.floatToIntBits(tone.getFrequency());

        for (int i = 0; i < sampleCount; i++) {
            float t = sampleCount == 1 ? 1f : (float) i / (sampleCount - 1);
            float frequency = tone.getFrequency() * lerp(1f, tone.getSweep(), t);
            phase += Math.PI * 2.0 * frequency / SAMPLE_RATE;
            float attack = Math.min(1f, t / 0.12f);
            float decay = 1f - t;
            float envelope = attack * decay * decay;
            float sine = (float) Math.sin(phase);
            float square = sine >= 0f ? 1f : -1f;
            noiseState = noiseState * 1664525 + 1013904223;
            float noise = ((noiseState >>> 8) / 16777215f) * 2f - 1f;
            float tonal = sine * 0.72f + square * 0.28f;
            float mixedWave = tonal * (1f - tone.getNoiseMix()) + noise * tone.getNoiseMix();
            samples[i] = mixedWave * envelope * tone.getVolume();
        }

        if (proceduralAudioRunning) {
            audioDevice.writeSamples(samples, 0, samples.length);
        }
    }

    private float lerp(float from, float to, float alpha) {
        return from + (to - from) * alpha;
    }

    private void sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
        }
    }


    public void playCue(Cue cue) {
        if (cue == null || !proceduralAudioRunning) {
            return;
        }

        tones.offer(createTone(cue));
    }

    private Tone createTone(Cue cue) {
        return new Tone(cue.getFrequency(), cue.getDuration(), cue.getVolume(), cue.getSweep(), cue.getNoiseMix());
    }
}
