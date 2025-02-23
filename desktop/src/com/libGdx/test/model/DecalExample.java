//package com.libGdx.test.model;
//
//import com.badlogic.gdx.graphics.Camera;
//import com.badlogic.gdx.graphics.OrthographicCamera;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.graphics.g2d.TextureRegion;
//import com.badlogic.gdx.graphics.g3d.decals.Decal;
//import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
//
//public class DecalExample {
//    private SpriteBatch batch;
//    private DecalBatch decalBatch;
//    private Camera camera;
//    private Decal decal;
//
//    public void create() {
//        batch = new SpriteBatch();
//        decalBatch = new DecalBatch();
//        camera = new OrthographicCamera(800, 600); // 创建一个正交相机
//
//        Texture texture = new Texture("image.png"); // 加载纹理
//        TextureRegion region = new TextureRegion(texture);
//        decal = Decal.newDecal(region); // 创建 Decal
//
//        // 设置 Decal 的位置
//        decal.setPosition(200, 200, 0);
//        decal.setScale(1.5f, 1.5f); // 缩放大小
//    }
//
//    public void render() {
//        camera.update();
//        decalBatch.begin(camera.combined, new LightManager().getLights()); // 渲染时传递摄像机和光照管理器
//        decalBatch.add(decal); // 添加 Decal 到批处理中
//        decalBatch.end();
//    }
//
//    public void dispose() {
//        batch.dispose();
//        decalBatch.dispose();
//    }
//}
