//package com.libGdx.test.other;
//
//import com.badlogic.gdx.ApplicationAdapter;
//import com.badlogic.gdx.ApplicationListener;
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
//import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
//import com.badlogic.gdx.graphics.GL20;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.glutils.ShaderProgram;
//import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
//import com.badlogic.gdx.math.Bezier;
//import com.badlogic.gdx.math.Vector2;
//
//import kw.test.file.Bean;
//import kw.test.file.ReadFileConfig;
//
///**
// * @Auther jian xian si qi
// * @Date 2023/6/29 18:17
// */
//class CutX implements ApplicationAdapter {
//    public static void main(String[]a) {
//        ReadFileConfig readFileConfig = new ReadFileConfig();
//        Bean value = readFileConfig.getValue();
//        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
//        config.title=value.getName();
//        config.x = 1000;
//        config.y = 0;
//        config.height = (int) (1920 );
//        config.width = (int) (1080  );
//        Gdx.isJiami = true;
//        CutX dekstop = new CutX();
//        new LwjglApplication(dekstop, config);
//    }
//    private Trail trail;
//
//
//    @Override
//    public void create() {
//        trail = new Trail();
//
//    }
//
//    @Override
//    public void resize(int width, int height) {
//
//    }
//
//    @Override
//    public void render() {
//        float deltaTime = Gdx.graphics.getDeltaTime();
//
//        // 更新并渲染拖尾效果
//        trail.update(deltaTime);
//        trail.render(batch);
//    }
//
//    @Override
//    public void touchDragged(int screenX, int screenY, int pointer) {
//        // 当用户拖动时，更新拖尾效果的位置
//        Vector3 worldPos = new Vector3(screenX, screenY, 0);
//        camera.unproject(worldPos);
//        trail.setPosition(worldPos.x, worldPos.y);
//    }
//
//    @Override
//    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
//        // 当用户开始拖动时，启动拖尾效果
//        trail.start();
//        return true;
//    }
//
//    @Override
//    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//        // 当用户停止拖动时，停止拖尾效果
//        trail.stop();
//        return true;
//    }
//    @Override
//    public void pause() {
//
//    }
//
//    @Override
//    public void resume() {
//
//    }
//
//    @Override
//    public void dispose() {
//
//    }
//}
