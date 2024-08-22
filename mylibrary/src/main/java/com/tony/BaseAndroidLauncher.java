package com.tony;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.kw.gdx.constant.Configuration;
import com.kw.gdx.constant.Constant;
import com.tony.util.PackageUtils;
import com.tony.util.Utils;

import java.util.Random;
import java.util.UUID;

public class BaseAndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!isTaskRoot()){
            finish();
            return;
        }
        initImmersiveMode();
    }

    private String getUUID() {
        String uuid = PreferenceManager.getDefaultSharedPreferences(this).getString("UUID", null);
        if (uuid != null){
            System.out.println(uuid);
            return uuid;
        }else {
            UUID uuid1 = UUID.randomUUID();
            PreferenceManager
                    .getDefaultSharedPreferences(this)
                    .edit()
                    .putString("UUID",uuid1.toString())
                    .apply();
            return uuid1.toString();
        }
    }

    public String diviceAB(String pki){
        String AB = "AG";
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        Utils utils = new Utils();
        if (utils.initUser(this)){
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



    @SuppressLint("NewApi")
    private void initImmersiveMode() {
        if (Build.VERSION.SDK_INT >= 19) {
            View.OnSystemUiVisibilityChangeListener listener = new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        enterImmersiveMode();
                    }
                }
            };
            getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(listener);
            enterImmersiveMode();
        }
    }

    @SuppressLint("NewApi")
    private void enterImmersiveMode() {
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            );
        }
    }

    public boolean checkNet(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            @SuppressLint("MissingPermission") NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        initImmersiveMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private int getAdWidth() {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = this.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        return (int) (widthPixels / density);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        graphics.getView().requestFocus();
        return super.dispatchKeyEvent(event);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            SafeAreaInsetsUtils.getSafeAreaInsets(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
