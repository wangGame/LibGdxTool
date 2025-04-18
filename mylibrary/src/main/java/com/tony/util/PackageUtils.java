package com.tony.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.kw.gdx.utils.log.NLog;

public class PackageUtils {
    private Context context;

    public PackageUtils(Context context){
        this.context = context;
    }

    public String getApkName(){
        String packageName = context.getPackageName();
        NLog.d("apkinfo --- packageName:"+packageName);
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            NLog.d("apkinfo --- versionName:"+packageInfo.versionName);
            NLog.d("apkinfo --- versionCode:"+packageInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        CharSequence applicationLabel = "";
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            applicationLabel = packageManager.getApplicationLabel(applicationInfo);
            System.out.println(applicationLabel);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return (String) applicationLabel;
    }
}
