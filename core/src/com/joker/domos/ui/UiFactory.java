package com.joker.domos.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public final class UiFactory {
    static final Color NAVY   = new Color(0.055f, 0.086f, 0.145f, 1f);
    static final Color NAVY2  = new Color(0.09f, 0.14f, 0.22f, 1f);
    static final Color GOLD   = new Color(0.84f, 0.69f, 0.29f, 1f);
    static final Color GOLDB  = new Color(0.98f, 0.83f, 0.42f, 1f);
    static final Color PALEG  = new Color(0.96f, 0.88f, 0.65f, 1f);
    static final Color CINN   = new Color(0.58f, 0.16f, 0.14f, 1f);
    static final Color JADE   = new Color(0.20f, 0.46f, 0.36f, 1f);

    public static Skin create(BitmapFont font, BitmapFont small) {
        Skin skin = new Skin();
        skin.add("font", font);
        skin.add("small", small);

        Pixmap pm = new Pixmap(8, 8, Pixmap.Format.RGBA8888);
        pm.setColor(Color.WHITE);
        pm.fill();
        Texture tex = new Texture(pm);
        pm.dispose();
        skin.add("white", tex);
        skin.add("panel", new TextureRegionDrawable(tex).tint(new Color(0.07f, 0.11f, 0.16f, 0.94f)),
                Drawable.class);

        NinePatch np = new NinePatch(tex, 1, 1, 1, 1);
        skin.add("patch", np);

        Label.LabelStyle ls = new Label.LabelStyle(font, Color.WHITE);
        skin.add("default", ls);
        Label.LabelStyle lss = new Label.LabelStyle(small, Color.WHITE);
        skin.add("small", lss);
        // Gold label styles (v0.27.0 Tang chrome).
        Label.LabelStyle lgold = new Label.LabelStyle(font, PALEG);
        skin.add("gold", lgold);
        Label.LabelStyle lgoldSmall = new Label.LabelStyle(small, PALEG);
        skin.add("goldSmall", lgoldSmall);

        TextButton.TextButtonStyle tbs = new TextButton.TextButtonStyle();
        tbs.font = font;
        tbs.fontColor = Color.WHITE;
        tbs.up = new NinePatchDrawable(np).tint(new Color(0.16f, 0.28f, 0.38f, 0.92f));
        tbs.down = new NinePatchDrawable(np).tint(new Color(0.30f, 0.52f, 0.64f, 0.97f));
        tbs.over = new NinePatchDrawable(np).tint(new Color(0.22f, 0.40f, 0.52f, 0.95f));
        tbs.disabledFontColor = new Color(0.6f, 0.6f, 0.6f, 1f);
        skin.add("default", tbs);

        TextButton.TextButtonStyle acc = new TextButton.TextButtonStyle(tbs);
        acc.up = new NinePatchDrawable(np).tint(new Color(0.22f, 0.42f, 0.28f, 0.92f));
        acc.down = new NinePatchDrawable(np).tint(new Color(0.34f, 0.64f, 0.42f, 0.97f));
        acc.over = new NinePatchDrawable(np).tint(new Color(0.28f, 0.54f, 0.35f, 0.95f));
        skin.add("go", acc);

        TextButton.TextButtonStyle danger = new TextButton.TextButtonStyle(tbs);
        danger.up = new NinePatchDrawable(np).tint(new Color(0.45f, 0.18f, 0.16f, 0.92f));
        danger.down = new NinePatchDrawable(np).tint(new Color(0.68f, 0.28f, 0.24f, 0.97f));
        danger.over = new NinePatchDrawable(np).tint(new Color(0.58f, 0.24f, 0.20f, 0.95f));
        skin.add("danger", danger);

        // Circular action buttons (left-rail 货物/图鉴/港口). Painted at 128px and
        // minified to the button size so the circle edge stays smooth.
        Pixmap cp = new Pixmap(128, 128, Pixmap.Format.RGBA8888);
        cp.setBlending(Pixmap.Blending.None);
        cp.setColor(0f, 0f, 0f, 0f);
        cp.fill();
        float cc = 64f, cr = 60f;
        for (int yy = 0; yy < 128; yy++) {
            for (int xx = 0; xx < 128; xx++) {
                float ddx = xx - cc, ddy = yy - cc;
                float dist = (float) Math.sqrt(ddx * ddx + ddy * ddy);
                if (dist <= cr - 1f) {
                    cp.setColor(1f, 1f, 1f, 1f);
                } else if (dist <= cr + 1f) {
                    cp.setColor(1f, 1f, 1f, Math.max(0f, (cr + 1f - dist) / 2f));
                } else {
                    cp.setColor(0f, 0f, 0f, 0f);
                }
                cp.drawPixel(xx, yy);
            }
        }
        Texture ctex = new Texture(cp);
        cp.dispose();
        skin.add("circle", ctex);
        TextureRegionDrawable circWhite = new TextureRegionDrawable(new TextureRegion(ctex));
        TextButton.TextButtonStyle circ = new TextButton.TextButtonStyle();
        circ.font = font;
        circ.fontColor = Color.WHITE;
        circ.up = circWhite.tint(new Color(0.14f, 0.26f, 0.36f, 0.92f));
        circ.down = circWhite.tint(new Color(0.34f, 0.56f, 0.68f, 0.98f));
        circ.over = circWhite.tint(new Color(0.22f, 0.42f, 0.55f, 0.96f));
        skin.add("circ", circ);

        TextField.TextFieldStyle tfs = new TextField.TextFieldStyle();
        tfs.font = font;
        tfs.fontColor = Color.WHITE;
        tfs.background = new NinePatchDrawable(np).tint(new Color(0.08f, 0.10f, 0.14f, 0.9f));
        tfs.cursor = new TextureRegionDrawable(tex).tint(Color.WHITE);
        tfs.selection = new TextureRegionDrawable(tex).tint(new Color(0.3f, 0.5f, 0.7f, 0.5f));
        skin.add("default", tfs);

        ScrollPane.ScrollPaneStyle sps = new ScrollPane.ScrollPaneStyle();
        sps.background = new NinePatchDrawable(np).tint(new Color(0.05f, 0.08f, 0.10f, 0.55f));
        skin.add("default", sps);

        Window.WindowStyle ws = new Window.WindowStyle();
        ws.titleFont = font;
        ws.titleFontColor = Color.WHITE;
        ws.background = new NinePatchDrawable(np).tint(new Color(0.08f, 0.12f, 0.18f, 0.94f));
        skin.add("default", ws);

        addTangChrome(skin, tex);
        return skin;
    }

    // ------------------------------------------------------- v0.27.0 Tang chrome
    // Gold-rimmed navy panel, rounded gold field pill, gold ring, and the three
    // ornate button styles (cinnabar 登录, navy 注册, jade 加速). All built from
    // tiny Pixmaps so there is nothing to ship but code.
    private static void addTangChrome(Skin skin, Texture tex) {
        // 1. panelGold: navy fill + 1px gold border (9-patch, straight corners).
        Pixmap pg = new Pixmap(12, 12, Pixmap.Format.RGBA8888);
        pg.setColor(Color.CLEAR);
        pg.fill();
        pg.setColor(GOLD);
        pg.fillRectangle(0, 0, 12, 1);
        pg.fillRectangle(0, 11, 12, 1);
        pg.fillRectangle(0, 0, 1, 12);
        pg.fillRectangle(11, 0, 1, 12);
        pg.setColor(0.055f, 0.086f, 0.145f, 0.96f); // navy
        pg.fillRectangle(1, 1, 10, 10);
        Texture tpg = new Texture(pg);
        pg.dispose();
        NinePatch npg = new NinePatch(tpg, 1, 1, 1, 1);
        skin.add("panelGold", npg);

        // 2. pillGold: rounded navy pill with a 2px gold outline (radius ~9).
        Pixmap pl = new Pixmap(28, 28, Pixmap.Format.RGBA8888);
        pl.setColor(Color.CLEAR);
        pl.fill();
        // Gold silhouette (outer), navy inside, with a soft anti-alias hint.
        for (int y = 0; y < 28; y++) {
            for (int x = 0; x < 28; x++) {
                float dxm = Math.max(0f, Math.abs(x - 14f) - (14f - 10f));
                float dym = Math.max(0f, Math.abs(y - 14f) - (14f - 10f));
                float d = (float) Math.sqrt(dxm * dxm + dym * dym);
                if (d <= 10f) {
                    if (d <= 8.2f) {
                        pl.setColor(0.05f, 0.09f, 0.16f, 0.94f);
                    } else {
                        pl.setColor(GOLD);
                    }
                    pl.drawPixel(x, y);
                }
            }
        }
        Texture tpl = new Texture(pl);
        pl.dispose();
        NinePatch npl = new NinePatch(tpl, 10, 10, 10, 10);
        skin.add("pillGold", npl);

        // 3. ringGold: gold annulus for circular chrome (rail rings, checkbox).
        Pixmap rg = new Pixmap(128, 128, Pixmap.Format.RGBA8888);
        rg.setColor(Color.CLEAR);
        rg.fill();
        for (int y = 0; y < 128; y++) {
            for (int x = 0; x < 128; x++) {
                float d = (float) Math.sqrt((x - 64f) * (x - 64f) + (y - 64f) * (y - 64f));
                if (d <= 58f && d >= 50f) {
                    rg.setColor(1f, 0.80f, 0.42f, 1f);
                    rg.drawPixel(x, y);
                }
            }
        }
        Texture trg = new Texture(rg);
        rg.dispose();
        skin.add("ringGold", new TextureRegionDrawable(new TextureRegion(trg)), Drawable.class);

        // 4. Rounded gold-bordered button styles: cinnabar (登录/减速), navy (注册),
        //    jade (加速). Pill base tinted with the fill, gold border preserved.
        TextButton.TextButtonStyle cinnabar = new TextButton.TextButtonStyle();
        cinnabar.font = skin.getFont("font");
        cinnabar.fontColor = new Color(1f, 0.94f, 0.82f, 1f);
        cinnabar.up = new NinePatchDrawable(npl).tint(new Color(0.62f, 0.20f, 0.16f, 0.98f));
        cinnabar.down = new NinePatchDrawable(npl).tint(new Color(0.80f, 0.34f, 0.26f, 1f));
        cinnabar.over = new NinePatchDrawable(npl).tint(new Color(0.72f, 0.27f, 0.21f, 1f));
        cinnabar.disabledFontColor = new Color(0.6f, 0.6f, 0.6f, 1f);
        skin.add("cinnabar", cinnabar);

        TextButton.TextButtonStyle navyBtn = new TextButton.TextButtonStyle();
        navyBtn.font = skin.getFont("font");
        navyBtn.fontColor = new Color(0.93f, 0.95f, 1f, 1f);
        navyBtn.up = new NinePatchDrawable(npl).tint(new Color(0.14f, 0.28f, 0.46f, 0.98f));
        navyBtn.down = new NinePatchDrawable(npl).tint(new Color(0.24f, 0.44f, 0.66f, 1f));
        navyBtn.over = new NinePatchDrawable(npl).tint(new Color(0.19f, 0.36f, 0.56f, 1f));
        navyBtn.disabledFontColor = new Color(0.6f, 0.6f, 0.6f, 1f);
        skin.add("navy", navyBtn);

        TextButton.TextButtonStyle jadeBtn = new TextButton.TextButtonStyle();
        jadeBtn.font = skin.getFont("font");
        jadeBtn.fontColor = new Color(0.92f, 1f, 0.95f, 1f);
        jadeBtn.up = new NinePatchDrawable(npl).tint(new Color(0.18f, 0.48f, 0.36f, 0.98f));
        jadeBtn.down = new NinePatchDrawable(npl).tint(new Color(0.30f, 0.66f, 0.50f, 1f));
        jadeBtn.over = new NinePatchDrawable(npl).tint(new Color(0.24f, 0.57f, 0.43f, 1f));
        jadeBtn.disabledFontColor = new Color(0.6f, 0.6f, 0.6f, 1f);
        skin.add("jade", jadeBtn);

        // 5. goldField: rounded navy field, gold border + gold cursor.
        TextField.TextFieldStyle goldField = new TextField.TextFieldStyle();
        goldField.font = skin.getFont("font");
        goldField.fontColor = new Color(0.97f, 0.94f, 0.85f, 1f);
        goldField.background = new NinePatchDrawable(npl).tint(new Color(0.045f, 0.08f, 0.14f, 0.94f));
        goldField.cursor = new TextureRegionDrawable(tex).tint(GOLDB);
        goldField.selection = new TextureRegionDrawable(tex).tint(new Color(0.45f, 0.55f, 0.30f, 0.5f));
        goldField.messageFont = skin.getFont("small");
        goldField.messageFontColor = new Color(0.55f, 0.58f, 0.62f, 1f);
        skin.add("goldField", goldField);

        // 6. dotGold: small filled gold circle (title flanks, list bullets).
        Pixmap dp = new Pixmap(16, 16, Pixmap.Format.RGBA8888);
        dp.setColor(Color.CLEAR);
        dp.fill();
        for (int y = 0; y < 16; y++) {
            for (int x = 0; x < 16; x++) {
                float dxp = x - 8f, dyp = y - 8f;
                if (dxp * dxp + dyp * dyp <= 36f) {
                    dp.setColor(GOLDB);
                    dp.drawPixel(x, y);
                }
            }
        }
        Texture tdp = new Texture(dp);
        dp.dispose();
        skin.add("dotGold", new TextureRegionDrawable(new TextureRegion(tdp)), Drawable.class);

        // 7. Tang check (login agreement): gold ring unchecked, gold ring + jade
        //    fill checked. Rings are drawn as fat annuli scoped to 24px.
        TextureRegionDrawable checkOff = ringDrawable(24, 24, 7.2f, 8.8f, GOLDB);
        skin.add("checkOff", checkOff, Drawable.class);
        Pixmap ckp = new Pixmap(24, 24, Pixmap.Format.RGBA8888);
        ckp.setColor(Color.CLEAR);
        ckp.fill();
        for (int y = 0; y < 24; y++) {
            for (int x = 0; x < 24; x++) {
                float d = (float) Math.sqrt((x - 12f) * (x - 12f) + (y - 12f) * (y - 12f));
                if (d <= 8.8f && d >= 7.2f) {
                    ckp.setColor(GOLDB);
                    ckp.drawPixel(x, y);
                } else if (d <= 6.4f) {
                    ckp.setColor(0.24f, 0.52f, 0.40f, 1f); // jade fill
                    ckp.drawPixel(x, y);
                } else if (d <= 6.8f) {
                    ckp.setColor(GOLDB);
                    ckp.drawPixel(x, y);
                }
            }
        }
        Texture tck = new Texture(ckp);
        ckp.dispose();
        skin.add("checkOn", new TextureRegionDrawable(new TextureRegion(tck)), Drawable.class);

        CheckBox.CheckBoxStyle tangCheck = new CheckBox.CheckBoxStyle();
        tangCheck.checkboxOff = skin.getDrawable("checkOff");
        tangCheck.checkboxOn = skin.getDrawable("checkOn");
        tangCheck.font = skin.getFont("small");
        tangCheck.fontColor = PALEG;
        skin.add("tang", tangCheck);
    }

    /** Paints a plain gold annulus into its own texture (independent of the
     * 128px ringGold used for the round HUD chrome). */
    private static TextureRegionDrawable ringDrawable(int w, int h, float rIn, float rOut, Color c) {
        Pixmap pm = new Pixmap(w, h, Pixmap.Format.RGBA8888);
        pm.setColor(Color.CLEAR);
        pm.fill();
        float cx = w / 2f, cy = h / 2f;
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                float d = (float) Math.sqrt((x - cx) * (x - cx) + (y - cy) * (y - cy));
                if (d <= rOut && d >= rIn) {
                    pm.setColor(c);
                    pm.drawPixel(x, y);
                }
            }
        }
        Texture t = new Texture(pm);
        pm.dispose();
        return new TextureRegionDrawable(new TextureRegion(t));
    }

    private UiFactory() {}


}