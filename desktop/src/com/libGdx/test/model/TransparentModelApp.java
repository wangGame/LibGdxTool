package com.libGdx.test.model;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;


public class TransparentModelApp implements ApplicationListener {

    private ModelBatch modelBatch;
    private Environment environment;
    private ModelInstance modelInstance;
    public OrthographicCamera cam;//3D视角
    @Override
    public void create() {
        modelBatch = new ModelBatch();
        environment = new Environment();

        // 创建模型实例
        Model model = createTransparentModel();
        modelInstance = new ModelInstance(model);


        cam = new OrthographicCamera(  10, 10);//67可以理解成一个定值，视角宽度（67度）
        cam.position.set(0f, 0f, 170f);
        cam.lookAt(0,0,0);
        cam.near = 1f;
        cam.far = 1300f;
        cam.update();
    }

    private Model createTransparentModel() {
        ModelBuilder modelBuilder = new ModelBuilder();
        Model model = modelBuilder.createBox(1f, 1f, 1f,
                new Material(ColorAttribute.createDiffuse(Color.RED)),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal);

        // 获取材质并设置透明度
        Material material = model.materials.get(0);
        ColorAttribute attribute = (ColorAttribute) material.get(ColorAttribute.Diffuse);

        Color color = attribute.color;
        color.a = 0.5f;  // 设置透明度为50%

        // 设置透明混合
//        material.set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));

        return model;
    }

    @Override
    public void render() {
        modelBatch.begin(cam);
        modelBatch.render(modelInstance, environment);
        modelBatch.end();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        modelBatch.dispose();
        modelInstance.model.dispose();
    }


    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.x = 1000;
        config.stencil=8;
        config.y = 0;
        config.height = (int) (1920 * 0.25f);
        config.width = (int) (1080 * 0.5f);
        Gdx.isJiami = true;
        new LwjglApplication(new TransparentModelApp(), config);

    }

}
