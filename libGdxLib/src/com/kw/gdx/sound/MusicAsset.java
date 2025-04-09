package com.kw.gdx.sound;


import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.kw.gdx.utils.log.NLog;

public class MusicAsset extends AAsset {
    Music music;
    boolean load = false;
    private AssetManager assetManager;

    public MusicAsset(String name,AssetManager assetManager) {
        this.assetManager = assetManager;
        this.name = name;
    }

    public void playMusicLoop(float v) {
//        if (!Constant.isMusic)return;
        load(name);
        try {
            if (!music.isLooping()) {
                music.setLooping(true);
            }
            music.setVolume(v);
            music.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setVolume(float volume){
        if (music==null) {
            return;
        }
        if (music.isPlaying()) {
            music.setVolume(volume);
        }
    }

    public void playMusicLoop1(float v) {
        load(name);
        try {
            if (!music.isLooping()) {
                music.setLooping(true);
            }
            music.setVolume(v);
            music.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pauseMusic() {
        try {
            if (music != null) {
                music.pause();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resumeMusic() {
        try {
            if (music != null) {
                music.play();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        try {
            if (music != null) {
                music.stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void load(String name) {
        // TODO Auto-generated method stub
        if (!assetManager.isLoaded(name)){
            assetManager.load(name,Music.class);
            assetManager.finishLoading();
        }
        music = assetManager.get(name,Music.class);
        music.setOnCompletionListener(setOnCompletionListener());
    }

    private static Music.OnCompletionListener setOnCompletionListener(){
        return new Music.OnCompletionListener() {
            @Override
            public void onCompletion(Music music) {
                NLog.e("compile ----------------");
            }
        };
    }


    @Override
    public void load() {

    }

    @Override
    public void loading(AssetManager assetManager) {
        // TODO Auto-generated method stub
        if (assetManager.isLoaded(name, Music.class)) {
            assetManager.unload(name);
            assetManager.load(name, Music.class);
        } else {
            assetManager.load(name, Music.class);
        }
    }

    @Override
    public void finished(AssetManager assetManager) {
        // TODO Auto-generated method stub
        if (assetManager.isLoaded(name)) {
            music = assetManager.get(name, Music.class);
        }
    }

    @Override
    public void dispose(AssetManager assetManager) {
        if (music != null) {
            music.dispose();
            music = null;
            load = false;
        }
    }

    public Music getMusic() {
        return music;
    }

    public void setPosition(float position){
        if (music!=null) {
            music.setPosition(position);
        }
    }

}
