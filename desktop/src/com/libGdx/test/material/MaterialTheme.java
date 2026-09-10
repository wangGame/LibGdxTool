package com.libGdx.test.material;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;

/**
 * Applies MaterialColorScheme roles to a libGDX Scene2D Skin.
 *
 * This class intentionally implements color theming, not Material layout/shape
 * specs. If you want rounded Material buttons, use a white rounded NinePatch as
 * the base drawable and tint it with the same semantic colors.
 */
public final class MaterialTheme implements Disposable {
    public static final String STYLE_FILLED_BUTTON = "m3-filled";
    public static final String STYLE_TONAL_BUTTON = "m3-tonal";
    public static final String STYLE_TEXT_BUTTON = "m3-text";
    public static final String STYLE_LABEL = "m3-label";

    private final BitmapFont font;
    private final Texture whiteTexture;
    private final TextureRegionDrawable whiteDrawable;
    private MaterialColorScheme scheme;

    public MaterialTheme(BitmapFont font) {
        this.font = font;

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whiteTexture = new Texture(pixmap);
        pixmap.dispose();
        whiteDrawable = new TextureRegionDrawable(new TextureRegion(whiteTexture));
    }

    public MaterialColorScheme getScheme() {
        return scheme;
    }

    /** Apply or re-apply a scheme to a Skin. */
    public void apply(Skin skin, MaterialColorScheme s) {
        this.scheme = s;

        // Semantic color resources: widgets/game code may fetch them by name.
        putColor(skin, "m3-primary", s.primary);
        putColor(skin, "m3-on-primary", s.onPrimary);
        putColor(skin, "m3-primary-container", s.primaryContainer);
        putColor(skin, "m3-on-primary-container", s.onPrimaryContainer);
        putColor(skin, "m3-secondary", s.secondary);
        putColor(skin, "m3-on-secondary", s.onSecondary);
        putColor(skin, "m3-secondary-container", s.secondaryContainer);
        putColor(skin, "m3-on-secondary-container", s.onSecondaryContainer);
        putColor(skin, "m3-tertiary", s.tertiary);
        putColor(skin, "m3-on-tertiary", s.onTertiary);
        putColor(skin, "m3-error", s.error);
        putColor(skin, "m3-on-error", s.onError);
        putColor(skin, "m3-background", s.background);
        putColor(skin, "m3-on-background", s.onBackground);
        putColor(skin, "m3-surface", s.surface);
        putColor(skin, "m3-on-surface", s.onSurface);
        putColor(skin, "m3-surface-variant", s.surfaceVariant);
        putColor(skin, "m3-on-surface-variant", s.onSurfaceVariant);
        putColor(skin, "m3-outline", s.outline);
        putColor(skin, "m3-outline-variant", s.outlineVariant);
        putColor(skin, "m3-surface-container-lowest", s.surfaceContainerLowest);
        putColor(skin, "m3-surface-container-low", s.surfaceContainerLow);
        putColor(skin, "m3-surface-container", s.surfaceContainer);
        putColor(skin, "m3-surface-container-high", s.surfaceContainerHigh);
        putColor(skin, "m3-surface-container-highest", s.surfaceContainerHighest);

        // Component styles. Re-applying the theme updates the Skin styles.
        TextButton.TextButtonStyle filled = new TextButton.TextButtonStyle();
        filled.font = font;
        filled.up = drawable(s.primary);
        filled.over = drawable(mix(s.primary, s.onPrimary, 0.08f));
        filled.down = drawable(mix(s.primary, s.onPrimary, 0.14f));
        filled.disabled = drawable(withAlpha(s.onSurface, 0.12f));
        filled.fontColor = new Color(s.onPrimary);
        filled.overFontColor = new Color(s.onPrimary);
        filled.downFontColor = new Color(s.onPrimary);
        filled.disabledFontColor = withAlpha(s.onSurface, 0.38f);
        skin.add(STYLE_FILLED_BUTTON, filled, TextButton.TextButtonStyle.class);

        TextButton.TextButtonStyle tonal = new TextButton.TextButtonStyle();
        tonal.font = font;
        tonal.up = drawable(s.secondaryContainer);
        tonal.over = drawable(mix(s.secondaryContainer, s.onSecondaryContainer, 0.08f));
        tonal.down = drawable(mix(s.secondaryContainer, s.onSecondaryContainer, 0.14f));
        tonal.disabled = drawable(withAlpha(s.onSurface, 0.12f));
        tonal.fontColor = new Color(s.onSecondaryContainer);
        tonal.overFontColor = new Color(s.onSecondaryContainer);
        tonal.downFontColor = new Color(s.onSecondaryContainer);
        tonal.disabledFontColor = withAlpha(s.onSurface, 0.38f);
        skin.add(STYLE_TONAL_BUTTON, tonal, TextButton.TextButtonStyle.class);

        TextButton.TextButtonStyle text = new TextButton.TextButtonStyle();
        text.font = font;
        text.up = drawable(Color.CLEAR);
        text.over = drawable(withAlpha(s.primary, 0.08f));
        text.down = drawable(withAlpha(s.primary, 0.12f));
        text.fontColor = new Color(s.primary);
        text.overFontColor = new Color(s.primary);
        text.downFontColor = new Color(s.primary);
        text.disabledFontColor = withAlpha(s.onSurface, 0.38f);
        skin.add(STYLE_TEXT_BUTTON, text, TextButton.TextButtonStyle.class);

        Label.LabelStyle label = new Label.LabelStyle(font, s.onSurface);
        skin.add(STYLE_LABEL, label, Label.LabelStyle.class);
    }

    public Drawable drawable(Color color) {
        return whiteDrawable.tint(new Color(color));
    }

    public Drawable surface() { return drawable(scheme.surface); }
    public Drawable surfaceContainer() { return drawable(scheme.surfaceContainer); }
    public Drawable surfaceContainerHigh() { return drawable(scheme.surfaceContainerHigh); }

    private static void putColor(Skin skin, String name, Color color) {
        // Store independent mutable instances; callers changing one resource do
        // not mutate the MaterialColorScheme object.
        skin.add(name, new Color(color), Color.class);
    }

    private static Color withAlpha(Color c, float alpha) {
        return new Color(c.r, c.g, c.b, alpha);
    }

    /** Linear RGB mix suitable for subtle state-layer tinting. */
    private static Color mix(Color base, Color overlay, float amount) {
        float a = Math.max(0f, Math.min(1f, amount));
        return new Color(
            base.r + (overlay.r - base.r) * a,
            base.g + (overlay.g - base.g) * a,
            base.b + (overlay.b - base.b) * a,
            base.a + (overlay.a - base.a) * a
        );
    }

    @Override
    public void dispose() {
        whiteTexture.dispose();
    }
}
