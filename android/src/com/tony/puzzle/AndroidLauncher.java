package com.tony.puzzle;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DisplayCutout;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowInsets;
import android.view.WindowManager;

import androidx.annotation.RequiresApi;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.kw.gdx.BaseGame;
import com.kw.gdx.constant.Configuration;
import com.kw.gdx.constant.Constant;
import com.tony.BaseAndroidLauncher;
import com.tony.SafeAreaInsetsUtils;
import com.tony.TonyPermission;
import com.tony.util.PackageUtils;

import java.util.Random;
import java.util.UUID;

public class AndroidLauncher extends BaseAndroidLauncher {
    public static boolean isDebug = false;
    private boolean isNewUser;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT_WATCH)
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
        initUser();
        initialize(new TestGame(),configuration);
    }

    private void initUser() {
        SharedPreferences artPuzzle = getSharedPreferences("ArtPuzzle", Context.MODE_PRIVATE);
        if (!artPuzzle.contains("isFristEnter")){
            isNewUser = true;
        }
    }

    public String diviceAB(String pki){
        String AB = "AG";
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        if (isNewUser){
            PackageManager packageManager = this.getPackageManager();
            try {
                PackageInfo packageInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.putString("apkVersion",packageInfo.versionName);
                edit.commit();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            Random random = new Random();
            int v = (int) (random.nextInt(20));
            if (v < 10){
                AB = "AG";
            }else{
                AB = "AH";
            }
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("ABVERSION",AB);
            edit.commit();
        }
        String abversion = sharedPreferences.getString("ABVERSION",AB);
        if (abversion.equalsIgnoreCase("AG") ||
                abversion.equalsIgnoreCase("AH")){
            if (abversion.equalsIgnoreCase("AG")) {
                abversion = "A";
            }else if (abversion.equalsIgnoreCase("AH")){
                abversion = "B";
            }else {
                abversion = "A";
            }
        }else{
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.putString("ABVERSION","");
            edit.commit();
            abversion = "";
        }
        return abversion;
    }
}
