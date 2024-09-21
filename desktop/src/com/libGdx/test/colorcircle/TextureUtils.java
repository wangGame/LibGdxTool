package com.libGdx.test.colorcircle;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.kw.gdx.asset.Asset;

public class TextureUtils {

    public final static int UNIT_SIZE = 82;
    public final static int BUILDING_SIZE = 100;

    public final static int ICON_SIZE = 16;
    public final static int CARD_HEIGHT = 164;
    public final static int CARD_WIDTH = CARD_HEIGHT * 2;

    public final static int BATTLE_HEIGHT = 800;

    public static TextureRegion unitIcon(int icon) {
        return new TextureRegion(Asset.getAsset().getTexture("textures/units.png"),
                (icon % 10) * UNIT_SIZE, (icon / 10) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
    }

    public static TextureRegion buildingIcon(int icon) {
        return new TextureRegion(Asset.getAsset().getTexture("textures/buildings.png"),
                (icon % 10) * BUILDING_SIZE, (icon / 10) * BUILDING_SIZE, BUILDING_SIZE, BUILDING_SIZE);
    }

    public static TextureRegion cardPicture(int picture) {
        return new TextureRegion(Asset.getAsset().getTexture("textures/cards/cards_" + picture / 5 + ".png"),
                0, (picture % 5) * CARD_HEIGHT, CARD_WIDTH, CARD_HEIGHT);
    }

    public static TextureRegion battleBackground(int nr) {
        return new TextureRegion(Asset.getAsset().getTexture("textures/battles/battle_" + nr + ".jpg"),
                0, 0, 1000, BATTLE_HEIGHT);
    }

    public static TextureRegion icon(int icon) {
        return new TextureRegion(Asset.getAsset().getTexture("textures/icons.png"),
                (icon % 20) * ICON_SIZE, (icon / 20) * ICON_SIZE, ICON_SIZE, ICON_SIZE);
    }

    public static TextureRegion[] split(Texture texture, int width, int height) {
        TextureRegion[][] tmp = TextureRegion.split(texture, width, height);
        int cols = texture.getWidth() / width;
        int rows = texture.getHeight() / height;
        TextureRegion[] regions = new TextureRegion[cols * rows];
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                regions[index++] = tmp[i][j];
            }
        }
        return regions;
    }
}
