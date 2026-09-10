package com.libGdx.test.material;

import com.badlogic.gdx.graphics.Color;

/**
 * A small Material-3-like semantic color scheme for libGDX.
 *
 * The important idea is semantic roles: widgets ask for PRIMARY, ON_PRIMARY,
 * SURFACE, etc. They do not hard-code RGB values.
 */
public final class MaterialColorScheme {
    public final boolean dark;

    public final Color primary;
    public final Color onPrimary;
    public final Color primaryContainer;
    public final Color onPrimaryContainer;

    public final Color secondary;
    public final Color onSecondary;
    public final Color secondaryContainer;
    public final Color onSecondaryContainer;

    public final Color tertiary;
    public final Color onTertiary;
    public final Color tertiaryContainer;
    public final Color onTertiaryContainer;

    public final Color error;
    public final Color onError;
    public final Color errorContainer;
    public final Color onErrorContainer;

    public final Color background;
    public final Color onBackground;
    public final Color surface;
    public final Color onSurface;
    public final Color surfaceVariant;
    public final Color onSurfaceVariant;
    public final Color outline;
    public final Color outlineVariant;

    public final Color surfaceContainerLowest;
    public final Color surfaceContainerLow;
    public final Color surfaceContainer;
    public final Color surfaceContainerHigh;
    public final Color surfaceContainerHighest;

    public final Color inverseSurface;
    public final Color inverseOnSurface;
    public final Color inversePrimary;

    private MaterialColorScheme(Builder b) {
        this.dark = b.dark;
        this.primary = copy(b.primary);
        this.onPrimary = copy(b.onPrimary);
        this.primaryContainer = copy(b.primaryContainer);
        this.onPrimaryContainer = copy(b.onPrimaryContainer);
        this.secondary = copy(b.secondary);
        this.onSecondary = copy(b.onSecondary);
        this.secondaryContainer = copy(b.secondaryContainer);
        this.onSecondaryContainer = copy(b.onSecondaryContainer);
        this.tertiary = copy(b.tertiary);
        this.onTertiary = copy(b.onTertiary);
        this.tertiaryContainer = copy(b.tertiaryContainer);
        this.onTertiaryContainer = copy(b.onTertiaryContainer);
        this.error = copy(b.error);
        this.onError = copy(b.onError);
        this.errorContainer = copy(b.errorContainer);
        this.onErrorContainer = copy(b.onErrorContainer);
        this.background = copy(b.background);
        this.onBackground = copy(b.onBackground);
        this.surface = copy(b.surface);
        this.onSurface = copy(b.onSurface);
        this.surfaceVariant = copy(b.surfaceVariant);
        this.onSurfaceVariant = copy(b.onSurfaceVariant);
        this.outline = copy(b.outline);
        this.outlineVariant = copy(b.outlineVariant);
        this.surfaceContainerLowest = copy(b.surfaceContainerLowest);
        this.surfaceContainerLow = copy(b.surfaceContainerLow);
        this.surfaceContainer = copy(b.surfaceContainer);
        this.surfaceContainerHigh = copy(b.surfaceContainerHigh);
        this.surfaceContainerHighest = copy(b.surfaceContainerHighest);
        this.inverseSurface = copy(b.inverseSurface);
        this.inverseOnSurface = copy(b.inverseOnSurface);
        this.inversePrimary = copy(b.inversePrimary);
    }

    private static Color copy(Color c) {
        if (c == null) throw new IllegalArgumentException("All color roles must be provided");
        return new Color(c);
    }

    public static Builder builder(boolean dark) {
        return new Builder(dark);
    }

    /** Common Material 3 baseline-like light palette. */
    public static MaterialColorScheme baselineLight() {
        return builder(false)
            .primary(hex("6750A4")).onPrimary(hex("FFFFFF"))
            .primaryContainer(hex("EADDFF")).onPrimaryContainer(hex("21005D"))
            .secondary(hex("625B71")).onSecondary(hex("FFFFFF"))
            .secondaryContainer(hex("E8DEF8")).onSecondaryContainer(hex("1D192B"))
            .tertiary(hex("7D5260")).onTertiary(hex("FFFFFF"))
            .tertiaryContainer(hex("FFD8E4")).onTertiaryContainer(hex("31111D"))
            .error(hex("B3261E")).onError(hex("FFFFFF"))
            .errorContainer(hex("F9DEDC")).onErrorContainer(hex("410E0B"))
            .background(hex("FFFBFE")).onBackground(hex("1C1B1F"))
            .surface(hex("FFFBFE")).onSurface(hex("1C1B1F"))
            .surfaceVariant(hex("E7E0EC")).onSurfaceVariant(hex("49454F"))
            .outline(hex("79747E")).outlineVariant(hex("CAC4D0"))
            .surfaceContainerLowest(hex("FFFFFF"))
            .surfaceContainerLow(hex("F7F2FA"))
            .surfaceContainer(hex("F3EDF7"))
            .surfaceContainerHigh(hex("ECE6F0"))
            .surfaceContainerHighest(hex("E6E0E9"))
            .inverseSurface(hex("313033")).inverseOnSurface(hex("F4EFF4"))
            .inversePrimary(hex("D0BCFF"))
            .build();
    }

