package com.kw.gdx.sound;


import com.kw.gdx.asset.Asset;
import com.kw.gdx.constant.Constant;
import com.kw.gdx.resource.annotation.AnnotationInfo;
import com.kw.gdx.resource.annotation.AudioResource;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Array;
import com.kw.gdx.utils.log.NLog;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class AudioProcess {
    public static HashMap<String, SoundAsset> soundAssets = new HashMap<>();
    public static HashMap<String, MusicAsset> musicAssets = new HashMap<>();
    private static String currMusic = "";
    private static HashSet<String> currSoundLoop = new HashSet<>();
    private static boolean onFocus = true;
    private static boolean prepared = false;
    private static HashMap<String,Long> currentAudioLastPlayTime = new HashMap<>();
    private static Array<SoundAsset> soundAssetTemp = new Array<>();
    private static Array<Long> soundLong = new Array<>();
    private static AssetManager assetManager;

    public static void prepare(Class clazz) { // 准备哪些资源将要加载
        clear();
        assetManager = Asset.getAsset().assetManager;
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field declaredField : declaredFields) {
            AudioResource annotation = AnnotationInfo.checkFeildAnnotation(declaredField, AudioResource.class);
            if (annotation != null){
                if (annotation.value()) {
                    try {
                        String value = (String) declaredField.get(clazz);
                        soundAssets.put(value, AAsset.registerSoundAsset("sound/"+ value));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }else {
                    try {
                        String value = (String) declaredField.get(clazz);
                        musicAssets.put(value, AAsset.registerMusicAsset(value,assetManager));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        prepared = true;
        currMusic = "";
        loadSound();
    }

    public static void unloadSound(String soundName){
        SoundAsset soundAsset = soundAssets.get(soundName);
        if(soundAsset!=null) {
            soundAsset.dispose(null);
            soundAssets.remove(soundName);
        }
    }

    public static boolean isPrepared() {
        return prepared;
    }

    public static void setOnFocus(boolean flag) {
        onFocus = flag;
        if (!prepared) return;
        if (onFocus) {
            resumeMusic();
        } else {
            pauseAllMusic();
        }
    }

    public static void loadSound() {
        Iterator<SoundAsset> iterator = soundAssets.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().loading(assetManager);
        }
    }

    public static void loadFinished(){
        Iterator<SoundAsset> iterator = soundAssets.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().finished(assetManager);
        }
    }

    public static void playSoundLoop(String name,float v) {
//        if(!Constant.is)return;
        SoundAsset sa = getSoundAsset(name);
        if (sa!=null) {
            if (sa.isPlaying()) {
                return;
            }
            sa.loop(v);
        }
    }
//
//    public static void

    public static void stopSoundLoop(String name) {
        if (name != null) {
            if (soundAssets.get(name) != null) {
                soundAssets.get(name).stop();
            }
        }
    }

    public static void stopMusicLoop(String name) {
        if (name != null) {
            if (musicAssets.get(name) != null) {
                MusicAsset musicAsset = musicAssets.get(name);
                musicAsset.stopMusic();
            }
        }
    }

    public static void stopSound(String name){
        if (name != null) {
            if (soundAssets.get(name) != null) {
                SoundAsset soundAsset = soundAssets.get(name);
                soundAssets.get(name).stop();
            }
        }
    }

    public static void downSoundVol(String name) {
        if (name != null) {
            if (soundAssets.get(name) != null) {
                SoundAsset soundAsset = soundAssets.get(name);
                soundAssets.get(name).stop();
            }
        }
    }

    public static void stopAllSound() {
        Iterator<String> iterator = currSoundLoop.iterator();
        while (iterator.hasNext()) {
            String soundName = iterator.next();
            if (soundName != null) {
                if (soundAssets.get(soundName) != null) {
                    soundAssets.get(soundName).stop();
                }
            }
        }
        currSoundLoop.clear();
    }


    public static void pauseAllSound() {
        Iterator<String> iterator = currSoundLoop.iterator();
        while (iterator.hasNext()) {
            String soundName = iterator.next();
            if (soundName != null) {
                if (soundAssets.get(soundName) != null) {
                    soundAssets.get(soundName).pause();
                }
            }
        }
    }

    public static void resumeAllSound() {
        Iterator<String> iterator = currSoundLoop.iterator();
        while (iterator.hasNext()) {
            String soundName = iterator.next();
            if (soundName != null) {
                if (soundAssets.get(soundName) != null) {
                    soundAssets.get(soundName).resume();
                }
            }
        }
    }

    public static void playsound(String name,float v,boolean isLoop) {
        NLog.e(name +" is ogg name ！");
        if (!Constant.isSound)return;
        SoundAsset sa = getSoundAsset(name);
        if (sa!=null) {
            if (currentAudioLastPlayTime.containsKey(name)) {
                currentAudioLastPlayTime.remove(name);
            }
            currentAudioLastPlayTime.put(name, System.currentTimeMillis());
            long play;
            if (isLoop){
                play = sa.loop(v);
            }else {
                play = sa.play(v);
            }
            soundAssetTemp.add(sa);
            soundLong.add(play);
            if (soundLong.size>12){
                Long aLong = soundLong.removeIndex(0);
                SoundAsset soundAsset = soundAssetTemp.removeIndex(0);
                soundAsset.stop(aLong);
            }
        }
    }

    private static SoundAsset getSoundAsset(String name) {
        return soundAssets.get(name);
    }


    public static void clear() {
        soundAssets.clear();
        musicAssets.clear();
        prepared = false;
        currMusic = "";
    }

    public static void resumeMusic() {
        if (currMusic == null || currMusic.equals("")) return;
        musicAssets.get(currMusic).resumeMusic();
    }

    private static void pausemusic(String musicName) {
        musicAssets.get(musicName).pauseMusic();
    }

    public static void stopMusic() {
        if (currMusic != null) {
            stopmusic(currMusic);

        }
    }

    public static void stopMusic(String name) {
        if (!currMusic.equals(name))
            return;
        stopmusic(name);
        currMusic = "";
    }

    private static void stopmusic(String musicName) {
        if (musicAssets.get(musicName) != null) {
            musicAssets.get(musicName).stopMusic();
            if (assetManager.isLoaded(musicName)) {
                assetManager.unload(musicName);
            }
        }
    }

    public static void pauseAllMusic() {
        Iterator<MusicAsset> iteratorMusic = musicAssets.values().iterator();
        while (iteratorMusic.hasNext()) {
            iteratorMusic.next().pauseMusic();
        }
    }

    public static void stopAllMusic() {
        Iterator<MusicAsset> iteratorMusic = musicAssets.values().iterator();
        while (iteratorMusic.hasNext()) {
            iteratorMusic.next().stopMusic();
        }
        currMusic = "";
    }

    public static void destory() {
        for (int i = 0; i < musicAssets.size(); i++) {
            musicAssets.get(i).music.dispose();
        }

        for (int i = 0; i < soundAssets.size(); i++) {
            soundAssets.get(i).sound.dispose();
        }
    }

    public static void playMusic(String musicName){
        if(!Constant.isMusic) return;
        if(musicName.equals(currMusic)) return;
        stopmusic(currMusic);
        currMusic = musicName;
//        if (musicName != AudioType.MENU_MUSIC2){
//            musicAssets.get(musicName).playMusicLoop(0.7F);
//        }else {
//            musicAssets.get(musicName).playMusicLoop(1F);
//        }
    }

    public static void playSound(String name) {
        playsound(name,Constant.soundV,false);
    }

    public static void setSoundAssetVolumn(String name, float volumn) {
        SoundAsset soundAsset = soundAssets.get(name);
        soundAsset.setVolume(volumn);
    }
}
