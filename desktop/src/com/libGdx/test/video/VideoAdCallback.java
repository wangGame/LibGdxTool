package com.libGdx.test.video;

public interface  VideoAdCallback {
    void onSuccess();   // 看完拿奖励
    void onSkip();      // 用户关闭
    void onFailed();    // 加载 / 播放失败
}