    /** Common Material 3 baseline-like dark palette. */
    public static MaterialColorScheme baselineDark() {
        return builder(true)
            .primary(hex("D0BCFF")).onPrimary(hex("381E72"))
            .primaryContainer(hex("4F378B")).onPrimaryContainer(hex("EADDFF"))
            .secondary(hex("CCC2DC")).onSecondary(hex("332D41"))
            .secondaryContainer(hex("4A4458")).onSecondaryContainer(hex("E8DEF8"))
            .tertiary(hex("EFB8C8")).onTertiary(hex("492532"))
            .tertiaryContainer(hex("633B48")).onTertiaryContainer(hex("FFD8E4"))
            .error(hex("F2B8B5")).onError(hex("601410"))
            .errorContainer(hex("8C1D18")).onErrorContainer(hex("F9DEDC"))
            .background(hex("1C1B1F")).onBackground(hex("E6E1E5"))
            .surface(hex("1C1B1F")).onSurface(hex("E6E1E5"))
            .surfaceVariant(hex("49454F")).onSurfaceVariant(hex("CAC4D0"))
            .outline(hex("938F99")).outlineVariant(hex("49454F"))
            .surfaceContainerLowest(hex("0F0D13"))
            .surfaceContainerLow(hex("1D1B20"))
            .surfaceContainer(hex("211F26"))
            .surfaceContainerHigh(hex("2B2930"))
            .surfaceContainerHighest(hex("36343B"))
            .inverseSurface(hex("E6E1E5")).inverseOnSurface(hex("313033"))
            .inversePrimary(hex("6750A4"))
            .build();
    }

    public static Color hex(String rgbOrRgba) {
        return Color.valueOf(rgbOrRgba);
    }

    public static final class Builder {
        private final boolean dark;
        private Color primary, onPrimary, primaryContainer, onPrimaryContainer;
        private Color secondary, onSecondary, secondaryContainer, onSecondaryContainer;
        private Color tertiary, onTertiary, tertiaryContainer, onTertiaryContainer;
        private Color error, onError, errorContainer, onErrorContainer;
        private Color background, onBackground, surface, onSurface;
        private Color surfaceVariant, onSurfaceVariant, outline, outlineVariant;
        private Color surfaceContainerLowest, surfaceContainerLow, surfaceContainer;
        private Color surfaceContainerHigh, surfaceContainerHighest;
        private Color inverseSurface, inverseOnSurface, inversePrimary;

        private Builder(boolean dark) { this.dark = dark; }

        public Builder primary(Color v) { primary = v; return this; }
        public Builder onPrimary(Color v) { onPrimary = v; return this; }
        public Builder primaryContainer(Color v) { primaryContainer = v; return this; }
        public Builder onPrimaryContainer(Color v) { onPrimaryContainer = v; return this; }
        public Builder secondary(Color v) { secondary = v; return this; }
        public Builder onSecondary(Color v) { onSecondary = v; return this; }
        public Builder secondaryContainer(Color v) { secondaryContainer = v; return this; }
        public Builder onSecondaryContainer(Color v) { onSecondaryContainer = v; return this; }
        public Builder tertiary(Color v) { tertiary = v; return this; }
        public Builder onTertiary(Color v) { onTertiary = v; return this; }
        public Builder tertiaryContainer(Color v) { tertiaryContainer = v; return this; }
        public Builder onTertiaryContainer(Color v) { onTertiaryContainer = v; return this; }
        public Builder error(Color v) { error = v; return this; }
        public Builder onError(Color v) { onError = v; return this; }
        public Builder errorContainer(Color v) { errorContainer = v; return this; }
        public Builder onErrorContainer(Color v) { onErrorContainer = v; return this; }
        public Builder background(Color v) { background = v; return this; }
        public Builder onBackground(Color v) { onBackground = v; return this; }
        public Builder surface(Color v) { surface = v; return this; }
        public Builder onSurface(Color v) { onSurface = v; return this; }
        public Builder surfaceVariant(Color v) { surfaceVariant = v; return this; }
        public Builder onSurfaceVariant(Color v) { onSurfaceVariant = v; return this; }
        public Builder outline(Color v) { outline = v; return this; }
        public Builder outlineVariant(Color v) { outlineVariant = v; return this; }
        public Builder surfaceContainerLowest(Color v) { surfaceContainerLowest = v; return this; }
        public Builder surfaceContainerLow(Color v) { surfaceContainerLow = v; return this; }
        public Builder surfaceContainer(Color v) { surfaceContainer = v; return this; }
        public Builder surfaceContainerHigh(Color v) { surfaceContainerHigh = v; return this; }
        public Builder surfaceContainerHighest(Color v) { surfaceContainerHighest = v; return this; }
        public Builder inverseSurface(Color v) { inverseSurface = v; return this; }
        public Builder inverseOnSurface(Color v) { inverseOnSurface = v; return this; }
        public Builder inversePrimary(Color v) { inversePrimary = v; return this; }

        public MaterialColorScheme build() { return new MaterialColorScheme(this); }
    }
}
