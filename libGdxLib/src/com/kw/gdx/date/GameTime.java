package com.kw.gdx.date;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;

import java.util.Timer;
import java.util.TimerTask;

public class GameTime {
    private Array<Runnable> endDayRunnable;
    private Timer timer;
    private String endStr = "23h59m";

    private static GameTime gameTime;

    public GameTime(){
        endDayRunnable = new Array<>();
    }

    public static GameTime getGameTime() {
        if (gameTime == null) {
            gameTime = new GameTime();
        }
        return gameTime;
    }

    public void showFloat(long endTime){
        try {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
        }catch (Exception e) {
        }



        timer = new Timer();
        try {
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    Gdx.app.postRunnable(new Runnable() {
                        @Override
                        public void run() {
                            long now = System.currentTimeMillis();
                            showEnd(endTime - now);
                        }
                    });
                }
            }, 0, 1000);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showEnd(long millis){
        if (millis<0){
            MonthCalendar.dateBean().timeShowUpdate();
            for (Runnable runnable : endDayRunnable) {
                runnable.run();
            }
        }else {
            long seconds = millis / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            if (hours == 0) {
                endStr = formatNum((int) minutes % 60) + "m " + formatNum((int) seconds % 60) + "s";
            } else {
                endStr = formatNum((int) hours % 24) + "h " + formatNum((int) minutes % 60) + "m";
            }
        }
    }

    public String getEndStr() {
        return endStr;
    }

    public String formatNum(int value){
        return String.format("%02d",value);
    }

    public void endRunnable(Runnable endR) {
        endDayRunnable.add(endR);
    }

    public void removeEndDayRunable(Runnable runnable){
        endDayRunnable.removeValue(runnable,true);
    }
}
