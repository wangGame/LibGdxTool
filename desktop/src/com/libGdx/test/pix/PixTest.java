package com.libGdx.test.pix;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

import java.util.HashMap;

/**
 * @Auther jian xian si qi
 * @Date 2023/9/15 9:43
 *
 *
 */
class PixTest extends LibGdxTestMain {
    public static void main(String[] args) {
        PixTest test = new PixTest();
        test.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
//        Asset.getAsset().getTexture("7.png")
//        FileHandle internal = Gdx.files.internal("assets/7.png");
//        test01();
        test03();
    }

    private void test01() {
        FileHandle internal = Gdx.files.internal("assets/split/test.png");
        byte[] bytes = internal.readBytes();
        Pixmap pixmap = new Pixmap(bytes,0,bytes.length);
        int widthTemp = pixmap.getWidth();
        int heightTemp = pixmap.getHeight();

        Group group = new Group();
        HashMap<Integer,String> hashMap = new HashMap<>();
        Table table = new Table(){{
            int bb = 10;
            int iii = 0;
            for (int i = 0; i < heightTemp/bb; i++) {
                for (int i1 = 0; i1 < widthTemp/bb; i1++) {
                    iii++;
                    Color colorTemp = new Color();
                    int pixel = pixmap.getPixel(i1*bb, i*bb);
                    Color.rgba8888ToColor(colorTemp,pixel);
                    Image image = new Image(Asset.getAsset().getTexture("assets/white_cir.png"));
                    add(image);
                    colorTemp.a = 1.0f;
                    image.setColor(colorTemp);
                    hashMap.put(pixel,"xc");
                }
                row();
            }
            System.out.println(iii);
            pack();
        }};
        System.out.println(hashMap.size());
        group.addActor(table);
        group.setScale(0.4f);
        addActor(group);
    }

    private void test03() {

        // 原图
        Pixmap source = new Pixmap(Gdx.files.internal("assets/split/test.png"));

        // 圆角 mask
        Pixmap mask = new Pixmap(Gdx.files.internal("assets/popups_bg.png"));

        int width = mask.getWidth();
        int height = mask.getHeight();

        // 输出图
        Pixmap result = new Pixmap(
                width,
                height,
                Pixmap.Format.RGBA8888
        );

        // =========================
        // 自动适配原图大小
        // =========================

        result.drawPixmap(
                source,
                0,
                0,
                source.getWidth(),
                source.getHeight(),

                0,
                0,
                width,
                height
        );

        // =========================
        // 应用 mask alpha
        // =========================

        result.setBlending(Pixmap.Blending.None);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {

                // 原图像素
                int srcPixel = result.getPixel(x, y);

                // mask像素
                int maskPixel = mask.getPixel(x, y);

                // 解析颜色
                Color srcColor = new Color();
                Color.rgba8888ToColor(srcColor, srcPixel);

                Color maskColor = new Color();
                Color.rgba8888ToColor(maskColor, maskPixel);

                // 使用 mask alpha
                srcColor.a *= maskColor.a;

                // 写回
                result.drawPixel(
                        x,
                        y,
                        Color.rgba8888(srcColor)
                );
            }
        }

        Texture texture = new Texture(result);

        source.dispose();
        mask.dispose();
        result.dispose();

        Image image = new Image(texture);
        image.setScale(0.7f);
        addActor(image);
    }

    private void test02() {
        FileHandle internal = Gdx.files.internal("assets/split/test.png");
        byte[] bytes = internal.readBytes();
        Pixmap pixmap = new Pixmap(bytes,0,bytes.length);
        int widthTemp = pixmap.getWidth();
        int heightTemp = pixmap.getHeight();

        Group group = new Group();
        HashMap<Integer,String> hashMap = new HashMap<>();
        Table table = new Table(){{
            int bb = 1;
            int iii = 0;
            for (int i = 0; i < heightTemp/bb; i++) {
                for (int i1 = 0; i1 < widthTemp/bb; i1++) {
                    iii++;
                    Color colorTemp = new Color();
                    int pixel = pixmap.getPixel(i1*bb, i*bb);
                    Color.rgba8888ToColor(colorTemp,pixel);
                    Image image = new Image(Asset.getAsset().getTexture("assets/white_cir.png"));
                    add(image);
                    colorTemp.a = 1.0f;
                    image.setColor(colorTemp);
                    hashMap.put(pixel,"xc");
                }
                row();
            }
            System.out.println(iii);
            pack();
        }};
        System.out.println(hashMap.size());
        group.addActor(table);
        group.setScale(0.4f);
        addActor(group);
    }
}
