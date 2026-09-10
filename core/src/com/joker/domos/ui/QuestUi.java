package com.joker.domos.ui;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

/** Quest chrome borrows shared fonts and owns its textures and optional plain dialogue font. */
public final class QuestUi implements Disposable {
    public static final Color PAPER = Color.valueOf("F2DCA8");
    public static final Color INK = Color.valueOf("493A27");
    public static final Color JADE = Color.valueOf("9EC99B");
    public final Drawable frame, inset, parchment, selected, red, blue;
    private final Skin skin;
    private final Array<Texture> textures = new Array<>();
    private BitmapFont dialogueFont;

    public QuestUi(Skin skin) {
        this.skin = skin;
        frame = patch("1B2B31", "B68B4D", true);
        inset = patch("12242D", "786441", false);
        parchment = patch("D6BD91", "9D7944", true);
        selected = patch("792F23", "E7C16F", true);
        red = patch("8B3224", "D5AE62", true);
        blue = patch("24444C", "B8985B", true);
    }

    private Drawable patch(String base, String edge, boolean ornament) {
        Pixmap p = new Pixmap(64,64,Pixmap.Format.RGBA8888);
        Color color = Color.valueOf(base);
        p.setColor(Color.valueOf(edge)); p.fillRectangle(4,0,56,64); p.fillRectangle(0,4,64,56);
        p.setColor(Color.valueOf("4D3C2B")); p.fillRectangle(4,4,56,56);
        // Fine deterministic grain and stepped highlights survive NinePatch stretching.
        for (int y=6;y<58;y++) for(int x=6;x<58;x++) {
            float grain=(((x*31+y*47+x*y*7)%19)-9)*.0015f+(58-y)*.0006f;
            p.setColor(color.r+grain,color.g+grain,color.b+grain,1);
            p.drawPixel(x,y);
        }
        p.setColor(Color.valueOf(edge)); p.drawRectangle(6,6,52,52);
        if (ornament) {
            p.setColor(Color.valueOf("E6C58C"));
            for(int side=0;side<2;side++) {
                int x=side==0?8:55, sign=side==0?1:-1;
                p.drawLine(x,8,x+sign*12,8); p.drawLine(x,8,x,20);
                p.drawLine(x,55,x+sign*12,55); p.drawLine(x,55,x,43);
                p.drawLine(x+sign*4,12,x+sign*8,16); p.drawLine(x+sign*8,16,x+sign*4,20);
            }
        }
        Texture t=new Texture(p); p.dispose(); textures.add(t);
        t.setFilter(Texture.TextureFilter.Nearest,Texture.TextureFilter.Nearest);
        NinePatch patch=new NinePatch(t,24,24,24,24);
        patch.setPadding(16,16,12,12);
        return new NinePatchDrawable(patch);
    }

    public Label label(String text,float size,Color color) {
        Label l=new Label(text,new Label.LabelStyle(skin.getFont("font"),color));
        l.setFontScale(size/22f); return l;
    }

    public TextButton button(String text, boolean primary) {
        TextButton.TextButtonStyle style=new TextButton.TextButtonStyle(skin.get(TextButton.TextButtonStyle.class));
        style.up=primary?red:blue; style.down=selected; style.over=selected;
        style.fontColor=PAPER; style.disabledFontColor=Color.valueOf("A49981");
        style.disabled=inset;
        return new TextButton(text,style);
    }
//
//    /** Dark ink on parchment needs unoutlined glyphs; bake once for this screen's conversations. */
//    public Label dialogueLine(String text) {
//        if (dialogueFont == null) {
//            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/nanhai-cjk.ttf"));
//            try {
//                FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
//                params.size = 28;
//                params.characters = FreeTypeFontGenerator.DEFAULT_CHARS
//                        + Gdx.files.internal("fonts/ui-chars.txt").readString("UTF-8");
//                dialogueFont = generator.generateFont(params);
//            } finally { generator.dispose(); }
//        }
//        return new Label(text, new Label.LabelStyle(dialogueFont, INK));
//    }

    @Override public void dispose() {
        for(Texture t:textures)t.dispose(); textures.clear();
        if (dialogueFont != null) { dialogueFont.dispose(); dialogueFont = null; }
    }
}
