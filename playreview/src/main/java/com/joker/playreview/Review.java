package com.joker.playreview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;

public class Review {
    public void startInAppView(Activity context){
        ReviewManager manager = ReviewManagerFactory.create(context);
        manager.requestReviewFlow().addOnCompleteListener(task -> {
            if (task.isSuccessful() && !context.isFinishing()) {
                manager.launchReviewFlow(context, task.getResult());
            }
        });
    }

    public void startReviewPage(Activity activity,String appPackage){
        if (!activity.isFinishing()) {
            String playStoreUri = String.format("market://details?id=%s",appPackage);
            String playStorePage = String.format("https://play.google.com/store/apps/details?id=%s",appPackage);
            try {
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(playStoreUri)));
            }catch (Exception e){
                activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(playStorePage)));
            }
        }
    }
}
