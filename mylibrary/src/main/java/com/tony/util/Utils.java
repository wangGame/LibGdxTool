package com.tony.util;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class Utils {
    public boolean initUser(Activity activity) {
        boolean isNewUser=false;
        PackageUtils utils = new PackageUtils(activity);
        String apkName = utils.getApkName();
        SharedPreferences artPuzzle = activity.getSharedPreferences(apkName, Context.MODE_PRIVATE);
        if (!artPuzzle.contains("isFristEnter")){
            isNewUser = true;
            SharedPreferences.Editor edit = artPuzzle.edit();
            edit.putBoolean("isFristEnter",false);
            edit.apply();
        }
        return isNewUser;
    }
}
