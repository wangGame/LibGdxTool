package com.tony;

import android.app.Activity;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class TonyPermission {
    private Activity context;
    public TonyPermission(Activity context){
        this.context = context;
    }

    public void requestNotificationPermission() {
        if (Build.VERSION.SDK_INT < 33 || hasNotificationPermission()) {
            return;
        }
        ActivityCompat.requestPermissions(context, new String[]{"android.permission.POST_NOTIFICATIONS"}, 101);
    }

    public  boolean hasNotificationPermission() {
        try {
            return ContextCompat.checkSelfPermission(context, "android.permission.POST_NOTIFICATIONS") == 0;
        } catch (Exception unused) {
            return false;
        }
    }
}
