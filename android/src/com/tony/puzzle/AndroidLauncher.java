package com.tony.puzzle;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.kw.gdx.constant.Configuration;
import com.kw.gdx.constant.Constant;
import com.libGdx.test.trile.TestGame;
import com.tony.BaseAndroidLauncher;
import com.tony.TonyPermission;

import java.util.Random;

public class AndroidLauncher extends BaseAndroidLauncher {
    public static boolean isDebug = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TonyPermission tonyPermission = new TonyPermission(this);
        if (!tonyPermission.hasNotificationPermission(Manifest.permission.POST_NOTIFICATIONS)) {
            tonyPermission.requestNotificationPermission(Manifest.permission.POST_NOTIFICATIONS);
        }
        AndroidApplicationConfiguration configuration = new AndroidApplicationConfiguration();
        if (Configuration.device_state != Configuration.DeviceState.poor) {
            configuration.r = 8;
            configuration.g = 8;
            configuration.b = 8;
        }
        //指南针
        configuration.useCompass = false;
        //加速度
        configuration.useAccelerometer = false;
        configuration.useWakelock = true;
        configuration.numSamples = 2;
        Constant.realseDebug = isDebug;
        if (Build.MODEL.equals("MediaPad 10 FHD")) {
            configuration.numSamples = 0;
        }
        initialize(new TestGame(),configuration);
    }

    /**
     * AB
     * @param pki
     * @return
     */
    public String diviceAB(String pki){
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String abversion = "A";
        if (true){
            Random random = new Random();
            String AB = "A";
            if (10 < random.nextInt(20)){
                AB = "A";
            }else{
                AB = "B";
            }
            abversion = AB;
            try {
                PackageManager packageManager = this.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
                String versionName = packageInfo.versionName;
                int versionCode = packageInfo.versionCode;
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("intsallApkVersionName",versionName);
                edit.putInt("intsallApkVersionCode",versionCode);
                edit.putString("ABVERSION",AB);
                edit.commit();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }else {
            try {
                PackageManager packageManager = this.getPackageManager();
                PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
                int versionCode = packageInfo.versionCode;
                int code = sharedPreferences.getInt("intsallApkVersionCode", 0);
                abversion = sharedPreferences.getString("ABVERSION","A");
                if (code != versionCode){
                    abversion = "A";
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        return abversion;
    }
}
