package com.kw.gdx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.kw.gdx.screen.BaseScreen;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class PersistentGame extends BaseGame{
    private Screen persistentScreen;

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        if (persistentScreen!=null){
            persistentScreen.resize(width, height);
        }
    }

    @Override
    public void render() {
        super.render();
        if (persistentScreen!=null){
            persistentScreen.render(Gdx.graphics.getDeltaTime());
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        if (persistentScreen!=null){
            persistentScreen.dispose();
        }
    }


    public void setPersistentScreen(Class<? extends BaseScreen> t) {
        Constructor<?> constructor = t.getConstructors()[0];
        try {
            BaseScreen baseScreen = (BaseScreen) constructor.newInstance(this);
            persistentScreen = baseScreen;
            persistentScreen.show();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        super.pause();
        if (persistentScreen!=null){
            persistentScreen.pause();
        }
    }

    @Override
    public void resume() {
        super.resume();
        if (persistentScreen!=null){
            persistentScreen.resume();
        }
    }


    public void removePersistentScreen() {
        if (persistentScreen!=null) {
            persistentScreen.hide();
            persistentScreen.dispose();
            persistentScreen = null;
        }
    }
}
