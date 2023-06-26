package com.kw.gdx.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;

public class SoundAsset extends AAsset {

    private boolean playing;
    public Sound sound;
    float v = 1f;
    boolean load = false;
    public long soundId = -1;
    public SoundAsset(Sound sound, String name) {
        this.sound = sound;
        this.name = name;
    }

    public SoundAsset(String name) {
        this.name = name;
    }

    public void setVolume(float v) {
        this.v = MathUtils.clamp(v,0,1);
        if (soundId!=-1){
            setVolume(soundId,v);
        }
    }

    public void setVolume(long soundId,float v) {
        v = MathUtils.clamp(v,0,1);
        try {
            sound.setVolume(soundId,v);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public long play() {
        if (sound != null) {
            try {
                soundId = sound.play(v);
                return soundId;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public void stop() {
        if (sound != null) {
            try {
                isPlaying = false;
                sound.stop();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        soundId = -1;
    }

    public void stop(long id) {
        if (sound != null) {
            try {
                sound.stop(id);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        soundId = -1;
    }


    public void pause() {
        if (sound != null) {
            try {
                sound.pause();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public long loop() {
        if (sound != null) {
            try {
                soundId = sound.loop(v);
                return soundId;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }


    private boolean isPlaying;

    public boolean isPlaying() {
        return isPlaying;
    }

    public long loop(float v) {
        if (sound != null) {
            try {
                isPlaying = true;
                soundId = sound.loop(v);
                return soundId;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }


    public void resume(){
        if (sound != null) {
            try {
                sound.resume();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public long play(float v) {
        v = MathUtils.clamp(v,0,1);
        soundId = this.sound.play(v);
        return soundId;
    }

    public void setPitch(float pitch) {
        if (soundId==-1)return;
        try {
            sound.setPitch(soundId, pitch);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override
    public void load() {
        if (load)
            return;
        try {
            sound = Gdx.audio.newSound(Gdx.files.internal(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        load = true;
    }

    @Override
    public void loading(AssetManager assetManager) {
        // TODO Auto-generated method stub
        if (assetManager.isLoaded(name, Sound.class)) {
            assetManager.unload(name);
            assetManager.load(name, Sound.class);
        } else {
            assetManager.load(name, Sound.class);
        }
    }

    @Override
    public void finished(AssetManager assetManager) {
        // TODO Auto-generated method stub
        if (assetManager.isLoaded(name)) {
            sound = assetManager.get(name, Sound.class);
        }
    }

    @Override
    public void dispose(AssetManager assetManager) {
        // TODO Auto-generated method stub
        if (sound != null) {
            sound.dispose();
            sound = null;
            load = false;
        }
    }

}
