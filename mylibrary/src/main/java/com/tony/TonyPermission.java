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

    //android.permission.POST_NOTIFICATIONS
    public void requestNotificationPermission(String permission) {
        if (Build.VERSION.SDK_INT < 33 || hasNotificationPermission(permission)) {
            return;
        }
        ActivityCompat.requestPermissions(context, new String[]{permission}, 101);
    }

    public  boolean hasNotificationPermission(String permission) {
        try {
            return ContextCompat.checkSelfPermission(context, permission) == 0;
        } catch (Exception unused) {
            return false;
        }
    }
}
