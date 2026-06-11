package com.joker.puzzle;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.graphics.Insets;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.joker.dialog.UserInputDialog;
import com.joker.domos.GameTest;
import com.joker.domos.listener.UserInputListener;
import com.kw.gdx.constant.Configuration;
import com.kw.gdx.constant.Constant;
import com.joker.BaseAndroidLauncher;
import com.joker.TonyPermission;


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
        configuration.useAccelerometer = true;
        configuration.useWakelock = true;
        configuration.numSamples = 2;
        Constant.realseDebug = isDebug;
        if (Build.MODEL.equals("MediaPad 10 FHD")) {
            configuration.numSamples = 0;
        }

        ViewCompat.setOnApplyWindowInsetsListener(getWindow().getDecorView(), (v, insets) -> {
            Insets systemBars = insets.getInsets(
                    WindowInsetsCompat.Type.systemBars()
            );

            Insets cutout = insets.getInsets(
                    WindowInsetsCompat.Type.displayCutout()
            );

            Configuration.left = Math.max(systemBars.left, cutout.left);
            Configuration.top = Math.max(systemBars.top, cutout.top);
            Configuration.right = Math.max(systemBars.right, cutout.right);
            Configuration.bottom = Math.max(systemBars.bottom, cutout.bottom);

            System.out.println(Configuration.left + " ===========> " + Configuration.top + " " + Configuration.right + " " + Configuration.bottom);

            return insets;
        });
        initialize(new GameTest(new UserInputListener() {
            @Override
            public void showHandleInput(String hint, Input.TextInputListener callback) {
                runOnUiThread(() -> {
                    try {
                        new UserInputDialog()
                                .setHint(hint)
                                .setOnSubmitListener(text -> {
                                    if (callback != null) {
                                        Gdx.app.postRunnable(() -> callback.input(text));
                                    }
                                })
                                .setOnCancelListener(() -> {
                                    if (callback != null) {
                                        Gdx.app.postRunnable(callback::canceled);
                                    }
                                })
                                .show(getFragmentManager(), "unity_input_bar");
                    } catch (Exception e) {
                        if (callback != null) {
                            Gdx.app.postRunnable(callback::canceled);
                        }
                    }
                });
            }
        }), configuration);






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
        }
        return abversion;
    }
}
