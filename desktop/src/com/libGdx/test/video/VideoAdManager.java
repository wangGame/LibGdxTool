package com.libGdx.test.video;

import com.badlogic.gdx.Gdx;

public class VideoAdManager {
    private VideoAdCallback currentCallback;

    public void showVideo(VideoAdCallback callback) {
        // 防止回调串
        if (currentCallback != null) {
            Gdx.app.log("VideoAd", "Ad is showing, ignore.");
            return;
        }
        currentCallback = callback;
        // ===== 正式流程 =====
//        if (!isVideoReady()) {
//            fail();
            return;
//        }
//
//        GamePreferences.getInstance().addAdsTimes();
//        FirebaseUtils.rewarded_Ad();

        // 👉 真正调 Android 广告
//        DoodleAds.showVideoAds();
    }
}
