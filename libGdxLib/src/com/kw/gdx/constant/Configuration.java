package com.kw.gdx.constant;

import com.kw.gdx.utils.log.NLog;

public class Configuration {
    public static String device_name ="undefined";
    public static int availableMem = 256;
    public static int APILevel = 9;
    public static float scale = 0.8F;
    public static DeviceState device_state = DeviceState.good;
    public static int screen_width = (int)Constant.STDWIDTH;
    public static int screen_height = (int)Constant.STDHIGHT;
    public static int bannerHeight = 90;

    public enum DeviceState{
        poor,good
    }

    public static void init_device() {
        scale = 0.8F;
        device_state = DeviceState.good;
        if(isPoor()){
            device_state = DeviceState.poor;
        }
        NLog.i("device_name :%s  APILevel :%s  availableMem %s",device_name,APILevel ,availableMem);
        if(APILevel < 19 && (screen_height* screen_width >810*480 || availableMem<256)){
            device_state = DeviceState.poor;
            scale = 1;
        }
        if(device_name.equals("GT-P5110")
                ||Configuration.device_name.equals("GT-S5360")
                ||device_name.equals("LG-H410")){
            device_state = DeviceState.poor;
            scale = 0.5F;
        }
        bannerHeight = (int)(50 * Constant.vvv);
    }

    public static boolean isPoor(){
        return device_name.equals("DROIDX")
                || device_name.equals("DROID X2")
                || device_name.equals("SCH-I679")
                || device_state.equals("LG-H410");
    }
}
