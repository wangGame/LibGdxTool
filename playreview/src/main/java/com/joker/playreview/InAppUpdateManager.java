package com.joker.playreview;

import android.app.Activity;

public class InAppUpdateManager {
    private boolean keepRequesting = true;

    public void checkUpdate(Activity activity) {
        if (keepRequesting) {
            keepRequesting = false;
            val appUpdateManager = AppUpdateManagerFactory.create(activity.applicationContext)
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                        !activity.isFinishing
                ) {
                    appUpdateManager.startUpdateFlow(
                            appUpdateInfo,
                            activity,
                            AppUpdateOptions.defaultOptions(AppUpdateType.FLEXIBLE),
                            )
                }
            }
        }
    }
}
