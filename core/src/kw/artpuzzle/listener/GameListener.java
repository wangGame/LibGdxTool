package kw.artpuzzle.listener;

import kw.artpuzzle.level.LevelView;

public interface GameListener {
    void vibrate(long milliseconds, int amplitude);

    boolean isNetConnect();

    void vibrate1();

    void auto(LevelView view);

    void rate();

    void newRate();

    void showBanner();

    void hideBanner();

    void showInterstitial();

    void showInterstitial(long time);

    void showAdsVideo(Runnable runnable);

    void videoClosed();

    void home();

    boolean isVideoAlready();
}