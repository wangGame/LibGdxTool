package com.joker.playreview;

import android.content.Context;

import com.google.android.gms.common.wrappers.InstantApps;

public class InstantAppManager {
    public boolean isInstall(Context context){
        return InstantApps.isInstantApp(context);
    }
}
