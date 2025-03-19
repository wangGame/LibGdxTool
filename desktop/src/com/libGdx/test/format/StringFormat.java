package com.libGdx.test.format;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kw.gdx.asset.Asset;
import com.libGdx.test.base.LibGdxTestMain;

import org.lwjgl.Sys;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Auther jian xian si qi
 * @Date 2023/9/12 18:25
 */
public class StringFormat extends LibGdxTestMain {
    public static void main(String[] args) {

//        date.setTime(System.currentTimeMillis());
//        int year = date.getYear();
//        int month = date.getMonth();
//        int day = date.getDay();
//
////        String format1 = String.format("%tb:%2d :%2d", year, month, day);
////        System.out.println(format1);
//        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
//        String dateString = sdf.format(date);
//        System.out.println(dateString);
//        Date date = new Date();
//
//        date.setHours(3);
//        date.setMinutes(14);
//        date.setSeconds(3);
//        System.out.println(String.format("%tT", date));
//

        StringFormat stringFormat = new StringFormat();
        stringFormat.start();
    }

    @Override
    public void useShow(Stage stage) {
        super.useShow(stage);
        stage.addActor(new Table(){{

            {
                BitmapFont bitmapFont = Asset.getAsset().loadBitFont("assets/font/Krub-Bold_52_1.fnt");
                Label label = new Label("ABVCD", new Label.LabelStyle() {{
                    font = bitmapFont;
                }});

                add(label);
            }
            {
                BitmapFont bitmapFont = Asset.getAsset().loadBitFont("assets/font/Krub-Bold_redStroke_52.fnt");
                Label label = new Label("ABVCD",new Label.LabelStyle(){{
                    font = bitmapFont;
                }});
                add(label);
                label.setFontScale(1.0f/1.5f);
            }
            pack();
        }});





    }
}
