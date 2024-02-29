package com.tony.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

public class PackageUtils {
    private Context context;
    public PackageUtils(Context context){
        this.context = context;
    }

    public String getApkName(){
        String packageName = context.getPackageName();
        System.out.println("apkinfo --- packageName:"+packageName);
        PackageManager packageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            System.out.println("apkinfo --- versionName:"+packageInfo.versionName);
            System.out.println("apkinfo --- versionCode:"+packageInfo.versionCode);
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
