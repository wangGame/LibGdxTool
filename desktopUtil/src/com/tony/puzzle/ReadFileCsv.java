package com.tony.puzzle;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.resource.csvanddata.ReadCvs;
import com.tony.puzzle.bean.CongfigBean;

import java.io.BufferedReader;
import java.io.File;

public class ReadFileCsv {
   public static void main(String[] args) {
      LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
      config.title="TriPeaks";
      config.x = 0;
      config.y = 0;
      config.height = (int) (640*1.5F);
      config.width = (int) (360*1.5F);
      new LwjglApplication(new Game() {
         @Override
         public void create() {
            FileHandle origin = Gdx.files.internal("origin");
            FileHandle out = Gdx.files.internal("out");
            StringBuilder builder = new StringBuilder();
            for (FileHandle fileHandle : origin.list()) {
               String name = fileHandle.name();
               builder.append(name);
            }
            FileHandle local = Gdx.files.local(out.path()+"/config.csv");
            local.writeString(builder.toString(),false);
         }
      }, config);
   }
}
