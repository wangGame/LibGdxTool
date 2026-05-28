package com.libGdx.test.sc;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import java.util.ArrayList;

public class SeneTest extends Group {

    private Texture whiteTexture;
    private BitmapFont font;

    public SeneTest() {
        setSize(420, 700);
        setPosition(100, 60);

        createDebugAssets();

        ArrayList<String> dataList = new ArrayList<>();

        for (int i = 0; i < 500; i++) {
            dataList.add("Level " + (i + 1));
        }

        VirtualScrollList<String> virtualList = new VirtualScrollList<>(
                420,
                700,
                80,
                new VirtualScrollList.Adapter<String>() {

                    @Override
                    public Actor createView() {
                        ItemActor itemActor = new ItemActor(whiteTexture, font);

                        System.out.println("真正创建了一个 ItemActor");

                        return itemActor;
                    }

                    @Override
                    public void bindView(Actor view, String data, int index) {
                        ItemActor itemActor = (ItemActor) view;
                        itemActor.setData(data, index);
                    }
                }
        );

        virtualList.setItems(dataList);
        addActor(virtualList);
    }

    private void createDebugAssets() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();

        whiteTexture = new Texture(pixmap);
        pixmap.dispose();

        font = new BitmapFont();
    }

}