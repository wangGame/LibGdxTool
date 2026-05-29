package com.libGdx.test.point;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

public class Point extends LibGdxTestMain {
    private int TABLE_SIZE =  20;                     // 正弦波一周期点数 N = 20
    private float sine_table[];
    float output_buffer[] =new float[200];
    public static void main(String[] args) {
        Point p = new Point();
        p.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        init_sine_table();
        int i = 0;
        for (float v : sine_table) {
            Image image = new Image(Asset.getAsset().getTexture("assets/next.png"));
            image.setSize(10,10);
            image.setPosition(i,v*100+100, Align.center);
            addActor(image);
            i+=30;
        }

        generate_wave();
        i=0;
        for (float v : output_buffer) {
            Image image = new Image(Asset.getAsset().getTexture("assets/next.png"));
            image.setSize(10,10);
            image.setPosition(i,v*100+500, Align.center);
            addActor(image);
            i+=30;
        }
    }

    void init_sine_table() {
        sine_table = new float[TABLE_SIZE];
        for (int i = 0; i < TABLE_SIZE; i++) {
            float angle = 2 * 3.1415926f * i / TABLE_SIZE;
            sine_table[i] = (float) Math.sin(angle);  // 输出 -1~+1
        }
    }


    void generate_wave() {
        int index = 0;
        for (int i = 0; i < 200; i++) {
            output_buffer[i] = sine_table[index++];
            if (index >= TABLE_SIZE) index = 0;
        }
    }
}
