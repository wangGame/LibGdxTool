package com.joker.domos.audio;

public enum Cue {
    MENU_MOVE(520f, 0.045f, 0.12f, 1.25f),
    MENU_CONFIRM(390f, 0.08f, 0.16f, 1.85f),
    MENU_BACK(260f, 0.08f, 0.13f, 0.62f),
    PLAYER_SHOOT(860f, 0.035f, 0.09f, 1.18f),
    PLAYER_SPECIAL(280f, 0.18f, 0.18f, 2.35f),
    DASH(170f, 0.07f, 0.11f, 2.1f),
    PLAYER_HIT(160f, 0.12f, 0.2f, 0.48f),
    BOSS_HIT(560f, 0.045f, 0.12f, 0.72f),
    BOSS_VINE_CHARGE(145f, 0.16f, 0.13f, 1.9f, 0.12f),
    BOSS_VINE_STRIKE(190f, 0.14f, 0.2f, 0.28f, 0.42f),
    BOSS_MAGIC_CHARGE(360f, 0.18f, 0.11f, 1.75f, 0.05f),
    BOSS_MAGIC_VOLLEY(760f, 0.1f, 0.15f, 0.62f, 0.08f),
    BOSS_POLLEN_CHARGE(250f, 0.22f, 0.1f, 1.3f, 0.22f),
    BOSS_POLLEN_DROP(125f, 0.16f, 0.16f, 0.5f, 0.3f),
    BOSS_STAGGER(118f, 0.18f, 0.17f, 0.42f, 0.36f),
    BOSS_CHAIN_WARNING(440f, 0.16f, 0.14f, 1.65f, 0.12f),
    BOSS_FINAL_RAGE(72f, 0.42f, 0.23f, 0.28f, 0.52f),
    BOSS_PHASE_ROAR(88f, 0.48f, 0.24f, 0.34f, 0.58f),
    BOSS_PHASE_SHOCKWAVE(64f, 0.18f, 0.2f, 0.2f, 0.48f),
    BOSS_DEFEAT_EXPLOSION(96f, 0.085f, 0.21f, 0.24f, 0.82f),
    DEFEAT(190f, 0.22f, 0.16f, 0.55f);

    private final float frequency;
    private final float duration;
    private final float volume;
    private final float sweep;
    private final float noiseMix;

    Cue(float frequency, float duration, float volume, float sweep) {
        this(frequency, duration, volume, sweep, 0f);
    }

    Cue(float frequency, float duration, float volume, float sweep, float noiseMix) {
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
